package jus.proc.prodcons;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon{

	private int nbConsommateur;
	private int nbProducteur;

	public ProdCons(){
		nbConsommateur =0;
	}
	
	@Override
	public int enAttente() {
		return nbConsommateur;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		
	}

	@Override
	public int taille() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public synchronized void beginComsommateur(){
		while(nbConsommateur>0 || nbProducteur>0){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		nbConsommateur++;
		
	}
	
	public synchronized void endComsommateur(){
		nbConsommateur--;
		notifyAll();
	}
	
	public synchronized void beginProducteur(){
		while(nbConsommateur>0 || nbProducteur>0){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		nbProducteur++;
		
	}
	
	public synchronized void endProducteur(){
		nbProducteur--;
		notifyAll();
		
	}
}
