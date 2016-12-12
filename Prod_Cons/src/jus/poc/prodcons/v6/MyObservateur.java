package jus.poc.prodcons.v6;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class MyObservateur {
	
	private boolean coherent;
	private Semaphore mutexObservateur;
	
	public MyObservateur() {
		this.coherent = false;
		mutexObservateur = new Semaphore(1);
	}
	
	boolean coherent(){
		return this.coherent;
	}

	public void consommationMessage(_Consommateur c, Message m, int tempsDeTraitement) throws ControlException{
		try {
			mutexObservateur.P();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!(c!=null && m!=null && tempsDeTraitement>0)){
			this.coherent = false;
			throw new ControlException(getClass(), "consomation message");
		}
		mutexObservateur.V();
	}

	public void depotMessage(_Producteur p, Message m) throws ControlException {
		try {
			mutexObservateur.P();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!(p!=null && m!=null)){
			this.coherent = false;
			throw new ControlException(getClass(), "depot message");
		}
		mutexObservateur.V();
	}
	
	public void init(int nbproducteurs, int nbconsommateurs, int nbBuffers) throws ControlException{
		
		if(!(nbproducteurs>0 && nbconsommateurs>0 && nbBuffers>0)){
			throw new ControlException(getClass(), "erreur init");
		}
	}
	
	public void newConsommateur(_Consommateur c) throws ControlException{
		try {
			mutexObservateur.P();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(!(c!=null)){
			 this.coherent = false;
			 throw new ControlException(getClass(), "erreur new consomateur");
		 }
		mutexObservateur.V();
	 }
	 
	public void newProducteur(_Producteur p) throws ControlException{
		try {
			mutexObservateur.P();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(!(p!=null)){
			 this.coherent = false;
			 throw new ControlException(getClass(), "erreur new producteur");
		 }
		mutexObservateur.V();
	 }
	 
	public void productionMessage(_Producteur p, Message m, int tempsDeTraitement) throws ControlException{
		try {
			mutexObservateur.P();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(!(p!=null && m!=null && tempsDeTraitement>0)){
			 this.coherent = false;
			 throw new ControlException(getClass(), "erreur production message");
		 }
		mutexObservateur.V();
	 }
	 
	public void retraitMessage(_Consommateur consommateur, Message m) throws ControlException{
		try {
			mutexObservateur.P();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!(consommateur!=null && m!=null)){
			 this.coherent = false;
			 throw new ControlException(getClass(), "erreur retrit message");
		 }
		mutexObservateur.V();
	 }
	
}