package informatikforum.chatbox.tcp;

import java.net.Socket;

import informatikforum.chatbox.Informant;

public class TCPCommunicationThread extends Thread{
	
	private static final long REGISTER_TIMEOUT = 60000;
	private static final String EXCEPTION_COULDNT_CREATE_SOCKET = "Couldn't create Socket.";
	private CommonData cd;
	private Informant inf;
	
	private final String EXCEPTION_COULDNT_REGISTER = "Couldn't register to server. Retrying..";


	public TCPCommunicationThread(){
		this.cd = CommonData.getInstance();
		this.inf = Informant.getInstance();
	}
	
	private void undoTCPComponent(){
		
		cd.setRegistered(false);
		
		try {
			cd.getSocket().close();
		} catch (Exception e) {
			// Ignore
		}
		
		inf.interruptThreadByClass(TCPCommunicationThread.class);
		inf.interruptThreadByClass(TCPListenerThread.class);
		inf.interruptThreadByClass(TCPHeartbeatReceiverThread.class);
		inf.interruptThreadByClass(TCPHeartbeatSenderThread.class);
		inf.interruptThreadByClass(TCPWorkerThread.class);

		cd.setSocket(null);
	}

	
	@Override
	public void run(){
		
		boolean socketCreationSucceeded = false;
		
		// First, undo all TCP related stuff..
		undoTCPComponent();

		// Register this Thread as running.
		inf.addThread(this);
		
		// Create a socket. This is mandatory, so continue, till
		// one is created.
		while(!socketCreationSucceeded && !isInterrupted()){
			
			// Try to create a Socket and set variable to true.
			try {
				cd.setSocket(new Socket(cd.getServerHost(), cd.getServerPort()));
				socketCreationSucceeded = true;
			} catch (Exception e) {
				
				// Raise warning in service.
				inf.serverRaiseNormalError(new TCPException(EXCEPTION_COULDNT_CREATE_SOCKET, e));
				
				// Sleep a certain time (same time as register timeout
				// for the sake of ease).
				try {
					Thread.sleep(REGISTER_TIMEOUT);
					continue;
				}
				
				// If Thread is interrupted during this time,
				// unregister Thread and return.
				catch (InterruptedException e1) {
					inf.removeThread(this);
					return;
				}
			}
		}
		
		// Start new Listener-thread.
		new TCPListenerThread().start();
		
		// We must be registered to continue, therefore this step
		// is necessary.
		while(!cd.isRegistered() && !isInterrupted()){
			
			// Try to send register-command to server..
			try {
System.out.println("SENDING REGISTER");
				cd.getStbs().setInterestedInRelease(true);
				TCPSender.getInstance().sendMessageToServer(TCPCommand.REGISTER);
			}
			
			// But if it fails, raise error message and redo TCP-stuff.
			catch (TCPException e) {
				inf.serverRaiseNormalError(new TCPException(EXCEPTION_COULDNT_REGISTER, e));
				
				// Sleep a certain timeout.
				try {
					Thread.sleep(REGISTER_TIMEOUT);
				}
				
				// If Thread is interrupted during this time,
				// unregister Thread and return.
				catch (InterruptedException e1) {
					inf.removeThread(this);
					return;
				}
				
				// Unregister yourself..
				inf.removeThread(this);
				
				// Start new Communication-thread..
				new TCPCommunicationThread().start();
				
				// and quit.
				return;
			}
			
			
			// Acquire semaphore.
			try {
System.out.println("ACQUIRUNG SEMAPHORE..");
				cd.getStbs().acquire();
cd.getStbs().setInterestedInRelease(false);
System.out.println("SEMAPHORE ACQUIRED..");
			} catch (InterruptedException e) {
System.out.println("INTERRUPTED EXCEPTION!");				
				// If Thread is interrupted during this time,
				// unregister Thread and return.
				inf.removeThread(this);
				return;
			}
			
			
			// If we aren't registered after acquiring the semaphore
			// raise error, wait some time and try everything again.
			if(!cd.isRegistered()){
System.out.println("WE ARE NOT REGISTERED!");
				inf.serverRaiseNormalError(new TCPException(EXCEPTION_COULDNT_REGISTER));
				
				// Sleep for a certain time..
				try {
					Thread.sleep(REGISTER_TIMEOUT);
					continue;
				}
				
				// If Thread is interrupted during this time,
				// unregister Thread and return.
				catch (InterruptedException e1) {
					inf.removeThread(this);
					return;
				}
			}
		}
System.out.println("DOWN THERE");		
		// Registering succeeded.
		// Now we can start the Heartbeat-Thread.
		new TCPHeartbeatSenderThread().start();
		new TCPHeartbeatReceiverThread().start();
		
		// We are finished. Unregister and quit.
		inf.removeThread(this);
	}
}
