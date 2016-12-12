package jus.poc.prodcons.v6;


public class Semaphore {

	private int nbThread;
	
	public Semaphore(int c){
		this.nbThread = c;
	}
	
	
	/**
	 * suspend
	 * @throws InterruptedException
	 */
    public synchronized void P() throws InterruptedException {
        this.nbThread--;
        if(this.nbThread < 0){
        	this.wait();
        }
    }

    /**
     * remet
     */
    public synchronized void V() {
        this.nbThread++;
        //if(this.nbThread <= 0){
        	this.notify();
        //}
    }
	
}
