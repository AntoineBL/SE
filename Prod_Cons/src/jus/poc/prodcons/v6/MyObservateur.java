package jus.poc.prodcons.v6;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class MyObservateur {
	
	private boolean coherent;
	
	public MyObservateur() {
		this.coherent = false;
	}
	
	boolean coherent(){
		return this.coherent;
	}

	public synchronized void consommationMessage(_Consommateur c, Message m, int tempsDeTraitement) throws ControlException{
		if(!(c!=null && m!=null && tempsDeTraitement>0)){
			this.coherent = false;
			throw new ControlException(getClass(), "consomation message");
		}
	}

	public synchronized void depotMessage(_Producteur p, Message m) throws ControlException {
		if(!(p!=null && m!=null)){
			this.coherent = false;
			throw new ControlException(getClass(), "depot message");
		}
	}
	
	public void init(int nbproducteurs, int nbconsommateurs, int nbBuffers) throws ControlException{
		if(!(nbproducteurs>0 && nbconsommateurs>0 && nbBuffers>0)){
			throw new ControlException(getClass(), "erreur init");
		}
	}
	
	public synchronized void newConsommateur(_Consommateur c) throws ControlException{
		 if(!(c!=null)){
			 this.coherent = false;
			 throw new ControlException(getClass(), "erreur new consomateur");
		 }
	 }
	 
	public synchronized void newProducteur(_Producteur p) throws ControlException{
		 if(!(p!=null)){
			 this.coherent = false;
			 throw new ControlException(getClass(), "erreur new producteur");
		 }
	 }
	 
	public synchronized void productionMessage(_Producteur p, Message m, int tempsDeTraitement) throws ControlException{
		 if(!(p!=null && m!=null && tempsDeTraitement>0)){
			 this.coherent = false;
			 throw new ControlException(getClass(), "erreur production message");
		 }
	 }
	 
	public synchronized void retraitMessage(_Consommateur consommateur, Message m) throws ControlException{
		 if(!(consommateur!=null && m!=null)){
			 this.coherent = false;
			 throw new ControlException(getClass(), "erreur retrit message");
		 }
	 }
	
}