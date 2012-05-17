package informatikforum.chatbox.tcp;

/**
 * Exception concerning everything in the dao-Package.
 * 
 * @author emptyvi
 * @version 1.0.0
 */
public class TCPException extends Exception {

	/**
	 * Used for serialization
	 */
	private static final long serialVersionUID = 5287155160249469781L;

	
	/**
	 * Normal constructor
	 * Makes a call to the super-Constructor.
	 */
	public TCPException(){
		super();
	}
	
	
	/**
	 * Takes a message (and passes it to the super-constructor)
	 * 
	 * @param msg The message to pass on.
	 */
	public TCPException(String msg){
		super(msg);
	}
	
	
	/**
	 * Takes a message and an Exception (and passes it to the super-constructor)
	 * Used for Exception-chaining
	 * 
	 * @param msg The message to pass on
	 * @param e Another exception, that was caught using try/catch and is
	 * now rethrown.
	 */
	public TCPException(String msg, Exception e){
		super(msg,e);
	}
}
