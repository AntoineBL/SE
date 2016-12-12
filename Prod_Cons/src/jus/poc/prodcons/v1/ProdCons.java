package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

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
	
	
	public ProdCons(int taille){
		this.tailleBuffer = taille;
		this.buffer = new Message[tailleBuffer];
	}
	
	
	@Override
	public synchronized int enAttente() {
		return nbMessageBuffer;	
	}

	
	@Override
	public synchronized Message get(_Consommateur consommateur) throws InterruptedException {
		while(nbMessageBuffer <= 0) {
			wait();
		}
		MessageX msg = (MessageX) buffer[iCons];
		iCons = (iCons +1) % tailleBuffer;
		nbMessageBuffer--;
		
		System.out.println("Le consommateur: "+consommateur.identification()+" vient de retirer le message "+msg.toStringSimple()+"\n");
		notifyAll();
		return msg;
	}


	@Override
	public synchronized void put(_Producteur producteur, Message msg) throws Exception, InterruptedException {
		
		while(nbMessageBuffer >= tailleBuffer) {
			wait();
		}
		buffer[iProd] = msg;
		iProd = (iProd +1) % tailleBuffer;
		nbMessageBuffer++;
		
		System.out.println("Le producteur: "+producteur.identification()+" vient de produire un message: "+msg.toString()+"\n");
		notifyAll();
	}

	@Override
	public int taille() {
		return tailleBuffer;
	}
	
	public int getnbMessageBuffer() {
		return nbMessageBuffer;
	}
	
	
}
