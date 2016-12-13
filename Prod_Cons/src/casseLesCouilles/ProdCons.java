package casseLesCouilles;

import java.util.HashMap;
import java.util.Map;

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
	
	private Map<Integer, Semaphore> prodSemaphore = null;
	private Map<Integer, Integer> prodExemplaire = null;
	


	
	public ProdCons(int taille, Observateur observateur){
		this.tailleBuffer = taille;
		this.buffer = new Message[tailleBuffer];
		this.observateur = observateur;
		
		this.notFull = new Semaphore(tailleBuffer);
		this.mutexProd = new Semaphore(1);
		this.notEmpty = new Semaphore(0);
		this.mutexCons = new Semaphore(1);
		
		prodExemplaire = new HashMap<Integer, Integer>();
		prodSemaphore = new HashMap<Integer, Semaphore>();
	}
	
	
	@Override
	public synchronized int enAttente() {
		return nbMessageBuffer;	
	}

	
	@Override
	public Message get(_Consommateur consommateur) throws InterruptedException, ControlException {
		
		MessageX msg;
		boolean Pasterminee;
		int eRestants;
		
		notEmpty.P();
		mutexCons.P();
			msg = (MessageX) buffer[iCons];
			eRestants = this.prodExemplaire.get(msg.getProducteurId());
			eRestants--;
			this.prodExemplaire.put(msg.getProducteurId(), eRestants);
			
			if (eRestants == 0) {
				iCons = (iCons +1) % tailleBuffer;
				Pasterminee = false;
				
			} else {
				Pasterminee = true;
			}

			observateur.retraitMessage(consommateur, msg);
			System.out.println("\n Le consommateur: "+consommateur.identification()+" vient de retirer le message "+msg.toStringSimple());
		mutexCons.V();
		
		if (Pasterminee) {
			notEmpty.V();
			synchronized(msg) {
				msg.wait();
			}
			} 
		else {
			synchronized(msg) {
				msg.notifyAll();
			}
			prodSemaphore.get(msg.getProducteurId()).V();
			notFull.V();
		}

		return msg;
	}


	@Override
	public void put(_Producteur producteur, Message msg) throws Exception, InterruptedException {
		
		
		notFull.P();
		
		mutexProd.P();
			buffer[iProd] = msg;
			iProd = (iProd +1) % tailleBuffer;
			
			int exemplaireRestants = ((MessageX) msg).getnbExemplaire(); 
			this.prodExemplaire.put(producteur.identification(), exemplaireRestants);
			
			observateur.depotMessage(producteur, msg);
			System.out.println("\n Le producteur: "+producteur.identification()+" vient de produire un message: "+msg.toString());
		mutexProd.V();
		notEmpty.V();
		/* Création de la Sémaphore permettant l'attente de la fin de conso */
		Semaphore wSemaphore = new Semaphore(0);
		prodSemaphore.put(((MessageX) msg).getProducteurId(), wSemaphore);
		wSemaphore.P();

		
	}

	@Override
	public int taille() {
		return tailleBuffer;
	}
	
	public int getnbMessageBuffer() {
		return nbMessageBuffer;
	}
	
}
