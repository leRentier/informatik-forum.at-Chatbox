package informatikforum.chatbox.gui;

import informatikforum.chatbox.ClientCallback;
import informatikforum.chatbox.MessageRetrieveService;
import informatikforum.chatbox.R;
import informatikforum.chatbox.business.BusinessLogic;
import informatikforum.chatbox.business.BusinessLogicException;
import informatikforum.chatbox.business.CommonData;
import informatikforum.chatbox.business.Informant;
import informatikforum.chatbox.entity.Message;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatboxActivity extends Activity implements ClientCallback{

	private BusinessLogic bl;
	private Informant informant;
	private ListView lv;
	private MessageArrayAdapter maa;
	private final String TAG = "ChatboxActivity";
	private CommonData cd;
	private boolean running = false;
	private int newMessageIndex;
	private  EditText messageInputField;

	private ChatboxActivity _this;
	
	final Handler handler = new Handler() {
		
		@Override
        public void handleMessage(android.os.Message msg) {
        	Log.d(TAG, "Handler retrieved a message!");
           updateMessages();
        }
    };

    final Handler linkHandler = new Handler() {
		
		@Override
        public void handleMessage(android.os.Message msg) {
			String url = msg.getData().getString("url");
			Log.d(TAG, "Opening URL: " + url);
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(browserIntent);		
        }
    };
    
    /**
     * Called when the activity is created.
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		_this = this;
		
		ArrayList<Message> messagesAtStart;
        final Button buttonSend;
    	
    	// Call superconstructor and set generated Layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Retrieve the Informant-instance and CommonData-instance.
        this.informant = Informant.getInstance();
        this.cd = CommonData.getInstance();
        
        // Set the context in CommonData
        this.cd.setContext(this);
        
        // Set username and password in the CommonData-instance
        // TODO should not be here.

	    // Retrieve the BusinessLogic-instance.
		this.bl = BusinessLogic.getInstance();
		this.bl.updateContext(this.getApplicationContext());
        this.bl.registerLinkHandler(linkHandler);
        
        // Register to informant.
        this.informant.registerClientCallback(this);
        
        // Starting MessageRetrieveService. Notice, that this service self checks,
        // whether it is already running or not.
        Log.d(TAG, "Starting MessageRetrieveService.");
        startService(new Intent(this, MessageRetrieveService.class));
        
       messagesAtStart =  new ArrayList<Message>(this.cd.getMessages());
       this.newMessageIndex = messagesAtStart.size();
        
        // Create a new MessageArrayAdapter initially containing the messages
        // from the common data. Also initialize a new ListView, and set the adapter.
        // It is important to create a new ArrayList, as later adding from the list to self won't be a good idea..
        maa = new MessageArrayAdapter(this, R.layout.list_message_item, messagesAtStart);
	    lv = (ListView)findViewById(R.id.list_message);
	    lv.setAdapter(maa);
        
	    // Create Input-field and buttons.
        messageInputField = (EditText) findViewById(R.id.message_input_field);
        buttonSend = (Button) findViewById(R.id.button_send);
        
        // Set up the Button's functionality.
        buttonSend.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		new PostMessageTask().execute(messageInputField.getText().toString());
        		messageInputField.setText("");
        	}

        });
        
    	if(!bl.isConfigured()) {
    		AlertDialog alertDialog = new AlertDialog.Builder(_this).create();
    		alertDialog.setTitle("Welcome");
    		alertDialog.setMessage("This is the first time you run this app. You will have to configure it in order to use it.");
    		alertDialog.setButton("Ok", new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    				Intent intent = new Intent(_this, Settings.class);
    				startActivity(intent);
				}
    		});
    		alertDialog.show();
    	}
    	
        // Set client to be running
        this.running = true;
    }    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.main_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.main_exit:
				// TODO
				break;
				
			case R.id.main_settings:
				Intent intent = new Intent(this, Settings.class);
				startActivity(intent);
				break;
		}
		
		return true;
	}
   
    @Override
    protected void onPause(){
    	super.onPause();
    	
    	this.running = false;
    }

    
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	this.running = true;
    }
    
    
    /**
     * Thread for posting messages
     * As posting takes quite a lot of time this Thread is
     * needed for not blocking the UI.
     */
    private class PostMessageTask extends AsyncTask<String, Void, String>{
    	
    	private Exception ex = null;
    	
    	
    	@Override
    	protected String doInBackground(String... msg){
	    	try {
				bl.postMessage(msg[0]);
			} catch (BusinessLogicException e) {
				ex = e;
			}
	    	catch(Exception e){
	    		ex = new Exception(getString(R.string.error_posting_message), e);
	    	}
	    	
	    	// Pass the message on, so that if an error happened, we can set the input-text back.
	    	return msg[0];
	    }
    	
    	@Override
    	protected void onPostExecute(String in){
    		if(ex != null){
    			informant.clientRaiseNormalError(ex);
    			messageInputField.setText(in);
    		}
    	}
    }

    private void updateMessages(){
		ArrayList<Message> allMessages = cd.getMessages();
		for(int i=this.newMessageIndex; i<allMessages.size(); i++){
			maa.add(allMessages.get(i));
		}

		
		this.newMessageIndex = allMessages.size();
    }

	@Override
	public void clientNotifyMessageUpdate() {
		Log.d(TAG, "ClientNotifyMessageUpdate() was called here.. Create a new Message..");
		android.os.Message msg = handler.obtainMessage();
		
		Log.d(TAG, "Sending this message to the handler.");
		handler.sendMessage(msg);
	}

	@Override
	public void clientRaiseNormalError(Exception e) {
    	AlertDialog alertDialog;
    	alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle(getString(R.string.error));
    	alertDialog.setMessage(e.getMessage());
    	alertDialog.show();
	}

	@Override
	public boolean clientIsRunning() {
		return this.running;
	}
}
