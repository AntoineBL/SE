package jus.proc.prodcons;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur{
	
	
	protected Consommateur(int type, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement) throws ControlException {
		super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
	}

	@Override
	public int deviationTempsDeTraitement() {
		// TODO Auto-generated method stub
		return deviationTempsDeTraitement;
	}

	@Override
	public int identification() {
		return 0;
	}

	@Override
	public int moyenneTempsDeTraitement() {
		// TODO Auto-generated method stub
		return moyenneTempsDeTraitement;
	}

	@Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	


}
