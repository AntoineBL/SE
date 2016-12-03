package jus.proc.prodcons;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur{

	//Buffer utilisé par le producteur
	private ProdCons buffer;
	
	//Nombre de message du producteur
	private int nbMessage = 0;
	
	protected Producteur(ProdCons buffer, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int moyenneNbMessage, int deviationNbMessage) throws ControlException {
		
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;
		this.nbMessage = Aleatoire.valeur(moyenneNbMessage, deviationNbMessage);
		
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
			tpsAlea = Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement);
			message = new MessageX(this,IDmessage);
			
			//Attente pour simuler le traitement, c'est à dire la production du message
			try {
				Thread.sleep(tpsAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Dépose un message sur le buffer
			try {  // ACO: essayer plusieurs catchs
				this.buffer.put(this,message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.nbMessage = this.nbMessage -1;
			IDmessage++;
			
		}
		this.stop();
	}
	

}
