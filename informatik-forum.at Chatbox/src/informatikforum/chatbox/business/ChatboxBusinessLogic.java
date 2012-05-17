package informatikforum.chatbox.business;

import informatikforum.chatbox.CommonData;
import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.wrapper.ChatboxWrapper;
import informatikforum.chatbox.wrapper.WrapperException;

import java.util.ArrayList;

import android.util.Log;

public class ChatboxBusinessLogic {
	
	private static final String TAG = "ChatboxBusinessLogic";
	
	private ChatboxWrapper cbWrapper;

	private static ChatboxBusinessLogic instance = null;
	private String username = "unset";
	private String password = "unset";
	
	private final String EXCEPTION_COULDNT_INSTANTIATE_WRAPPER = "Couldn't instantiate a ChatboxWrapper.";
	private final String EXCEPTION_COULDNT_POST_MESSAGE = "Couldn't post the given Message.";
	private final String EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN = "Couldn't post the given Message despite being logged in.";
	private final String EXCEPTION_COULDNT_CHECK_LOGIN_STATUS = "Couldn't retrieve login-status from wrapper or relog.";
	private final String EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE = "Couldn't post message despite re-login.";
	private final String EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGGED_IN = "Couldn't retrieve messages despite being logged in.";
	private final String EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGIN_UPDATE = "Couldn't retrieve messages despite re-login.";
	private final String EXCEPTION_COULDNT_RETRIEVE_MESSAGE = "Couldn't retrieve messages.";
	
	
	
	// TODO Exception-handling.
	public synchronized ArrayList<Message> retrieveMessages() throws BusinessLogicException{
		ArrayList<Message> messages;
		
		try{
			Log.d(TAG, "Try retrieving messages..");
			messages = this.cbWrapper.retrieveMessages();
			Log.d(TAG, "Retrieving messages successfull.");
			return messages;
		}
		catch(WrapperException e){
			Log.w(TAG, "Retrieving failed because of a WrapperException: " + e.getMessage());
			try {
				Log.d(TAG, "Checking, if currently logged in before trying to fetch messages again..");
				if(this.cbWrapper.isLoggedIn()){
					Log.d(TAG, "Currently logged in. Can not fix this problem. Throwing new BusinessLogicException with message: " + EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGGED_IN);
					throw new BusinessLogicException(EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGGED_IN, e);
				}
				else{
					Log.d(TAG, "Currently not logged in. Trying to log in again..");
					this.cbWrapper.logIn(this.username, this.password);
					Log.d(TAG, "Successfully logged in again.");
				}
			} catch (Exception e1) {
				Log.w(TAG, "Exception while trying to determine the login-status: " + e1.getMessage() + ". Throwing new exception with message: " + EXCEPTION_COULDNT_CHECK_LOGIN_STATUS);
				throw new BusinessLogicException(EXCEPTION_COULDNT_CHECK_LOGIN_STATUS, e1);
			}
			
			try {
				Log.d(TAG, "Trying to retrieve message again..");
				messages = this.cbWrapper.retrieveMessages();
				Log.d(TAG, "Successfully retrieved messages..");
				return messages;
			} catch (Exception e1) {
				Log.w(TAG, "Retrieving messages failed again with exception: " + e1.getMessage() + ". Throwing new exception with message: " + EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGIN_UPDATE);
				throw new BusinessLogicException(EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGIN_UPDATE, e1);
			}
		}
		catch(Exception e){
			Log.w(TAG, "Retrieving messages failed because of an arbitrary Exception: " + e.getMessage());
			throw new BusinessLogicException(EXCEPTION_COULDNT_RETRIEVE_MESSAGE, e);
		}	
	}
	
	public static ChatboxBusinessLogic getInstance() throws BusinessLogicException{
		if(instance == null){
			instance = new ChatboxBusinessLogic();
		}
		
		return instance;
	}
	
