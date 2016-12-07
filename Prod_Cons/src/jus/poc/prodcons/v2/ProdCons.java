package jus.poc.prodcons.v2;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v2.Semaphore;

public class ProdCons implements Tampon{

	private int nbMessageAttente;
	private int taille;
	private Message []buffer;
	private int indiceBuffer;
	private int indiceBufferBefor;
	
	private Semaphore notFull;
	private Semaphore mutexIn;
	private Semaphore notEmpty;
	private Semaphore mutexOut;
	

	public ProdCons(int taille){
		nbMessageAttente =0;
		this.taille = taille;
		buffer = new Message[this.taille] ;
		indiceBuffer = 0;
		
		notFull = new Semaphore(taille);
		mutexIn = new Semaphore(1);
		notEmpty = new Semaphore(0);
		mutexOut = new Semaphore(1);
	}
	
	@Override
	public int enAttente() {
		return nbMessageAttente;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		
		
		nbMessageAttente--;
		
		
		notEmpty.P();
		mutexOut.P();
		indiceBufferBefor = indiceBuffer;
		indiceBuffer = (indiceBuffer + 1)%taille;
		mutexOut.V();
		notFull.V();
		
		
		return buffer[indiceBufferBefor];
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		
		notFull.P();
		mutexIn.P();
		buffer[(nbMessageAttente + indiceBuffer)%taille] = arg1;
		
		System.out.println(buffer[nbMessageAttente + indiceBuffer]);
		System.out.println(nbMessageAttente);
		nbMessageAttente++;
		
		mutexIn.V();
		notEmpty.V();
		
		
		
	}

	@Override
	public int taille() {
		return this.taille;
	}
	
}
