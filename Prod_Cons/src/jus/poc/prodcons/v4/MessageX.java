package jus.poc.prodcons.v4;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message{

	// Producteur du message
	private Producteur mProducteur;	
	
	// ID du message
	private int IDmessage;
	
	// Nombre d'exemplaires du message
	private int nbExemplaire;
	
	public MessageX(Producteur producteur, int IDmessage, int nbEexemplaire, int deviationNbExemplaire) {
		
		this.mProducteur = producteur;
		this.IDmessage = IDmessage;
		this.nbExemplaire = nbExemplaire;
		this.nbExemplaire = Aleatoire.valeur(nbEexemplaire, deviationNbExemplaire);
		
	}
	
	public String toString() {
		
		return "<<<Message NO: [" +IDmessage+"-"+mProducteur.identification()+"] | nbExemplaire: " + this.nbExemplaire+">>>";
	}
	
	public String toStringSimple() {
		
		return "<<<"+IDmessage+"-"+mProducteur.identification()+">>>";
	}
	
	public int getnbExemplaire() {
		return nbExemplaire;
	}
}
