package informatikforum.chatbox.tcp;

import informatikforum.chatbox.Informant;

import java.io.IOException;
import java.util.Scanner;

import android.util.Log;

public class TCPListenerThread extends Thread{
	
	private static final String EXCEPTION_OPENING_INPUTSTREAM = "Couldn't open inputstream from socket.";
	private static final String TAG = "TCPListenerThread";
	private CommonData cd;
	private Informant inf;
	
	public TCPListenerThread(){
		cd = CommonData.getInstance();
		inf = Informant.getInstance();
	}
	
	@Override public void run(){
		
		// Register as running Thread..
		inf.addThread(this);
		
		Scanner sc;

		
		try {
			// Try to create a Scanner from InputStream.
			sc = new Scanner(cd.getSocket().getInputStream());
			
			// Main loop
int loopin=1;
			while(sc.hasNext()){
				Log.d(TAG, "New message from server! Starting worker..");
				new TCPWorkerThread(sc.next().split("_")).start();
System.err.println("Loopin=" + loopin);
loopin++;
			}
			
			Log.d(TAG, "Scanner has nothing more!");
		}
		
		// If creating Scanner fails with IOException,
		catch (Exception e) {
			
			// First raise message..
			inf.serverRaiseNormalError(new TCPException( EXCEPTION_OPENING_INPUTSTREAM, e));
			
			// Unregister..
			inf.removeThread(this);
			
			// than redo TCP-Stuff (as the Socket is probably faulty).
			new TCPCommunicationThread().start();
			
			// ..and quit.
			return;
		}
		

		
		// Finally unregister
		inf.removeThread(this);
	}
}
