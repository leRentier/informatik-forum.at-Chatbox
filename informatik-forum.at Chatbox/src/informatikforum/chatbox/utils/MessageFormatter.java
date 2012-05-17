package informatikforum.chatbox.utils;

import informatikforum.chatbox.AnimatedGifDrawable;
import informatikforum.chatbox.AnimatedGifDrawable.UpdateListener;
import informatikforum.chatbox.AnimatedImageSpan;
import informatikforum.chatbox.CommonData;
import informatikforum.chatbox.dao.CSVSmileyDao;
import informatikforum.chatbox.dao.DaoException;
import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.entity.Smiley;
import informatikforum.chatbox.wrapper.WrapperException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

import android.app.Application;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;



public class MessageFormatter extends Application{
	
	private static final String EXCEPTION_CANT_ENCODE_MESSAGE = "Couldn't encode the given message with smileys, links etc.";
	private static final String EXCEPTION_CANT_DECODE_MESSAGE = "Couldn't decode the given message into smileys, links etc.";
	
	private static final String SMILEY_PATTERN = "<img src=\"(.*?.gif)\".*?/>";
	private static final String LINK_PATTERN = "<a href=\"(.*?)\".*?</a>";
	private static final String HOST_URL = "http://www.informatik-forum.at";
	private static CommonData cd = CommonData.getInstance();
	
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
			throw new WrapperException(EXCEPTION_CANT_ENCODE_MESSAGE, e);
		}

		return rawString;
	}
	
	
	public static void decodeMessage(String rawString, Message m) throws DaoException, WrapperException{

		try{
			Pattern patSmiley = Pattern.compile(SMILEY_PATTERN);
			Pattern patLink = Pattern.compile(LINK_PATTERN);
			Matcher matSmiley;
			Matcher matLink;
	
			for(int i=0; i<rawString.length()+1; i++){
				String substi = rawString.substring(0,i);
	
				matSmiley = patSmiley.matcher(substi);
				matLink = patLink.matcher(substi);
				
				if(matSmiley.find()){
					m.getSmileys().put(matSmiley.start(), CSVSmileyDao.getInstance().getSmileyByUrl(HOST_URL + "/" + matSmiley.group(1))); //TODO refine?
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
			throw new WrapperException(EXCEPTION_CANT_DECODE_MESSAGE, e);
		}
	}
	
	public static String getHtmlRepresentationForMessageList(List<Message> ml) throws WrapperException{
		String result = "<html><body><table border=\"1px\" style=\"width: 100% word-wrap:break-word\">";
		
		for(int i=0; i<ml.size()-1; i++){
			result += getHtmlRepresentationForMessage(ml.get(i));
		}
		
		if(ml.size()!=0){
			result += getHtmlRepresentationForMessage(ml.get(ml.size()-1));
		}
		
		result += "</table></body></html>";
System.err.println(result);
		return result;
	}
	
	public static String getHtmlRepresentationForMessage(Message m) throws WrapperException{
		String rawString = m.getMessage();
		
		// Add smileys and links to the textmessage.
		try{
			List<Integer> smileyPositions ;
			List<Integer> linkPositions;
			
			ArrayList<Integer> positions;
			
			for(int i=0; i<rawString.length(); i++){
				char c = rawString.charAt(i);
				String escapeSeq = StringEscapeUtils.escapeHtml4(String.valueOf(c));
				int lengthExtension = escapeSeq.length()-1;
				
				if(lengthExtension > 0){
					for(Integer k: m.getSmileys().keySet()){
						if(k>i){
							Smiley s = m.getSmileys().get(k);
							m.getSmileys().remove(k);
							m.getSmileys().put(k+lengthExtension, s);
						}
					}
					
					for(Integer k: m.getLinks().keySet()){
						if(k>i){
							String s = m.getLinks().get(k);
							m.getLinks().remove(k);
							m.getLinks().put(k+lengthExtension, s);
						}
					}
				}
				
				rawString = rawString.substring(0,i) + escapeSeq + rawString.substring(i+1,rawString.length());
				i+=lengthExtension;
			}
			
			smileyPositions = new ArrayList<Integer>(m.getSmileys().keySet());			
			linkPositions = new ArrayList<Integer>(m.getLinks().keySet());
			
			positions = new ArrayList<Integer>();
			positions.addAll(linkPositions);
			positions.addAll(smileyPositions);

			Collections.sort(positions);
			Collections.reverse(positions);	
			
			for(Integer i: positions){
				if(smileyPositions.contains(i)){
					rawString = rawString.substring(0,i) + "<img src=\"" + m.getSmileys().get(i).getUrl() + "\" title=\"" + m.getSmileys().get(i).getUrl() + "\"/>" + rawString.substring(i+1);
				}
				else if(linkPositions.contains(i)){
					rawString = rawString.substring(0,i) + "<a href=\"" + m.getLinks().get(i) + "\">" + m.getLinks().get(i) + "</a>" + rawString.substring(i+1);
				}
			}
		}
		catch (Exception e) {
			throw new WrapperException(EXCEPTION_CANT_ENCODE_MESSAGE, e);
		}

		rawString = "<tr valign=\"top\"><td style=\"white-space: nowrap; width: 1px\"><b>" + m.getUser() + "</b></td>"
		+ "<td>" + cd.getSimpleDateFormat().format(m.getTimeStamp()) + "</td></tr>"
		+ "<tr style=\"word-wrap:break-word\"><td colspan=\"2\" style=\"word-wrap:break-word\">" + rawString + "</td></tr>";
		
		return rawString;
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
			
			for(Integer i: positions){
				if(smileyPositions.contains(i)){
					
					AnimatedImageSpan ais;
					UpdateListener ul;
					
					// First the UpdateListener.. Check, whether we already created one once for
					// this smiley and this message. 

					if(cd.getBufferedListeners().contains(tv.hashCode() + Integer.toString(m.getSmileys().get(i).getImageId()))){

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
						cd.getBufferedListeners().add(tv.hashCode() + Integer.toString(m.getSmileys().get(i).getImageId()));
					}
					
					// Check, whether we already have the appropriate AnimatedImageSpan..
					if(cd.getBufferedAnimatedImageSpannables().containsKey(Long.toString(m.getId()) + Integer.toString(i))){		
						
						// If we have, simply use it..
						ais = cd.getBufferedAnimatedImageSpannables().get(Long.toString(m.getId()) + Integer.toString(i));

						// BUT!!!!!!!!!
						// Set new UpdateListener. This is done by directly approaching the AnimatedGifDrawable
						// which was buffered (as all AnimatedImageSpans are only using them.
						cd.getBufferedGifs().get(m.getSmileys().get(i).getImageId()).addUpdateListener(ul);
					}
					else{
						
						// But if we haven't, we have to create it. For this, we need two things:
						// An AnimatedGifDrawable and
						// an UpdateListener.
						
						AnimatedGifDrawable agd;
						
						

							
						// Now for the AnimatedGifDrawable.. The MessageRetrieveService is responsible for
						// fetching them, so we will have just to wait..
						while(!cd.getBufferedGifs().containsKey(m.getSmileys().get(i).getImageId())){
							try {
								synchronized(cd.getGifLock()){
									cd.getGifLock().wait();
								}
							} catch (InterruptedException e) {
								// Should never happen.
							}
						}
						
						// Get the saved AnimatedGifDrawable and add the brand new UpdateListener (or null).
						agd = cd.getBufferedGifs().get(m.getSmileys().get(i).getImageId());
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
					//rawString = rawString.substring(0,i) + "<a href=\"" + m.getLinks().get(i) + "\">" + m.getLinks().get(i) + "</a>" + rawString.substring(i+1);
				}
			}

		return ssb;
}

}
