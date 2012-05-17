package informatikforum.chatbox;

import java.util.concurrent.Semaphore;


public class BinarySemaphore extends Semaphore{

	private static final long serialVersionUID = 4314818204229046995L;

	
	public BinarySemaphore(int permits) {
		super(checkPermits(permits));
	}
	
	public BinarySemaphore(int permits, boolean fair) {
		super(checkPermits(permits), fair);
	}
	
	private static int checkPermits(int permits) {
		if(permits == 0 || permits == 1){
			return permits;
		}
		else{
			throw new IllegalArgumentException("Permits must be 0 or 1.");
		}
	}
	
	public void release(){
		if(this.availablePermits() < 1){
			super.release();
		}
	}
}

