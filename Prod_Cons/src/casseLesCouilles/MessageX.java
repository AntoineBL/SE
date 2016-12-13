package casseLesCouilles;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message{

	// Producteur du message
	private Producteur mProducteur;	
	
	// ID du message
	private int IDmessage;
	
	// Nombre d'exemplaire
	private int nbExemplaire;
	
	public MessageX(Producteur producteur, int IDmessage, int nbMoyenExemplaire, int deviationNbExemplaire) {
		
		this.mProducteur = producteur;
		this.IDmessage = IDmessage;
		this.nbExemplaire = Aleatoire.valeur(nbMoyenExemplaire, deviationNbExemplaire);
		
	}
	
	public String toString() {
		
		return "<<<Message NO: [" +IDmessage+"-"+mProducteur.identification()+"] | nbExemplaire: " + this.nbExemplaire+">>>";
	}
	
	public String toStringSimple() {
		
		return "<<<"+IDmessage+"-"+mProducteur.identification()+">>>";
	}
	
	public int getnbExemplaire() {
		
		return this.nbExemplaire;
	}

	public int getProducteurId() {
		// TODO Auto-generated method stub
		return mProducteur.identification();
	}
}
