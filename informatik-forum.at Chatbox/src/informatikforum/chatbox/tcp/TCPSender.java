package informatikforum.chatbox.tcp;

import java.io.PrintWriter;

import android.util.Log;

public class TCPSender {
	
	private static TCPSender instance = null;
	
	private static final String EXCEPTION_COULDNT_SEND_MESSAGE = "Couldn't send message to server.";

	private static final String TAG = "TCPSender";
	
	private CommonData cd;
	private PrintWriter out;
	
	
	private TCPSender(){
		out = null;
		cd = CommonData.getInstance();
	}
	
	public static TCPSender getInstance(){
		if(instance == null){
			instance = new TCPSender();
		}
		
		return instance;
	}

	public synchronized void sendMessageToServer(TCPCommand cmd) throws TCPException{
		
		try {
			out = new PrintWriter(cd.getSocket().getOutputStream(), true);
			
Log.d(TAG, "Sending command: " + cmd.toString());
			out.print(cmd.toString() + "\n");
			out.flush();
		} catch (Exception e) {
			throw new TCPException(EXCEPTION_COULDNT_SEND_MESSAGE);
		}
		

	}
}
