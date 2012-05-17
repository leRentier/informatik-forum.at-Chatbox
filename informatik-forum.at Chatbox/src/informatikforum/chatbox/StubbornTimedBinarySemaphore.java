package informatikforum.chatbox;

public class StubbornTimedBinarySemaphore extends TimedBinarySemaphore{

	private static final long serialVersionUID = 6641644594981878873L;
	
	private boolean interestedInRelease;

	
	public StubbornTimedBinarySemaphore(long maxAcquireTime, int permits, boolean fair) {
		super(maxAcquireTime, permits, fair);
		// TODO Auto-generated constructor stub
	}
	
	public StubbornTimedBinarySemaphore(long maxAcquireTime, int permits) {
		super(maxAcquireTime, permits);
		// TODO Auto-generated constructor stub
	}


	public void setInterestedInRelease(boolean interestedInRelease){
		this.interestedInRelease = interestedInRelease;
	}
	
	@Override
	public void release(){
		if(this.interestedInRelease){
			super.release();
		}
	}
}
