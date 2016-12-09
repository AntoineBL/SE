package jus.poc.prodcons.v3;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message{

	// Producteur du message
	private Producteur mProducteur;	
	
	// ID du message
	private int IDmessage;
	
	public MessageX(Producteur producteur, int IDmessage) {
		
		this.mProducteur = producteur;
		this.IDmessage = IDmessage;
		
	}
	
	public String toString() {
		
		return "<<<Message NO: [" +IDmessage+"-"+mProducteur.identification()+"] | Producteur: " + mProducteur.identification()+">>>";
	}
	
	public String toStringSimple() {
		
		return "<<<"+IDmessage+"-"+mProducteur.identification()+">>>";
	}
}
