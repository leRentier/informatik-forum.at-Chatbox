package informatikforum.chatbox.tcp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.codec.binary.Base64;

import android.util.Log;
import informatikforum.chatbox.business.Informant;
import informatikforum.chatbox.dao.SmileyData;
import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.entity.Smiley;
import informatikforum.chatbox.gui.gif.AnimatedGifDrawable;

public class TCPWorkerThread extends Thread{
	
	private static final String TAG = "TCPWorkerThread";
	private String[] msg;
	private CommonData cd;
	private Informant inf;

	public TCPWorkerThread(String[] strings){
		this.msg = strings;
		cd = CommonData.getInstance();
		inf = Informant.getInstance();
	}
	
	
	@Override
	public void run(){
		if(msg.length == 1 && msg[0].equals(TCPCommand.REGISTEROK.toString())){
			Log.d(TAG, "Message from server is a REGISTEROK!");
			cd.setRegistered(true);
			cd.getStbs().release();
			
		}
		else if(msg.length == 1 && msg[0].equals(TCPCommand.BEAT.toString())){
			Log.d(TAG, "Message from server is BEAT!");
			cd.setLastTimeReceiving(System.currentTimeMillis());
			new TCPHeartbeatReceiverThread().start();
		}
		else if(msg.length == 2 && msg[0].equals(TCPCommand.NOTIFY.toString())){
			Log.d(TAG, "Message from server is NOTIFY!");
			
			retrieveMessagesFromSerializedList(msg[1]);
		}
		else{
			Log.d(TAG, "Message from server is unknown: " + msg);
		}
	}
	
	private void retrieveMessagesFromSerializedList(String serializedList){
		try {
			
			// Get ArrayList with messages.
			ArrayList<Message> messages = (ArrayList<Message>) new ObjectInputStream(new ByteArrayInputStream(Base64.decodeBase64(serializedList.getBytes()))).readObject();
			Collections.sort(messages);
			
			HashSet<Integer> smileyIds = new HashSet<Integer>();		
			Log.d(TAG, "There was no error before.");
			ArrayList<Message> newMessages = new ArrayList<Message>();
	
			Log.d(TAG, "Get only the newest messages from all found messages. (With id > " + cd.getLastId() + ").");
			
			for(Message m: messages){
				if(m.getId() > cd.getLastId()){
					cd.setLastId(m.getId());

					// Add smileys to buffering list..
					for(Smiley s : m.getSmileys().values()){
						if(!informatikforum.chatbox.business.CommonData.getInstance().getBufferedGifs().containsKey(SmileyData.getInstance().getFileIdForName(s.getName()))){
							smileyIds.add(SmileyData.getInstance().getFileIdForName(s.getName()));
						}
					}
					
					newMessages.add(m);
				}
			}
			
		
			Log.d(TAG, "Did we receive new messages?");
			if(newMessages != null && newMessages.size() > 0){
				Log.d(TAG, "We received new messages..");
				
				
				for(final Integer i : smileyIds){
					new Thread( new Runnable(){
						private static final String TAG = "AnonymousGifBufferer";
						@Override
						public void run() {
							
							Log.d(TAG, "Retrieving animated gif and put it inside gifBuffer in CommonData.");
							informatikforum.chatbox.business.CommonData.getInstance().getBufferedGifs().put(i, new AnimatedGifDrawable(informatikforum.chatbox.business.CommonData.getInstance().getContext().getResources().openRawResource(i)));
						
							Log.d(TAG, "Notify waiting threads about the newly buffered gif.");
							synchronized(informatikforum.chatbox.business.CommonData.getInstance().getGifLock()){	
								informatikforum.chatbox.business.CommonData.getInstance().getGifLock().notifyAll();
							}
							
						}
						
					}).start();
				}
			
				Log.d(TAG, "Add new messages to the messages stored in CommonData. There are " + newMessages.size() + " new messages.");
				informatikforum.chatbox.business.CommonData.getInstance().addMessages(newMessages);
				
				Log.d(TAG, "Tell the Informant to notify all clients, that we have a message update.");
				inf.clientNotifyMessageUpdate();
			}
			
			
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}	
	}
}
