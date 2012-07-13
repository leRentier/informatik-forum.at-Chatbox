package informatikforum.chatbox.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * DAO representing a single Message.
 * 
 * @author emptyvi
 * @version 1.0.0
 */
public class Message implements Comparable<Message>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3183614406431857869L;

	/**
	 * Internal id of the message in the forum.
	 */
	private long id;
	
	/**
	 * Time, when the message was posted.
	 */
	private Date timeStamp = null;
	
	/**
	 * Author of the message.
	 */
	private String user = null;
	
	/**
	 * The actual message (with links and smileys substituted by a placeholder-character).
	 */
	private String message = null;
	
	/**
	 * Contains all Smileys used in the message.
	 * Keys are the index of the corresponding placeholder-characters in
	 * the message, values are the Smileys.
	 */
	private HashMap<Integer, Smiley> smileys;
	
	/**
	 * Contains all Links used in the message.
	 * Keys are the index of the corresponding placeholder-characters in
	 * the message, values are the SLinks.
	 */
	private HashMap<Integer, String> links;
	
	
	/**
	 * Constructor that takes the message only.
	 * All other necessary fields will be initialized by using the
	 * parameterless Constructor.
	 * 
	 * @param message Message containing placeholders for Smileys and Links,
	 * that will be inserted later.
	 */
	public Message(String message){
		this();
		this.message = message;
	}
	
	
	/**
	 * Constructor that takes the message, smileys and links.
	 * All other necessary fields will be initialized by using the
	 * parameterless Constructor.
	 * 
	 * @param message Message containing placeholders for Smileys and Links,
	 * that will be inserted later.
	 * @param smileys Contains key-value pairs, where the key represents the corresponing
	 * placeholder-character in the message of the Smiley, that is used as the value.
	 * @param links Contains key-value pairs, where the key represents the corresponding
	 * placeholder-character in the message of the Link, that is used as the value.
	 */
	public Message(String message, HashMap<Integer, Smiley> smileys, HashMap<Integer, String> links){
		this();
		this.message = message;
		this.smileys = smileys;
		this.links = links;
	}
	
	
	/**
	 * Constructor that takes the ID, timestamp, user, message, smileys and links.
	 * 
	 * @param id Internal id of the message in the forum.
	 * @param timeStamp Time, when the message was posted.
	 * @param user Author of the message.
	 * @param message Message containing placeholders for Smileys and Links,
	 * that will be inserted later.
	 * @param smileys Contains key-value pairs, where the key represents the corresponing
	 * placeholder-character in the message of the Smiley, that is used as the value.
	 * @param links Contains key-value pairs, where the key represents the corresponding
	 * placeholder-character in the message of the Link, that is used as the value.
	 */
	public Message(long id, Date timeStamp, String user, String message, HashMap<Integer, Smiley> smileys, HashMap<Integer, String> links){
		this.timeStamp = timeStamp;
		this.user = user;
		this.message = message;
		this.smileys = smileys;
		this.links = links;
	}
	
	
	/**
	 * Normal Constructor that takes no arguments.
	 * Sets the id to 0, timeStamp, user and message to null and initializes smileys and links
	 * with a new HashMap.
	 */
	public Message(){
		this.id = 0;
		this.timeStamp = null;
		this.user = null;
		this.message = null;
		this.setSmileys(new HashMap<Integer, Smiley>());
		this.setLinks(new HashMap<Integer,String>());
	}
	
	
	/**
	 * Returns the ID of the message.
	 * The ID is the internal id of the message in the forum.
	 * 
	 * @return The id of the message.
	 */
	public long getId() {
		return id;
	}
	
	
	/**
	 * Sets the ID of the message.
	 * The ID is the internal id of the message in the forum.
	 * 
	 * @param id The id of the message.
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	
	/**
	 * Returns the message itself.
	 * Links and smileys are substituted by a placeholder-character.
	 * 
	 * @return The actual message having smileys and links substituted by placeholder-characters.
	 */
	public String getMessage() {
		return message;
	}
	
	
	/**
	 * Sets the message of the message.
	 * Links and smileys must be substituted by placeholder-characters (and be added to the corresponding
	 * HashMaps).
	 * 
	 * @param message The actual message having smileys and links substituted by placeholder-characters.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public String getUser() {
		return user;
	}
	
	
	public void setUser(String user) {
		this.user = user;
	}
	
	
	@Override
	public String toString(){
		String smileyString = "";
		String linksString = "";
		
		if(smileys != null){
			for(Integer i: smileys.keySet()){
				smileyString += "Smileyposition: " + i + ", Smileyname: " + smileys.get(i).getName() + ", Smileyurl: " + smileys.get(i).getUrl() + "\n";
			}
		}
		smileyString = smileyString.trim();
		
		if(links != null){
			for(Integer i: links.keySet()){
				linksString += "Linkposition: " + i + ", Link: " + links.get(i) + "\n";
			}
		}
		linksString = linksString.trim();
		
		return "Time: " + timeStamp + ", User: " + user + ", Message: " + message + ", ID: " + id + "\nSmileylist:\n" + smileyString + "\nLinklist:\n" + linksString + "\n------------";
	}
	
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	

	public HashMap<Integer, Smiley> getSmileys() {
		return smileys;
	}
	

	public void setSmileys(HashMap<Integer, Smiley> smileys) {
		this.smileys = smileys;
	}
	
	
	public void addSmiley(Integer position, Smiley s){
		this.smileys.put(position, s);
	}
	
	public void addLink(Integer position, String link){
		this.links.put(position, link);
	}

	
	public HashMap<Integer, String> getLinks() {
		return links;
	}

	
	public void setLinks(HashMap<Integer, String> links) {
		this.links = links;
	}

	@Override
	public int compareTo(Message another) {
		return (int) (this.getId() - another.getId());
	}

}