	private ChatboxBusinessLogic() throws BusinessLogicException{
		try {
			this.cbWrapper = ChatboxWrapper.getInstance();
		} catch (WrapperException e) {
			throw new BusinessLogicException(EXCEPTION_COULDNT_INSTANTIATE_WRAPPER, e);
		}
	}
	
	
	public synchronized void postMessage(Message m) throws BusinessLogicException{
		try{
			Log.d(TAG, "Try posting message (without smileys and such): " + m.getMessage() + "..");
			this.cbWrapper.postMessage(m);
			Log.d(TAG, "Posting successfull.");
		}
		catch(WrapperException e){
			Log.w(TAG, "Posting failed because of a WrapperException: " + e.getMessage());
			try {
				Log.d(TAG, "Checking, if currently logged in before trying to repost message..");
				if(this.cbWrapper.isLoggedIn()){
					Log.d(TAG, "Currently logged in. Can not fix this problem. Throwing new BusinessLogicException with message: " + EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN);
					throw new BusinessLogicException(EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN, e);
				}
				else{
					Log.d(TAG, "Currently not logged in. Trying to log in again..");
					this.cbWrapper.logIn(this.username, this.password);

					Log.d(TAG, "Successfully logged in again.");
				}
			} catch (Exception e1) {
				Log.w(TAG, "Exception while trying to determine the login-status: " + e1.getMessage() + ". Throwing new exception with message: " + EXCEPTION_COULDNT_CHECK_LOGIN_STATUS);
				throw new BusinessLogicException(EXCEPTION_COULDNT_CHECK_LOGIN_STATUS, e1);
			}
			
			try {
				Log.d(TAG, "Trying to repost message..");
				this.cbWrapper.postMessage(m);
				Log.d(TAG, "Successfully reposted message..");
			} catch (Exception e1) {
				Log.w(TAG, "Reposting message failed with exception: " + e1.getMessage() + ". Throwing new exception with message: " + EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE);
				throw new BusinessLogicException(EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE, e1);
			}
		}
		catch(Exception e){
			Log.w(TAG, "Posting failed because of an arbitrary Exception: " + e.getMessage());
			throw new BusinessLogicException(EXCEPTION_COULDNT_POST_MESSAGE, e);
		}	
	}
	
	public synchronized void postMessage(String encodedMessage) throws BusinessLogicException{
		try{
			Log.d(TAG, "Try posting message: " + encodedMessage + "..");
			this.cbWrapper.postMessage(encodedMessage);
			Log.d(TAG, "Posting successfull.");
		}
		catch(WrapperException e){
			Log.w(TAG, "Posting failed because of a WrapperException: " + e.getMessage());
			try {
				Log.d(TAG, "Checking, if currently logged in before trying to repost message..");
				if(this.cbWrapper.isLoggedIn()){
					Log.d(TAG, "Currently logged in. Can not fix this problem. Throwing new BusinessLogicException with message: " + EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN);
					throw new BusinessLogicException(EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN, e);
				}
				else{
					Log.d(TAG, "Currently not logged in. Trying to log in again..");
					this.cbWrapper.logIn(this.username, this.password);
					Log.d(TAG, "Successfully logged in again.");
				}
			} catch (Exception e1) {
				Log.w(TAG, "Exception while trying to determine the login-status: " + e1.getMessage() + ". Throwing new exception with message: " + EXCEPTION_COULDNT_CHECK_LOGIN_STATUS);
				throw new BusinessLogicException(EXCEPTION_COULDNT_CHECK_LOGIN_STATUS, e1);
			}
			
			try {
				Log.d(TAG, "Trying to repost message..");
				this.cbWrapper.postMessage(encodedMessage);
				Log.d(TAG, "Successfully reposted message..");
			} catch (Exception e1) {
				Log.w(TAG, "Reposting message failed with exception: " + e1.getMessage() + ". Throwing new exception with message: " + EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE);
				throw new BusinessLogicException(EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE, e1);
			}
		}
		catch(Exception e){
			Log.w(TAG, "Posting failed because of an arbitrary Exception: " + e.getMessage());
			throw new BusinessLogicException(EXCEPTION_COULDNT_POST_MESSAGE, e);
		}
	}

	public void setUserdata(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
