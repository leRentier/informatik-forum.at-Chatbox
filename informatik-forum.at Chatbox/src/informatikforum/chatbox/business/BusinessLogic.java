package informatikforum.chatbox.business;


import informatikforum.chatbox.entity.Message;

import java.net.URLDecoder;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class BusinessLogic {

	private static BusinessLogic instance = null;
	
	private Context context;

	private Handler linkHandler;

	private SharedPreferences preferences;
	
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
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
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


	public String getUsername() {
		return preferences.getString("settings_username", "");
	}
	
	
	public String getPassword(){
		return preferences.getString("settings_password", "");
	}

	public String[] getIgnoreList(){
		return preferences.getString("settings_ignorelist", "").split(",");
	}

	public void openLink(String url) {
		android.os.Message message = android.os.Message.obtain();
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		message.setData(bundle);
		linkHandler.dispatchMessage(message);
	}


	public void registerLinkHandler(Handler linkHandler) {
		this.linkHandler = linkHandler;
	}


	public boolean isConfigured() {
		return preferences.getBoolean("configured", false);
	}
}
