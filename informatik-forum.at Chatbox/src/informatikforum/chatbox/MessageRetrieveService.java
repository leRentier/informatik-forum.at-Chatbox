package informatikforum.chatbox;

import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.tcp.TCPCommunicationThread;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class MessageRetrieveService extends Service implements ServerCallback{
 
	private boolean running = false;
	String errorMessage;
	private AsyncTask<Void, ArrayList<Message>, Void> rmt;
	private static final String TAG = "MessageRetrieveService";
	private Informant informant;
	
	/**
	 * Never used.
	 * Will only return null.
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	

	@Override
	public void onStart(Intent intent, int startId) {
		
		// Checking, if already running. If yes, return.
		Log.d(TAG, "onStart() called.. checking whether this happened before..");
		if(running){
			Log.d(TAG, "onStart() was called before.. returning.");
			return;
		}
		
		// If not already running, start new RetrieveMessageTask.
		Log.d(TAG, "onStart() was not called before. Getting the Informant-instance.");
		informant = Informant.getInstance();
		
		// Register this instance as server to informant
		informant.registerServerCallback(this);

		if(rmt == null || rmt.getStatus() == AsyncTask.Status.FINISHED){
			new TCPCommunicationThread().start();
		}
		
		Log.d(TAG, "Mark, that onStart() was called now.");
		running = true;
	} 

	@Override
	public void serverStopService() {
		
		if(!this.serverIsRunning()){
			return;
		}

		// Unregister from informant
		informant.unRegisterServerCallback(this);
		
		// Cancel the async-task
		if(!rmt.isCancelled()){
			rmt.cancel(true);
		}
		
		// Set flag to false.
		running= false;
		
		// Finally stop self.
		stopSelf();
	}


	@Override
	public boolean serverIsRunning() {
		return running;
	}


	@Override
	public void serverRaiseNormalError(Exception e) {
		// TODO Auto-generated method stub
		
e.printStackTrace();
		
	}


}
