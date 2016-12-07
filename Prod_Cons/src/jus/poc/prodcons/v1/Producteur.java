package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur{

	//Buffer utilis� par le producteur
	private ProdCons buffer;
	
	//Nombre de message du producteur
	private int nbMessage = 0;
	
	//Constructeur de Producteur:
	//buffer
	//observateur
	//tempsMoyenProd -> moyenneTempsDeTraitement()
	//deviationTempsProd -> deviationTempsDeTraitement()
	//nbMoyenProd
	//deviationTempsProd
	protected Producteur(ProdCons buffer, Observateur observateur, int tempsMoyenProd,
			int deviationTempsProd, int nbMoyenProd, int deviationNbProd) throws ControlException {
		
		super(Acteur.typeProducteur, observateur, tempsMoyenProd, deviationTempsProd);
		this.buffer = buffer;
		this.nbMessage = Aleatoire.valeur(nbMoyenProd, deviationNbProd);
		
	}

	@Override
	public int nombreDeMessages() {

		return this.nbMessage;
	}
	
	public void run() {

		MessageX message;
		int IDmessage = 1;
		int tpsAlea;
		while (nombreDeMessages() > 0) {
			
			//ACO: remplacer les variable int par les fonctions
			tpsAlea = Aleatoire.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
			message = new MessageX(this,IDmessage);
			
			//Attente pour simuler le traitement, c'est � dire la production du message
			try {
				Thread.sleep(tpsAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// D�pose un message sur le buffer
			try {  // ACO: essayer plusieurs catchs
				this.buffer.put(this,message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.nbMessage = this.nbMessage -1;
			IDmessage++;
			
		}
		
	}
	

}
