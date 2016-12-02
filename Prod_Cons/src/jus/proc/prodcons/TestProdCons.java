package jus.proc.prodcons;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.Tampon;

public class TestProdCons extends Simulateur{

	
	
 	private static int nbActeur = 10;
 	private static Consommateur tab_Consommateur[];
 	private static Producteur tab_Producteur[];
	
	public TestProdCons(Observateur observateur) {
		super(observateur);	
	}

	@Override
	protected void run() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	 public static void main(String[] args){new TestProdCons(new Observateur()).start();

		int moyenneTempsDeTraitement = 50;
		int deviationTempsDeTraitement = 10;
	 	
	 	for(int i=0; i<nbActeur; i++){
			try {
				tab_Consommateur[i] = new Consommateur(i, new Observateur(), moyenneTempsDeTraitement, deviationTempsDeTraitement);
			} catch (ControlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 	
	 	for(int i=0; i<nbActeur; i++){
			try {
				tab_Producteur[i] = new Producteur(i, new Observateur(), moyenneTempsDeTraitement, deviationTempsDeTraitement);
			} catch (ControlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 }
}
