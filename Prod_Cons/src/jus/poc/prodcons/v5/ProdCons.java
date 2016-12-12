package jus.poc.prodcons.v5;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v2.Semaphore;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	
    private final Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
	
	public ProdCons(int taille, Observateur observateur){
		this.tailleBuffer = taille;
		this.buffer = new Message[tailleBuffer];
		this.observateur = observateur;

	}
	
	
	@Override
	public synchronized int enAttente() {
		return nbMessageBuffer;	
	}

	
	@Override
	public Message get(_Consommateur consommateur) throws InterruptedException, ControlException {
		
		lock.lock();
		
		try {
	        while (nbMessageBuffer <= 0) {
	            notEmpty.await();
	        }
			
			MessageX msg = (MessageX) buffer[iCons];
			iCons = (iCons +1) % tailleBuffer;
	
			
			observateur.retraitMessage(consommateur, msg);
			System.out.println("\n Le consommateur: "+consommateur.identification()+" vient de retirer le message "+msg.toStringSimple());
			
			notFull.signal();
			
			return msg;
		} finally {
			lock.unlock();
		}
	}


	@Override
	public void put(_Producteur producteur, Message msg) throws Exception, InterruptedException {
		
		lock.lock();
		
		try {
			while(nbMessageBuffer >= tailleBuffer) {
				notFull.await();
			}
			buffer[iProd] = msg;
			iProd = (iProd +1) % tailleBuffer;
			
			observateur.depotMessage(producteur, msg);
			System.out.println("\n Le producteur: "+producteur.identification()+" vient de produire un message: "+msg.toString());
			
			notEmpty.signal();
		} finally {
			lock.unlock();
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
