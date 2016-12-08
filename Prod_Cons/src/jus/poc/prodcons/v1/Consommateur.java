package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur{
	
	//Buffer utilis� par le consommateur
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
			Message msg = null;
			while(true) {
				
				tpsAlea = Aleatoire.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
				//Recupere un message sur le buffer
				try {
					msg = this.buffer.get(this);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block 
					break;
				}
				
				//Attente pour simuler le traitement, c'est � dire la consommation du message
				System.err.println("Dur�e de consommation du message: "+((MessageX) msg).toStringSimple()+" par le consommateur: "+this.identification()+" = "+this.gettpsAlea()+" ms\n");
				
				Thread.sleep(tpsAlea);

				//Fonction pas utilis� mais qui peut etre utile (savoir combien de message a lu un consommateur)
				this.nbMessage = this.nbMessage +1;
			}
			
		} catch (InterruptedException e) {System.out.println("Thread Interrupted!");}
	}
	

	
}