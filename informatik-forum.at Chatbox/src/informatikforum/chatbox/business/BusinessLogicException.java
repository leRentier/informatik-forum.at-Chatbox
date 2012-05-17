package informatikforum.chatbox.business;

/**
 * Exception concerning everything in the wrapper-Package.
 * 
 * @author emptyvi
 * @version 1.0.0
 */
public class BusinessLogicException extends Exception {

	/**
	 * Used for serialization
	 */
	private static final long serialVersionUID = 5426962647931942866L;

	
	/**
	 * Normal constructor
	 * Makes a call to the super-Constructor.
	 */
	public BusinessLogicException(){
		super();
	}
	
	
	/**
	 * Takes a message (and passes it to the super-constructor)
	 * 
	 * @param msg The message to pass on.
	 */
	public BusinessLogicException(String msg){
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
	public BusinessLogicException(String msg, Exception e){
		super(msg,e);
	}
}
