package jus.poc.prodcons.v1;

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
		
		ProdCons buffer = new ProdCons(5);
		
		Producteur p1 = new Producteur(buffer, observateur, 1000, 999, 5, 1);
		Producteur p2 = new Producteur(buffer, observateur, 1000, 999, 5, 1);
		Producteur p3 = new Producteur(buffer, observateur, 1000, 999, 5, 1);
		Consommateur c1 = new Consommateur(buffer, observateur, 2000, 500);
		Consommateur c2 = new Consommateur(buffer, observateur, 2000, 500);
		Consommateur c3 = new Consommateur(buffer, observateur, 2000, 500);
		p1.start(); p2.start(); p3.start(); c1.start(); c2.start(); c3.start();
		

	}
	
	 public static void main(String[] args){new TestProdCons(new Observateur()).start();


	 	
	 	
	 }
}

//public static void init(String file) {
//	Properties properties = new Properties();
//	
//	properties.loadFromXML(new FileInputStream(file));
//	nb_cons = Integer.parseInt(properties.getProperty("CHAMP DANS LE XML"));
//	...
//	...
//}