package informatikforum.chatbox.business;


import informatikforum.chatbox.entity.Message;

import java.net.URLDecoder;
import java.util.ArrayList;

import android.content.Context;

public class BusinessLogic {

	private static BusinessLogic instance = null;
	
	private Context context;

	
	private BusinessLogic(){

	}
	
	
	public static BusinessLogic getInstance(){
		if(instance == null){
			instance = new BusinessLogic();
		}

		return instance;
	}
	
	public void updateContext(Context context){
		this.context = context;
	}
	
	
	/**
	 * Returns the String for a given id using the
	 * saved Context-instance.
	 * 
	 * Doesn't perform any checks (like if context == null).
	 *
	 *@param id The id of the String to lookup.
	 *@return The corresponding String.
	 *@throws NullPointerException, if the current context is not set.
	 *May also throw other RuntimeExceptions by the AndroidAPI if the
	 *given id can't be found.
	 */
	public String getString(int id) {
		return URLDecoder.decode(this.context.getString(id));
	}
		

	public synchronized ArrayList<Message> retrieveMessages() throws BusinessLogicException{
		return ChatboxWrapperBusinessLogic.getInstance().retrieveMessages();
	}

	
	public synchronized void postMessage(Message m) throws BusinessLogicException{
		ChatboxWrapperBusinessLogic.getInstance().postMessage(m);
	}
	
	public synchronized void postMessage(String encodedMessage) throws BusinessLogicException{
		ChatboxWrapperBusinessLogic.getInstance().postMessage(encodedMessage);
	}
	
	public synchronized boolean checkUserdata() throws BusinessLogicException{
		return ChatboxWrapperBusinessLogic.getInstance().checkUserdata();
	}
	
	
	private void suicide(){
		// TODO
		// Kill myself :).
	}


	private String getUsername(){
		return "USERNAME";
	}
	
	
	private String getPassword(){
		return "PASSWORD";
	}
}