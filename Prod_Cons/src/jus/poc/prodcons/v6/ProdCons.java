package jus.poc.prodcons.v6;

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
	private MyObservateur myObservateur;
	
	private Semaphore notFull;
	private Semaphore mutexProd;
	private Semaphore notEmpty;
	private Semaphore mutexCons;

	
	public ProdCons(int taille, Observateur observateur, MyObservateur myObservateur){
		this.tailleBuffer = taille;
		this.buffer = new Message[tailleBuffer];
		this.observateur = observateur;
		this.myObservateur = myObservateur;
		
		this.notFull = new Semaphore(tailleBuffer);
		this.mutexProd = new Semaphore(1);
		this.notEmpty = new Semaphore(0);
		this.mutexCons = new Semaphore(1);
	}
	
	
	@Override
	public synchronized int enAttente() {
		return nbMessageBuffer;	
	}

	
	@Override
	public Message get(_Consommateur consommateur) throws InterruptedException, ControlException {
		
		
		notEmpty.P();
		mutexCons.P();
		
		MessageX msg = (MessageX) buffer[iCons];
		iCons = (iCons +1) % tailleBuffer;
		nbMessageBuffer--;
		//observateur.retraitMessage(consommateur, msg);
		myObservateur.retraitMessage(consommateur, msg);
		System.out.println("\n Le consommateur: "+consommateur.identification()+" vient de retirer le message "+msg.toStringSimple());
		mutexCons.V();
		notFull.V();
		
		return msg;
	}


	@Override
	public void put(_Producteur producteur, Message msg) throws Exception, InterruptedException {
		
		
		notFull.P();
		mutexProd.P();

		buffer[iProd] = msg;
		iProd = (iProd +1) % tailleBuffer;
		nbMessageBuffer++;
		//observateur.depotMessage(producteur, msg);
		myObservateur.depotMessage(producteur, msg);
		System.out.println("\n Le producteur: "+producteur.identification()+" vient de produire un message: "+msg.toString());
		mutexProd.V();
		notEmpty.V();
	}

	@Override
	public int taille() {
		return tailleBuffer;
	}
	
	public int getnbMessageBuffer() {
		return nbMessageBuffer;
	}
	
}
