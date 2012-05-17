package informatikforum.chatbox;

public class TimedBinarySemaphore extends BinarySemaphore{

	private static final long serialVersionUID = 7398088881080925558L;
	private long maxAcquireTime;

	
	public TimedBinarySemaphore(long maxAcquireTime, int permits, boolean fair) {
		super(permits, fair);
		this.maxAcquireTime = maxAcquireTime;
	}
	
	public TimedBinarySemaphore(long maxAcquireTime, int permits) {
		super(permits);
		this.maxAcquireTime = maxAcquireTime;
	}
	
	@Override
	public void acquire() throws InterruptedException{
		TimedReleaseThread trt = null;;
		
		if(this.availablePermits() == 0){
			trt = new TimedReleaseThread(maxAcquireTime);
			trt.start();
		}
		
		super.acquire();
		if(trt != null && trt.isAlive()){
			trt.interrupt();
		}
	}
	
	
	private class TimedReleaseThread extends Thread{
		
		private long time;

		public TimedReleaseThread(long time){
			this.time = time;
		}
		
		@Override
		public void run(){
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// Just return without release
				return;
			}
			
			release();
			
		}
		
	}

}
