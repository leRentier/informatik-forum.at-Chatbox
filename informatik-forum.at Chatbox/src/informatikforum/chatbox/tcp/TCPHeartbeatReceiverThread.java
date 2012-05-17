package informatikforum.chatbox.tcp;

import informatikforum.chatbox.Informant;

public class TCPHeartbeatReceiverThread extends Thread {
	
	private static final long BEAT_INTERVAL = 5000;
	private CommonData cd;
	private Informant inf;
	private static final String EXCEPTION_NO_BEAT_RECEIVED = "Did not receive a beat from the server.";
	
	public TCPHeartbeatReceiverThread(){
		cd = CommonData.getInstance();
		inf = Informant.getInstance();
	}
	
	
	@Override
	public void run(){
		
		// Register thread
		inf.addThread(this);

			try {
				Thread.sleep(BEAT_INTERVAL*2);
			} catch (InterruptedException e) {
				
				// Just quit.
				return;
			}

				// If we have a client, that hasn't sent a beat,
				// Remove it from the list, interrupt listener and exit.
				if(cd.getLastTimeReceiving() -( System.currentTimeMillis() - (BEAT_INTERVAL*2)) <= 0){
				
					// Warn service
					inf.serverRaiseNormalError(new TCPException(EXCEPTION_NO_BEAT_RECEIVED));
					
					// Unregister..
					inf.removeThread(this);
					
					// Restart TCP stuff..
					new TCPCommunicationThread().start();
					
					// ..and quit.
					return;

				}
			
		
		// Unregister thread
		inf.removeThread(this);
	}

}
