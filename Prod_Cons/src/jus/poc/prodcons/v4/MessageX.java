package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message{

	// Producteur du message
	private Producteur mProducteur;	
	
	private int nbExemplaire;
	
	// ID du message
	private int IDmessage;
	
	private Semaphore sMessage;
	
	public MessageX(Producteur producteur, int IDmessage, int nbExemplaire) {
		this.nbExemplaire = nbExemplaire;
		this.mProducteur = producteur;
		this.IDmessage = IDmessage;
		this.sMessage = new Semaphore(this.nbExemplaire);
		
	}
	
	public String toString() {
		
		return "<<<Message NO: [" +IDmessage+"-"+mProducteur.identification()+" en "+this.nbExemplaire+" exemplaires"+"] | Producteur: " + mProducteur.identification()+">>>";
	}
	
	public String toStringSimple() {
		
		return "<<<"+IDmessage+"-"+mProducteur.identification()+">>>";
	}
	
	public int getNbExemplare(){
		return nbExemplaire;
	}
	
	public void setNbExemplare(int nbExemplaire){
		this.nbExemplaire = nbExemplaire;
	}
	
	public void sMessageSuspend() throws InterruptedException{
		this.sMessage.P();
	}
	
	public void sMessageWakeup(){
		this.sMessage.V();
	}
	
	public Producteur getProducteurMessage(){
		return this.mProducteur;
	}
	
	
}
