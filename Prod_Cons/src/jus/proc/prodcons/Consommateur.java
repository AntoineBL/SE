package jus.proc.prodcons;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur{
	
	//Buffer utilisé par le consommateur
	private ProdCons buffer;
	
	//Nombre de message lus par le consommateur
	private int nbMessage = 0;
	
	
	protected Consommateur(ProdCons buffer, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement) throws ControlException {
		
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;
		
	}

	@Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub
		return this.nbMessage;
	}

	@Override
	public void run() {
		
		Message msg;
		int tpsAlea;
		while(true) {
			
			
			// Recupere un message sur le buffer
			try {
				msg = this.buffer.get(this);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//ACO: remplacer les variable int par les fonctions
			tpsAlea = Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement);
			
			//Attente pour simuler le traitement, c'est à dire la consommation du message	
			try {
				Thread.sleep(tpsAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.nbMessage = this.nbMessage +1;
		}
		
	}
	


}
