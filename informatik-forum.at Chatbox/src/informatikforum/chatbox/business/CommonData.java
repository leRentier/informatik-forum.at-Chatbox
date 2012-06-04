package informatikforum.chatbox.business;

import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.gui.LinkSpan;
import informatikforum.chatbox.gui.gif.AnimatedGifDrawable;
import informatikforum.chatbox.gui.gif.AnimatedImageSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;


public class CommonData {
	
	private static CommonData instance = null;

	private ArrayList<Message> messages;
	private long waitTime;
	private SimpleDateFormat sdf;
	private Context context;
	private ConcurrentHashMap<Integer, AnimatedGifDrawable> bufferedGifs;
	private ConcurrentHashMap<String, LinkSpan> bufferedClickSpannables;
	private float smileyScale;
	private Object gifLock;
	private ArrayList<String> bufferedUpdateListeners;


	private ConcurrentHashMap<String, AnimatedImageSpan> bufferedAnimatedImageSpannables;


	
	private CommonData(){
		messages = new ArrayList<Message>();
		waitTime = 7000;
		sdf = new SimpleDateFormat("dd.MM.yyyy | HH:mm");
		setContext(null);
		smileyScale = 1.6f;
		setBufferedGifs(new ConcurrentHashMap<Integer, AnimatedGifDrawable>());
		gifLock = new Object();
		bufferedUpdateListeners = new ArrayList<String>();
		bufferedAnimatedImageSpannables = new ConcurrentHashMap<String, AnimatedImageSpan>();
		bufferedClickSpannables = new ConcurrentHashMap<String, LinkSpan>();
	}
	
	
	
	public static CommonData getInstance(){
		if(instance == null){
			instance = new CommonData();
		}
		
		return instance;
	}

	
	public ArrayList<Message> getMessages(){
		return messages;
	}
	
	
	public void addMessages(ArrayList<Message> msgs){
		this.messages.addAll(msgs);
	}

	public long getWaitTime() {
		return waitTime;
	}



	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}



	public SimpleDateFormat getSimpleDateFormat() {
		return sdf;
	}



	public void setSimpleDateFormat(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}



	public Context getContext() {
		return context;
	}



	public void setContext(Context context) {
		this.context = context;
	}



	public float getSmileyScale() {
		return this.smileyScale;
	}

	public Object getGifLock(){
		return this.gifLock;
	}


	public ConcurrentHashMap<Integer, AnimatedGifDrawable> getBufferedGifs() {
		return bufferedGifs;
	}



	public void setBufferedGifs(ConcurrentHashMap<Integer, AnimatedGifDrawable> concurrentHashMap) {
		this.bufferedGifs = concurrentHashMap;
	}



	public ArrayList<String> getBufferedListeners() {
		return bufferedUpdateListeners;
	}



	public void setBufferedListeners(ArrayList<String> bufferedUpdateListeners) {
		this.bufferedUpdateListeners = bufferedUpdateListeners;
	}



	public ConcurrentHashMap<String, AnimatedImageSpan> getBufferedAnimatedImageSpannables() {
		return this.bufferedAnimatedImageSpannables;
	}



	public ConcurrentHashMap<String, LinkSpan> getBufferedClickSpannables() {
		return bufferedClickSpannables;
	}



	public void setBufferedClickSpannables(ConcurrentHashMap<String, LinkSpan> bufferedClickSpannables) {
		this.bufferedClickSpannables = bufferedClickSpannables;
	}

}
