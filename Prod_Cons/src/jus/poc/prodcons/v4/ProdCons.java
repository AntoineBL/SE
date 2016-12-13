package jus.poc.prodcons.v4;

import java.util.HashMap;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v2.Semaphore;

public class ProdCons implements Tampon{

	// Indice actuel du pointeur buffer pour les producteurs
	private int iProd = 0;
	// Indice actuel du pointeur buffer pour les consommateurs
	private int iCons = 0;
	
	// Le buffer circulaire 
	private Message[] buffer;
	private int tailleBuffer;
	
	// Nombre de message dans le buffer
	private int nbMessageBuffer = 0;
	
	private Observateur observateur;
	
	private Semaphore notFull;
	private Semaphore mutexProd;
	private Semaphore notEmpty;
	private Semaphore mutexCons;
	//private Semaphore test;
	
	private HashMap<Message, Integer> prodExemplaire = null;


	
	public ProdCons(int taille, Observateur observateur){
		this.tailleBuffer = taille;
		this.buffer = new Message[tailleBuffer];
		this.observateur = observateur;
		
		this.notFull = new Semaphore(tailleBuffer);
		this.mutexProd = new Semaphore(1);
		this.notEmpty = new Semaphore(0);
		this.mutexCons = new Semaphore(1);
		//this.test = new Semaphore(1);
		
		prodExemplaire = new HashMap<Message, Integer>();
	}
	
	
	@Override
	public synchronized int enAttente() {
		return nbMessageBuffer;	
	}

	
	@Override
	public Message get(_Consommateur consommateur) throws InterruptedException, ControlException {
		
		MessageX msg;
		int eRestants;
		
		notEmpty.P();
		mutexCons.P();
	
				msg = (MessageX) buffer[iCons];

			System.out.println(this.enAttente());
			
			eRestants = this.prodExemplaire.get(msg);
			eRestants--;
			this.prodExemplaire.put(msg, eRestants); // ACO : synchroniser sur this.prodExemplaire
			

			observateur.retraitMessage(consommateur, msg);
			System.out.println("\n Le consommateur: "+consommateur.identification()+" vient de retirer le message "+msg.toStringSimple());
			
			if (eRestants == 0) {
				iCons = (iCons +1) % tailleBuffer;
				nbMessageBuffer--;
				synchronized(msg) {
					msg.notifyAll();	
				}
		mutexCons.V(); // remettre ou c'etait avant ACO
			} else {
		mutexCons.V();
				synchronized(msg) {
					msg.wait(); 
				}
			}

		notFull.V();
		return msg;
	}


	@Override
	public void put(_Producteur producteur, Message msg) throws Exception, InterruptedException {
		
		
		notFull.P();
		//System.out.println("------------"+msg+"-------------");
		mutexProd.P();
			buffer[iProd] = msg;
			iProd = (iProd +1) % tailleBuffer;
			nbMessageBuffer++;
			int exemplaireRestants = ((MessageX) msg).getnbExemplaire(); 
			this.prodExemplaire.put(msg, exemplaireRestants);
			
			observateur.depotMessage(producteur, msg);
			System.out.println("\n Le producteur: "+producteur.identification()+" vient de produire un message: "+msg.toString());

		//test.P();
			for(int i =0; i < exemplaireRestants; i++ ) {
				notEmpty.V();
			}
		//test.V();
		mutexProd.V();
			synchronized(msg) {
				if (this.prodExemplaire.get(msg) != 0)
				msg.wait(); 
		}

		
	}

	@Override
	public int taille() {
		return tailleBuffer;
	}
	
	public int getnbMessageBuffer() {
		return nbMessageBuffer;
	}
	
}
