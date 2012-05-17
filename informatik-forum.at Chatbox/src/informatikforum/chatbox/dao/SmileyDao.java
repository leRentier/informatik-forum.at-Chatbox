package informatikforum.chatbox.dao;

import informatikforum.chatbox.entity.Smiley;

import java.util.List;


/**
 * Interface that deals with loading available Smileys from a datasource.
 * 
 * @author emptyvi
 * @version 1.0.0
 */
public interface SmileyDao {
	
	/**
	 * Returns a List containing all available Smileys.
	 * The list was read in in the constructor.
	 */
	public List<Smiley> getAvailableSmileys() throws DaoException;
	
	
	/**
	 * Returns the Smiley identified by a given URL.
	 * 
	 * @param url The URL to look up for.
	 * @throws DaoException Will be thrown, if the requested Smiley is not
	 * available.
	 */
	public Smiley getSmileyByUrl(String url) throws DaoException;

}
