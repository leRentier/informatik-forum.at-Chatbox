package informatikforum.chatbox.business;

import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.wrapper.ChatboxWrapper;
import informatikforum.chatbox.wrapper.WrapperException;

import java.util.ArrayList;

import informatikforum.chatbox.R;
import android.util.Log;

public class ChatboxWrapperBusinessLogic {
	
	private static final String TAG = "ChatboxBusinessLogic";
	
	private ChatboxWrapper cbWrapper;
	private BusinessLogic bl;

	private static ChatboxWrapperBusinessLogic instance = null;
	private String username = "unset";
	private String password = "unset";
	

	public synchronized ArrayList<Message> retrieveMessages() throws BusinessLogicException{
		ArrayList<Message> messages;
		
		try{
			Log.d(TAG, "Try retrieving messages..");
			messages = this.cbWrapper.retrieveMessages();
			Log.d(TAG, "Retrieving messages successfull.");
			return messages;
		}
		catch(WrapperException e){
			Log.w(TAG, "Retrieving failed because of a BusinessLogicException: " + e.getMessage());
			try {
				Log.d(TAG, "Checking, if currently logged in before trying to fetch messages again..");
				if(this.cbWrapper.isLoggedIn()){
					Log.d(TAG, "Currently logged in. Can not fix this problem. Throwing new BusinessLogicException with message: " + bl.getString(R.string.EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGGED_IN));
					throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGGED_IN), e);

				}
				else{
					Log.d(TAG, "Currently not logged in. Trying to log in again..");
					this.cbWrapper.logIn(this.username, this.password);
					Log.d(TAG, "Successfully logged in again.");
				}
			} catch (Exception e1) {
				Log.w(TAG, "Exception while trying to determine the login-status: " + e1.getMessage() + ". Throwing new exception with message: " + bl.getString(R.string.EXCEPTION_COULDNT_CHECK_LOGIN_STATUS));
				throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_CHECK_LOGIN_STATUS), e1);
			}
			
			try {
				Log.d(TAG, "Trying to retrieve message again..");
				messages = this.cbWrapper.retrieveMessages();
				Log.d(TAG, "Successfully retrieved messages..");
				return messages;
			} catch (Exception e1) {
				Log.w(TAG, "Retrieving messages failed again with exception: " + e1.getMessage() + ". Throwing new exception with message: " + bl.getString(R.string.EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGIN_UPDATE));
				throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_RETRIEVE_MESSAGES_DESPITE_LOGIN_UPDATE), e1);
			}
		}
		catch(Exception e){
			Log.w(TAG, "Retrieving messages failed because of an arbitrary Exception: " + e.getMessage());
			throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_RETRIEVE_MESSAGE), e);
		}	
	}
	
	public static ChatboxWrapperBusinessLogic getInstance(){
		if(instance == null){
			instance = new ChatboxWrapperBusinessLogic();
		}
		
		return instance;
	}
	
	private ChatboxWrapperBusinessLogic(){
		this.cbWrapper = ChatboxWrapper.getInstance();
		this.bl = BusinessLogic.getInstance();
	}
	
	
	public synchronized void postMessage(Message m) throws BusinessLogicException{
		try{
			Log.d(TAG, "Try posting message (without smileys and such): " + m.getMessage() + "..");
			this.cbWrapper.postMessage(m);
			Log.d(TAG, "Posting successfull.");
		}
		catch(WrapperException e){
			Log.w(TAG, "Posting failed because of a BusinessLogicException: " + e.getMessage());
			try {
				Log.d(TAG, "Checking, if currently logged in before trying to repost message..");
				if(this.cbWrapper.isLoggedIn()){
					Log.d(TAG, "Currently logged in. Can not fix this problem. Throwing new BusinessLogicException with message: " + bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN));
					throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN), e);
				}
				else{
					Log.d(TAG, "Currently not logged in. Trying to log in again..");
					this.cbWrapper.logIn(this.username, this.password);

					Log.d(TAG, "Successfully logged in again.");
				}
			} catch (Exception e1) {
				Log.w(TAG, "Exception while trying to determine the login-status: " + e1.getMessage() + ". Throwing new exception with message: " + bl.getString(R.string.EXCEPTION_COULDNT_CHECK_LOGIN_STATUS));
				throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_CHECK_LOGIN_STATUS), e1);
			}
			
			try {
				Log.d(TAG, "Trying to repost message..");
				this.cbWrapper.postMessage(m);
				Log.d(TAG, "Successfully reposted message..");
			} catch (Exception e1) {
				Log.w(TAG, "Reposting message failed with exception: " + e1.getMessage() + ". Throwing new exception with message: " + bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE));
				throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE), e1);
			}
		}
		catch(Exception e){
			Log.w(TAG, "Posting failed because of an arbitrary Exception: " + e.getMessage());
			throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE), e);
		}	
	}
	
	public synchronized void postMessage(String encodedMessage) throws BusinessLogicException{
		try{
			Log.d(TAG, "Try posting message: " + encodedMessage + "..");
			this.cbWrapper.postMessage(encodedMessage);
			Log.d(TAG, "Posting successfull.");
		}
		catch(WrapperException e){
			Log.w(TAG, "Posting failed because of a BusinessLogicException: " + e.getMessage());
			try {
				Log.d(TAG, "Checking, if currently logged in before trying to repost message..");
				if(this.cbWrapper.isLoggedIn()){
					Log.d(TAG, "Currently logged in. Can not fix this problem. Throwing new BusinessLogicException with message: " + bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN));
					throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGGED_IN), e);
				}
				else{
					Log.d(TAG, "Currently not logged in. Trying to log in again..");
					this.cbWrapper.logIn(this.username, this.password);
					Log.d(TAG, "Successfully logged in again.");
				}
			} catch (Exception e1) {
				Log.w(TAG, "Exception while trying to determine the login-status: " + e1.getMessage() + ". Throwing new exception with message: " + bl.getString(R.string.EXCEPTION_COULDNT_CHECK_LOGIN_STATUS));
				throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_CHECK_LOGIN_STATUS), e1);
			}
			
			try {
				Log.d(TAG, "Trying to repost message..");
				this.cbWrapper.postMessage(encodedMessage);
				Log.d(TAG, "Successfully reposted message..");
			} catch (Exception e1) {
				Log.w(TAG, "Reposting message failed with exception: " + e1.getMessage() + ". Throwing new exception with message: " + bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE));
				throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE_DESPITE_LOGIN_UPDATE), e1);
			}
		}
		catch(Exception e){
			Log.w(TAG, "Posting failed because of an arbitrary Exception: " + e.getMessage());
			throw new BusinessLogicException(bl.getString(R.string.EXCEPTION_COULDNT_POST_MESSAGE), e);
		}
	}

	public void setUserdata(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public boolean checkUserdata() throws BusinessLogicException{
		// TODO Auto-generated method stub
		return false;
	}
}
