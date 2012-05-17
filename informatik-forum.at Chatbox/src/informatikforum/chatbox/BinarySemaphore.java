package informatikforum.chatbox;

import informatikforum.chatbox.business.BusinessLogic;

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
			throw new IllegalArgumentException(BusinessLogic.getInstance().getString(R.string.EXCEPTION_WRONG_NUMBER_OF_PERMITS));
		}
	}
	
	@Override
	public void release(){
		if(this.availablePermits() < 1){
			super.release();
		}
	}
}

