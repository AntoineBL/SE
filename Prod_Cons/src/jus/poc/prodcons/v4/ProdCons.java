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
	
	private HashMap<Integer, Integer> prodExemplaire = null;
	private HashMap<Integer, Semaphore> prodSemaphore = null;

	
	public ProdCons(int taille, Observateur observateur){
		this.tailleBuffer = taille;
		this.buffer = new Message[tailleBuffer];
		this.observateur = observateur;
		
		this.notFull = new Semaphore(tailleBuffer);
		this.mutexProd = new Semaphore(1);
		this.notEmpty = new Semaphore(0);
		this.mutexCons = new Semaphore(1);
		
		prodSemaphore = new HashMap<Integer, Semaphore>();
		prodExemplaire = new HashMap<Integer, Integer>();
	}
	
	
	@Override
	public synchronized int enAttente() {
		return nbMessageBuffer;	
	}

	
	@Override
	public Message get(_Consommateur consommateur) throws InterruptedException, ControlException {
		
		MessageX msg;
		boolean terminee;
		notEmpty.P();
		mutexCons.P();
			msg = (MessageX) buffer[iCons];
			
			int wRestants = this.prodExemplaire.get(msg.getProducteurId());
			this.prodExemplaire.put(msg.getProducteurId(), --wRestants);
			
			
			if (wRestants == 0) {
				iCons = (iCons +1) % tailleBuffer;
				//nbMessageBuffer--;
				terminee = true;
				
			} else {
				/* Réveille un consommateur sinon */
				terminee = false;
			}
			

			observateur.retraitMessage(consommateur, msg);
			System.out.println("\n Le consommateur: "+consommateur.identification()+" vient de retirer le message "+msg.toStringSimple());
		mutexCons.V();
		
		if (terminee) {
			synchronized(msg) {
				msg.notifyAll();	
			}
//			prodSemaphore.get(msg.getProducteurId()).V();
//			this.mutexProd.V();
		} else {
			notEmpty.V();
			synchronized(msg) {
			msg.wait(); }
		}

		notFull.V();
		return msg;
	}


	@Override
	public void put(_Producteur producteur, Message msg) throws Exception, InterruptedException {
		
		
		notFull.P();
		
		mutexProd.P();
			buffer[iProd] = msg;
			iProd = (iProd +1) % tailleBuffer;
			//nbMessageBuffer++;
			
			int exemplaireRestants = ((MessageX) msg).getnbExemplaire(); 
			this.prodExemplaire.put(producteur.identification(), exemplaireRestants);
			
			observateur.depotMessage(producteur, msg);
			System.out.println("\n Le producteur: "+producteur.identification()+" vient de produire un message: "+msg.toString());
		mutexProd.V();
		
		notEmpty.V();
		synchronized(msg) {
		msg.wait(); }
//		Semaphore wSemaphore = new Semaphore(0);
//		prodSemaphore.put(((MessageX) msg).getProducteurId(), wSemaphore);
//		wSemaphore.P();
		
	}

	@Override
	public int taille() {
		return tailleBuffer;
	}
	
	public int getnbMessageBuffer() {
		return nbMessageBuffer;
	}
	
}
