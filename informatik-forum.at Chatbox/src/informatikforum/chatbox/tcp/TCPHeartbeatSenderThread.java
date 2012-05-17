package informatikforum.chatbox.tcp;

import informatikforum.chatbox.Informant;

public class TCPHeartbeatSenderThread extends Thread {
	
	private static final long BEAT_INTERVAL = 5000;
	private static final String EXCEPTION_SEND_BEAT = "Couldn't send heartbeat to server.";

	private Informant inf;

	
	public TCPHeartbeatSenderThread(){
		inf = Informant.getInstance();
	}
	
	
	@Override
	public void run(){
		
		// Register thread
		inf.addThread(this);
		
		while(!isInterrupted()){
			
			// Sleep for a given time..
			try {
				Thread.sleep(BEAT_INTERVAL);
			} catch (InterruptedException e) {
				// Just return
				return;
			}
			
			// Send beat to server.
			try {
				TCPSender.getInstance().sendMessageToServer(TCPCommand.BEAT);
			} catch (TCPException e) {
				// If this failed, notify service.
				inf.serverRaiseNormalError(new TCPException(EXCEPTION_SEND_BEAT, e));
				
				// Unregister..
				inf.removeThread(this);
				
				// Restart TCP stuff..
				new TCPCommunicationThread().start();
				
				// ..and quit.
				return;
			}
		}
		
		// Unregister thread
		inf.removeThread(this);
	}

}
