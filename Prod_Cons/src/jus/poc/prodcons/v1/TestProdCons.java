package jus.poc.prodcons.v1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.Tampon;

public class TestProdCons extends Simulateur{

	
 	 static int nbProd;
 	 static int nbCons;
 	private static int tailleBuffer;
 	private static int tempsMoyenProd;
 	private static int deviationTempsProd;
 	private static int tempsMoyenCons;
 	private static int deviationTempsCons;
 	private static int nbMoyenProd;
 	private static int deviationNbProd;
 	private static int condTerminaison;
 	
 	
 	
	public TestProdCons(Observateur observateur) {
		super(observateur);	
	}

	@Override
	protected void run() throws Exception {
		
		init("src/jus/poc/prodcons/options/options.xml");
		condTerminaison = nbProd;
		ProdCons buffer = new ProdCons(tailleBuffer);
		
		Consommateur[] tabCons = new Consommateur[nbCons];
	 	Producteur[] tabProd = new Producteur[nbProd];
		
		//CREATION DES PRODUCTEURS et CONSOMMATEURS + lancement des threads
		for(int i=0; i < nbProd; i++) {
			tabProd[i] = new Producteur(buffer, observateur, tempsMoyenProd, deviationTempsProd, nbMoyenProd, deviationNbProd);
		}
		for(int i=0; i < nbCons; i++) {
			tabCons[i] = new Consommateur(buffer, observateur, tempsMoyenCons, deviationTempsCons);
		}
		for(int i=0; i < nbProd; i++) {
			tabProd[i].start();
		}
		for(int i=0; i < nbCons; i++) {
			tabCons[i].start();
		}
		
		for(int i=0; i < nbProd; i++) {
			tabProd[i].join();
		}
		while(buffer.enAttente() > 0) {
			System.out.println("NOMBRE DE MESSAGE RESTANT DANS LE BUFFER: "+buffer.enAttente());
		}
		Thread.sleep(3000);
		System.out.println("NOMBRE DE MESSAGE RESTANT DANS LE BUFFER: "+buffer.enAttente());
		for(int i=0; i < nbCons; i++) {
			tabCons[i].interrupt();
			System.out.println("terminaison de " + i);
		}
		
	}
	
	public static void main(String[] args){
		 new TestProdCons(new Observateur()).start();		
	 }
	 
	public static void init(String file) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		Properties properties = new Properties();
		
		properties.loadFromXML(new FileInputStream(file));
		nbProd = Integer.parseInt(properties.getProperty("nbProd"));
		nbCons = Integer.parseInt(properties.getProperty("nbCons"));
		tailleBuffer = Integer.parseInt(properties.getProperty("nbBuffer"));
		tempsMoyenProd = Integer.parseInt(properties.getProperty("tempsMoyenProduction"));
		deviationTempsProd = Integer.parseInt(properties.getProperty("deviationTempsMoyenProduction"));
		tempsMoyenCons = Integer.parseInt(properties.getProperty("tempsMoyenConsommation"));
		deviationTempsCons = Integer.parseInt(properties.getProperty("deviationTempsMoyenConsommation"));
		nbMoyenProd = Integer.parseInt(properties.getProperty("nombreMoyenDeProduction"));
		deviationNbProd = Integer.parseInt(properties.getProperty("deviationNombreMoyenDeProduction"));

	}
	
	public static void terminaison() {
		condTerminaison--;
	}
	

}

