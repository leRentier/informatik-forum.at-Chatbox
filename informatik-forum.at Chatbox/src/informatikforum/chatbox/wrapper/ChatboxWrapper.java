package informatikforum.chatbox.wrapper;

import informatikforum.chatbox.R;
import informatikforum.chatbox.business.BusinessLogic;
import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.gui.MessageFormatter;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class ChatboxWrapper{
	
	private static ChatboxWrapper instance = null;
	
	private static final String TAG = "ChatboxWrapper";

	
	private final HttpClient httpclient;
	private final BusinessLogic bl;
	
	private ChatboxWrapper(){
		this.httpclient = createHttpClient();
		bl = BusinessLogic.getInstance();
	}
	
	public static ChatboxWrapper getInstance(){
		if(instance == null){
			instance = new ChatboxWrapper();
		}
		
		return instance;
	}

	
	private HttpClient createHttpClient(){

		// Get a http-client..
		DefaultHttpClient httpclient = new DefaultHttpClient();

		// Set the cookie-policy. This is important as cookies won't be sent in
		// the second redirect
		// using standard method.
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

		return httpclient;
	}
	
	public String reloadSecurityTokenFromUrl(String url) throws WrapperException{
		HttpResponse response;
		HttpGet httpGet;
		String htmlSource;
		
		try{
			httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);

			htmlSource = EntityUtils.toString(response.getEntity());
			response.getEntity().consumeContent();
			return reloadSecurityTokenFromHtml(htmlSource);
		}
		catch(Exception e){
			throw new WrapperException(bl.getString(R.string.EXCEPTION_SECURITYTOKEN_NOT_FOUND));	
		}
	}
	
	
	public String reloadSecurityTokenFromHtml(String htmlString) throws WrapperException{
		
		try {
			Pattern patToken = Pattern.compile(bl.getString(R.string.SECURITY_TOKEN_PATTERN));
			Matcher matToken = patToken.matcher(htmlString);
			
			if(matToken.find()){
				return matToken.group(1);
			}
			else{
				throw new WrapperException(bl.getString(R.string.EXCEPTION_SECURITYTOKEN_NOT_FOUND));
			}
			
			
		} catch (Exception e){
			throw new WrapperException(bl.getString(R.string.EXCEPTION_SECURITYTOKEN_NOT_FOUND), e);
		}
	}
	
	
	public String reloadSecurityToken() throws WrapperException{
		return reloadSecurityTokenFromUrl(bl.getString(R.string.SECURITY_TOKEN_URL));
	}
	
	
	protected ArrayList<Message> parseMessagesFromHtml(String html) throws WrapperException{
		
		ArrayList<Message> messages;
		Pattern pat;
		Matcher matcher;
		int cnt;
		
		try {
			// Get the messages..
			pat = Pattern.compile(bl.getString(R.string.MESSAGE_PATTERN), Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
			matcher = pat.matcher(html);

			messages = new ArrayList<Message>();

			while(matcher.find()){
				
				// As this is the first round for retrieving infos, create new messages..
				Message m = new Message();
				
				MessageFormatter.decodeMessage(StringEscapeUtils.unescapeHtml4(matcher.group(1).trim()), m);
				messages.add(m);
			}

			// Get the users..
			pat = Pattern.compile(bl.getString(R.string.USER_PATTERN), Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
			matcher = pat.matcher(html);
			
			cnt=0;
			for(; cnt<messages.size() && matcher.find(); cnt++){
				messages.get(cnt).setUser(StringEscapeUtils.unescapeHtml4(matcher.group(1).replaceAll("<.*?>", "").trim()));
			}
			if((cnt == messages.size() && matcher.find()) || (cnt != messages.size())){
				throw new WrapperException(bl.getString(R.string.EXCEPTION_CANT_FIND_MESSAGES));
			}
			
			// Get the IDs..
			pat = Pattern.compile(bl.getString(R.string.ID_PATTERN), Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
			matcher = pat.matcher(html);
			
			cnt=0;
			for(; cnt<messages.size() && matcher.find(); cnt++){
				// Remove other elements like colorings etc.
				messages.get(cnt).setId(Long.parseLong(matcher.group(1).replaceAll("<.*?>", "").trim()));
			}
			if((cnt == messages.size() && matcher.find()) || (cnt != messages.size())){
				throw new WrapperException(bl.getString(R.string.EXCEPTION_CANT_FIND_MESSAGES));
			}
			
			// Get the timestamps..
			pat = Pattern.compile(bl.getString(R.string.TIMESTAMP_PATTERN), Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
			matcher = pat.matcher(html);
			
			cnt=0;
			for(; cnt<messages.size() && matcher.find(); cnt++){
				messages.get(cnt).setTimeStamp(parseDate(new GregorianCalendar().get(GregorianCalendar.YEAR) + "-" + matcher.group(1).replaceAll("<.*?>", "").trim()));
			}
			if((cnt == messages.size() && matcher.find()) || (cnt != messages.size())){
				throw new WrapperException(bl.getString(R.string.EXCEPTION_CANT_FIND_MESSAGES));
			}
			
			// There should be at least one message..
			if(messages.size() <=0){
				throw new WrapperException(bl.getString(R.string.EXCEPTION_CANT_FIND_MESSAGES));
			}

		} catch (Exception e) {
			throw new WrapperException(bl.getString(R.string.EXCEPTION_CANT_FIND_MESSAGES), e);
		}

		return messages;
	}
	
	
	private Date parseDate(String dateString) throws WrapperException{
		SimpleDateFormat sdf = new SimpleDateFormat(bl.getString(R.string.PATTERN_DATE));
		try {
			return sdf.parse(dateString);
		} catch (Exception e) {
			throw new WrapperException(bl.getString(R.string.EXCEPTION_DATE_NOT_PARSABLE) + dateString, e);
		}
	}

	private String htmlEscapeUnencodableCharacters(String message) {
		StringBuilder ret = new StringBuilder();
		int highUnicodeChar = 0;
		boolean awaitingLowSurrogate = false;

		CharsetEncoder forumCharsetEncoder = Charset.forName(bl.getString(R.string.FORUM_CHARSET_NAME)).newEncoder();

		for(char c : message.toCharArray()){
			if(awaitingLowSurrogate){
				if(Character.isLowSurrogate(c)){
					highUnicodeChar |= (c - 0xDC00);
					highUnicodeChar += 0x010000;

					// append the XML character entity
					ret.append("&#");
					ret.append(highUnicodeChar);
					ret.append(";");

					// reset the surrogate pair state
					highUnicodeChar = 0;
					awaitingLowSurrogate = false;
				}
				else{
					// leading surrogate not followed by trailing surrogate
					throw new WrapperException(bl.getString(R.string.EXCEPTION_ENCODING_FAILED_INVALID_UTF16));
				}
			}
			else if(Character.isHighSurrogate(c)){
				highUnicodeChar = (c - 0xD800) << 10;
				awaitingLowSurrogate = true;
			}
			else if(Character.isLowSurrogate(c)){
				// trailing surrogate following something that isn't a leading surrogate
				throw new WrapperException(bl.getString(R.string.EXCEPTION_ENCODING_FAILED_INVALID_UTF16));
			}
			else if(forumCharsetEncoder.canEncode(c)){
				// forum's encoding supports it directly
				ret.append(c);
			}
			else{
				// XML character entity
				ret.append("&#");
				ret.append((int)c);
				ret.append(";");
			}
		}

		return ret.toString();
	}
	
	public synchronized void postMessage(String encodedMessage) throws WrapperException{
		String escapedEncodedMessage = htmlEscapeUnencodableCharacters(encodedMessage);
		try {
			HttpPost httpost = new HttpPost(bl.getString(R.string.POSTMESSAGE_URL));
			
			// Set up a list with the necessary POST-name-value pairs.
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
			nvps.add(new BasicNameValuePair("do", "cb_postnew"));
			nvps.add(new BasicNameValuePair("vsacb_newmessage", escapedEncodedMessage));
			nvps.add(new BasicNameValuePair("do", "cb_postnew"));
			nvps.add(new BasicNameValuePair("color", "Black"));
			nvps.add(new BasicNameValuePair("securitytoken", reloadSecurityToken()));
			nvps.add(new BasicNameValuePair("s", ""));

			httpost.setEntity(new UrlEncodedFormEntity(nvps, bl.getString(R.string.FORUM_CHARSET_NAME)));
			
			// Execute the post.
			Log.i(TAG, "Posting message : " + encodedMessage);	
			HttpEntity entity = httpclient.execute(httpost).getEntity();
			
			// Checking response (which should be empty)
			Log.d(TAG, "Checking server response for posted message..");
			if(!EntityUtils.toString(entity).equals("")){
				Log.d(TAG, "Server response for post was not empty, which means, that the post has failed. Throw new Wrapper Exception with message: "  + bl.getString(R.string.EXCEPTION_POST_FAILED_RECEIVED_RESPONSE));
				throw new WrapperException(bl.getString(R.string.EXCEPTION_POST_FAILED_RECEIVED_RESPONSE));
			}
			Log.d(TAG, "Server response for post was ok (empty).");
		}
		catch (Exception e) {
			throw new WrapperException(bl.getString(R.string.EXCEPTION_POST_FAILED), e);
		}
	}
	
	public synchronized void postMessage(Message m) throws WrapperException{
		postMessage(MessageFormatter.encodeMessage(m));
	}
	
	
	public synchronized ArrayList<Message> retrieveMessages() throws WrapperException{
		ArrayList<Message> lvas = new ArrayList<Message>();
		HttpResponse response;
		HttpGet httpGet;
		String htmlSource;
		
		// Navigate to the messages-site.
		try {
			httpGet = new HttpGet(bl.getString(R.string.MESSAGES_URL));
			response = httpclient.execute(httpGet);
		} catch (Exception e){
			throw new WrapperException(bl.getString(R.string.EXCEPTION_GETTING_CERTIFICATE_URL_FAILED), e);
		}
		
		// If retrieving the response was successfull, parse the content for messages..
		// Unescaping here would cost REALLY much time.. only unescape the content in parseMessagesFromHtml()..
		try{
			htmlSource = EntityUtils.toString(response.getEntity());
			
			lvas = this.parseMessagesFromHtml(htmlSource);
		} catch (Exception e){
			throw new WrapperException(bl.getString(R.string.EXCEPTION_RETRIEVING_SOURCECODE), e);
		}
		
		return lvas;
	}

	
	private String getMd5(String s) throws WrapperException{
		MessageDigest m;
		
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new WrapperException(bl.getString(R.string.EXCEPTION_UNABLE_TO_RETRIEVE_PASSWORDHASH), e);
		}
	
		m.update(s.getBytes(),0,s.length());
		return new BigInteger(1,m.digest()).toString(16).toString();
	}
	
	
	public synchronized boolean logIn(String username, String password) throws WrapperException {
		String passwordMd5 = getMd5(password);
		
		// As there can go very much wrong, a bigger try-block catching 'Exception'.
		try {
			HttpPost httpost = new HttpPost(bl.getString(R.string.POSTLOGIN_URL));
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("vb_login_username", username));
			nvps.add(new BasicNameValuePair("vb_login_password", ""));
			nvps.add(new BasicNameValuePair("vb_login_password_hint", "Password"));
			nvps.add(new BasicNameValuePair("s", ""));
			nvps.add(new BasicNameValuePair("securitytoken", "guest"));
			nvps.add(new BasicNameValuePair("do", "login"));
			nvps.add(new BasicNameValuePair("s", ""));
			nvps.add(new BasicNameValuePair("vb_login_md5password", passwordMd5));
			nvps.add(new BasicNameValuePair("vb_login_md5password_utf", passwordMd5));

			httpost.setEntity(new UrlEncodedFormEntity(nvps));
			httpclient.execute(httpost).getEntity().consumeContent();
			
			// Design decision: Instead of checking the answer directly for another
			// logged-in word, we use isLoggedIn(), which will itself load a certain page
			// and determines there with a loggedIn() word, if we are already logged in.
			return isLoggedIn();
		}
		catch(Exception e){
			throw new WrapperException(bl.getString(R.string.EXCEPTION_LOGIN_FAILED), e);
		}
	}
	
	public synchronized boolean isLoggedIn() throws WrapperException{
		HttpResponse response;
		HttpGet httpGet;
		
		try {
			httpGet = new HttpGet(bl.getString(R.string.LOGINCHECK_URL));
			response = httpclient.execute(httpGet);
			String responseString = EntityUtils.toString(response.getEntity());
			response.getEntity().consumeContent();

			return responseString.contains(bl.getString(R.string.LOGGEDIN_WORD));
		} catch (Exception e){
			throw new WrapperException(bl.getString(R.string.EXCEPTION_GETTING_CERTIFICATE_URL_FAILED), e);
		}
	}
	
	public void shutDown(){
		httpclient.getConnectionManager().shutdown();	
	}
}