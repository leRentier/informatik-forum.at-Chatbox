package informatikforum.chatbox.gui;

import informatikforum.chatbox.R;
import informatikforum.chatbox.business.BusinessLogic;
import informatikforum.chatbox.business.CommonData;
import informatikforum.chatbox.dao.SmileyData;
import informatikforum.chatbox.dao.DaoException;
import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.entity.Smiley;
import informatikforum.chatbox.gui.gif.AnimatedGifDrawable;
import informatikforum.chatbox.gui.gif.AnimatedImageSpan;
import informatikforum.chatbox.gui.gif.AnimatedGifDrawable.UpdateListener;
import informatikforum.chatbox.wrapper.WrapperException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Application;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;



public class MessageFormatter extends Application{
	

	private static CommonData cd = CommonData.getInstance();
	private static BusinessLogic bl = BusinessLogic.getInstance();
	
	public static String encodeMessage(Message m) throws WrapperException{

		String rawString = m.getMessage();
		
		try{
			List<Integer> smileyPositions = new ArrayList<Integer>(m.getSmileys().keySet());
			List<Integer> linkPositions = new ArrayList<Integer>(m.getLinks().keySet());
			
			ArrayList<Integer> positions = new ArrayList<Integer>();
			positions.addAll(linkPositions);
			positions.addAll(smileyPositions);
			
			Collections.sort(positions);
			Collections.reverse(positions);
			
			for(Integer i: positions){
				if(smileyPositions.contains(i)){
					rawString = rawString.substring(0,i) + m.getSmileys().get(i).getBbCode() + rawString.substring(i+1);
				}
				else if(linkPositions.contains(i)){
					rawString = rawString.substring(0,i) + m.getLinks().get(i) + rawString.substring(i+1);
				}
			}
		}
		catch (Exception e) {
			throw new WrapperException(bl.getString(R.string.EXCEPTION_CANT_ENCODE_MESSAGE), e);
		}

		return rawString;
	}
	
	
	public static void decodeMessage(String rawString, Message m) throws DaoException, WrapperException{

		try{
			Pattern patSmiley = Pattern.compile(bl.getString(R.string.SMILEY_PATTERN));
			Pattern patLink = Pattern.compile(bl.getString(R.string.LINK_PATTERN));
			Matcher matSmiley;
			Matcher matLink;
	
			for(int i=0; i<rawString.length()+1; i++){
				String substi = rawString.substring(0,i);
	
				matSmiley = patSmiley.matcher(substi);
				matLink = patLink.matcher(substi);
				
				if(matSmiley.find()){
					m.getSmileys().put(matSmiley.start(), SmileyData.getInstance().getSmileyByUrl(bl.getString(R.string.HOST_URL) + "/" + matSmiley.group(1))); //TODO refine?
					rawString = rawString.substring(0, matSmiley.start()) + Smiley.PLACEHOLDER + rawString.substring(matSmiley.end());
					i=0;
				}
				else if(matLink.find()){
					m.getLinks().put(matLink.start(), matLink.group(1));
					rawString = rawString.substring(0, matLink.start()) + Smiley.PLACEHOLDER + rawString.substring(matLink.end());
					i=0;
				}
			}
			m.setMessage(rawString);
		}
		catch (Exception e) {
			throw new WrapperException(bl.getString(R.string.EXCEPTION_CANT_DECODE_MESSAGE), e);
		}
	}

	
	public static Spannable getAndroidRepresentationForMessageText(Message m, final TextView tv){
		
		SpannableStringBuilder ssb = new SpannableStringBuilder(m.getMessage());
		
		// Add smileys and links to the textmessage.
			List<Integer> smileyPositions ;
			List<Integer> linkPositions;
			
			ArrayList<Integer> positions;

			
			smileyPositions = new ArrayList<Integer>(m.getSmileys().keySet());			
			linkPositions = new ArrayList<Integer>(m.getLinks().keySet());
			
			positions = new ArrayList<Integer>();
			positions.addAll(linkPositions);
			positions.addAll(smileyPositions);

			Collections.sort(positions);
			Collections.reverse(positions);	
			
			HashMap<String, Integer> linkOccurrences = new HashMap<String, Integer>();
			
			for(Integer i: positions){
				if(smileyPositions.contains(i)){
					
					AnimatedImageSpan ais;
					UpdateListener ul;
					
					// First the UpdateListener.. Check, whether we already created one once for
					// this smiley and this message. 

					if(cd.getBufferedListeners().contains(tv.hashCode() + Integer.toString(SmileyData.getInstance().getFileIdForUrl(m.getSmileys().get(i).getUrl())))){

						// If such an UpdateListener is alreay in use, it will do the work for us.
						ul = null;
					}
					else{
						
						// If not, create one!
						ul = new AnimatedGifDrawable.UpdateListener() {   
						    @Override
						    public void update() {
						       tv.postInvalidate();
						    }
						};
						
						// Don't forget to add it to the buffered-list.
						cd.getBufferedListeners().add(tv.hashCode() + Integer.toString(SmileyData.getInstance().getFileIdForUrl(m.getSmileys().get(i).getUrl())));
					}
					
					// Check, whether we already have the appropriate AnimatedImageSpan..
					if(cd.getBufferedAnimatedImageSpannables().containsKey(Long.toString(m.getId()) + Integer.toString(i))){		
						
						// If we have, simply use it..
						ais = cd.getBufferedAnimatedImageSpannables().get(Long.toString(m.getId()) + Integer.toString(i));

						// BUT!!!!!!!!!
						// Set new UpdateListener. This is done by directly approaching the AnimatedGifDrawable
						// which was buffered (as all AnimatedImageSpans are only using them.
						cd.getBufferedGifs().get(SmileyData.getInstance().getFileIdForUrl(m.getSmileys().get(i).getUrl())).addUpdateListener(ul);
					}
					else{
						
						// But if we haven't, we have to create it. For this, we need two things:
						// An AnimatedGifDrawable and
						// an UpdateListener.
						
						AnimatedGifDrawable agd;
						
						

							
						// Now for the AnimatedGifDrawable.. The MessageRetrieveService is responsible for
						// fetching them, so we will have just to wait..
						while(!cd.getBufferedGifs().containsKey(SmileyData.getInstance().getFileIdForUrl(m.getSmileys().get(i).getUrl()))){
							try {
								synchronized(cd.getGifLock()){
									cd.getGifLock().wait();
								}
							} catch (InterruptedException e) {
								// Should never happen.
							}
						}
						
						// Get the saved AnimatedGifDrawable and add the brand new UpdateListener (or null).
						agd = cd.getBufferedGifs().get(SmileyData.getInstance().getFileIdForUrl(m.getSmileys().get(i).getUrl()));
						agd.addUpdateListener(ul);
							
						// Finally, create the new agd.
						ais = new AnimatedImageSpan(agd);
						
						// Add it to the bufferlistener.
						cd.getBufferedAnimatedImageSpannables().put(Long.toString(m.getId()) + Integer.toString(i), ais);
					}
					
					// Set the span.. we are done!
					ssb.setSpan(ais, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					
				}
				else if(linkPositions.contains(i)){
					String url = m.getLinks().get(i);
					
					// We need to create a new LinkSpan object for each URL.
					// To avoid creating new objects whenever the messages are updated,
					// already created objects will be cached in CommonData.
					// Unfortunately, one LinkSpan can not be used more than once
					// within the same Spannable (I don't know why, but if the same URL
					// occurs twice within a message, only the first occurrence will be
					// highlighted if the same LinkSpan object is used for both occurrences).
					// As a workaround, the occurrences of each URL in a message will be
					// numbered. The map linkOccurrences stores how often which URL has
					// already been processed in the message currently being formatted. 
					int occurrence = 1;
					if(linkOccurrences.containsKey(url)) {
						occurrence = linkOccurrences.get(url) + 1;
					}
					linkOccurrences.put(url, occurrence);
					
					LinkSpan cs;
					if(cd.getBufferedClickSpannables().containsKey(url + occurrence)) {
						cs = cd.getBufferedClickSpannables().get(url + occurrence);
					}
					else {
						cs = new LinkSpan(bl, url);
						cd.getBufferedClickSpannables().put(url + occurrence, cs); 
					}
					ssb.setSpan(cs, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					
					// TODO use the link text instead of the URL here (requires server-side change?)
					ssb.replace(i, i+1, url);
				}
			}

		return ssb;
	}
}
