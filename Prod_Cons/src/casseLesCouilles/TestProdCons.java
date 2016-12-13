package casseLesCouilles;

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

	
	//Declaration des variables (XML)
 	private static int nbProd;
 	private static int nbCons;
 	private static int tailleBuffer;
 	private static int tempsMoyenProd;
 	private static int deviationTempsProd;
 	private static int tempsMoyenCons;
 	private static int deviationTempsCons;
 	private static int nbMoyenProd;
 	private static int deviationNbProd;
 	private static int nbMoyenExemplaire;
 	private static int deviationNbExemplaire;
 	
 	
 	
	public TestProdCons(Observateur observateur) {
		super(observateur);
	}

	@Override
	protected void run() throws Exception {
		
		System.out.println("\n\n------------------");
		System.out.println("DEBUT VERSION CASSE LES COUILLES");
		System.out.println("------------------");
		
		initXML("src/jus/poc/prodcons/options/options.xml");
		observateur.init(nbProd, nbCons, tailleBuffer);
		ProdCons buffer = new ProdCons(tailleBuffer, observateur);
		
		Consommateur[] tabCons = new Consommateur[nbCons];
	 	Producteur[] tabProd = new Producteur[nbProd];
		
		//CREATION DES PRODUCTEURS et CONSOMMATEURS + lancement des threads
		for(int i=0; i < nbProd; i++) {
			tabProd[i] = new Producteur(buffer, observateur, tempsMoyenProd, deviationTempsProd, nbMoyenProd, deviationNbProd, nbMoyenExemplaire, deviationNbExemplaire);
			observateur.newProducteur(tabProd[i]);
		}
		for(int i=0; i < nbCons; i++) {
			tabCons[i] = new Consommateur(buffer, observateur, tempsMoyenCons, deviationTempsCons);
			observateur.newConsommateur(tabCons[i]);
		}
		for(int i=0; i < nbProd; i++) {
			tabProd[i].start();
		}
		for(int i=0; i < nbCons; i++) {
			tabCons[i].start();
		}
		
		//Bloquer le code tant que tous les threads producteurs non pas fini
		for(int i=0; i < nbProd; i++) {
			tabProd[i].join();
		}
		
//		Bloquer le code tant que le buffer n'est pas vide
		while(buffer.enAttente() > 0) {
			//On attend
		}

		for(int i=0; i < nbCons; i++) {
			tabCons[i].interrupt();
		}
		for(int i=0; i < nbCons; i++) {
			tabCons[i].join();
		}

		
		System.out.println("\n\n------------------");
		System.out.println("FIN DU PROGRAMME");
		System.out.println("------------------");
	}
	
	public static void main(String[] args){
		 new TestProdCons(new Observateur()).start();		
	 }
	 
	public static void initXML(String file) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
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
		nbMoyenExemplaire = Integer.parseInt(properties.getProperty("nombreMoyenNbExemplaire"));
		deviationNbExemplaire = Integer.parseInt(properties.getProperty("deviationNombreMoyenNbExemplaire"));
	}
	
	

}

