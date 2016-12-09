package jus.poc.prodcons.v3;

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
	
	
	private int tpsAlea;
	
	//Constructeur de Consommateur:
		//buffer
		//observateur
		//tempsMoyenCons -> moyenneTempsDeTraitement()
		//deviationTempsCons -> deviationTempsDeTraitement()
	protected Consommateur(ProdCons buffer, Observateur observateur, int tempsMoyenCons,
			int deviationTempsCons) throws ControlException {
		
		super(Acteur.typeConsommateur, observateur, tempsMoyenCons, deviationTempsCons);
		this.buffer = buffer;
		
	}

	@Override
	public int nombreDeMessages() {
		return this.nbMessage;
	}

	public int gettpsAlea() {  //Fonction de debug
		return tpsAlea;
	}
	
	@Override
	public void run() {
		
		try {
			Message msg;
			while(true) {
				
				tpsAlea = Aleatoire.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
				//Recupere un message sur le buffer
				try {
					msg = this.buffer.get(this);
				} catch (InterruptedException | ControlException e1) {
					break;
				}
				
				//Attente pour simuler le traitement, c'est à dire la consommation du message	
	
				Thread.sleep(tpsAlea);
				try {
					observateur.consommationMessage(this, msg, tpsAlea);
				} catch (ControlException e) {
					e.printStackTrace();
				}
	
				//Fonction pas utilisé mais qui peut etre utile (savoir combien de message a lu un consommateur)
				this.nbMessage = this.nbMessage +1;
			}
		} catch(InterruptedException e) {System.out.println("Thread Interrupted!");}
		
	}
	

}
