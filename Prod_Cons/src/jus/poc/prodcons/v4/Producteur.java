package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur{

	//Buffer utilis� par le producteur
	private ProdCons buffer;
	private Semaphore sProducteur;
	
	//Nombre de message du producteur
	private int nbMessage = 0;
	
	private int nbExemplaire;
	private int tpsAlea;
	
	//Constructeur de Producteur:
	//buffer
	//observateur
	//tempsMoyenProd -> moyenneTempsDeTraitement()
	//deviationTempsProd -> deviationTempsDeTraitement()
	//nbMoyenProd
	//deviationTempsProd
	protected Producteur(ProdCons buffer, Observateur observateur, int tempsMoyenProd,
			int deviationTempsProd, int nbMoyenProd, int deviationNbProd, int  nbExemplaire) throws ControlException {
		
		super(Acteur.typeProducteur, observateur, tempsMoyenProd, deviationTempsProd);
		this.buffer = buffer;
		this.nbMessage = Aleatoire.valeur(nbMoyenProd, deviationNbProd);
		this.nbExemplaire = nbExemplaire;
		this.sProducteur = new Semaphore(1);
		
		
	}

	@Override
	public int nombreDeMessages() {

		return this.nbMessage;
	}
	
	public void run() {
		
		MessageX message;
		int IDmessage = 1;
		while (nombreDeMessages() > 0) {

			tpsAlea = Aleatoire.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
			message = new MessageX(this,IDmessage, nbExemplaire);
			
			//Attente pour simuler le traitement, c'est � dire la production du message
			try {
				Thread.sleep(tpsAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// D�pose un message sur le buffer
			try { 
				this.buffer.put(this,message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Methode pour l'observateur
			try {
				observateur.productionMessage(this, message, tpsAlea);
			} catch (ControlException e1) {
				e1.printStackTrace();
			}
			
			this.nbMessage = this.nbMessage -1;
			IDmessage++;
			
		}
	}
	
	public void sProducteurSuspend() throws InterruptedException{
		this.sProducteur.P();
	}
	
	public void sProducteurWakeup(){
		this.sProducteur.V();
	}
	

}
