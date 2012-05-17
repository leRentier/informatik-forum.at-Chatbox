package informatikforum.chatbox;

import java.util.List;

import informatikforum.chatbox.entity.Message;
import informatikforum.chatbox.utils.MessageFormatter;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class MessageArrayAdapter extends ArrayAdapter<Message>{
	
	@SuppressWarnings("unused")
	private Context context;
	private List<Message> messages;
	private CommonData cd;
	
	public MessageArrayAdapter(Context context, int textViewResourceId, List<Message> messages){
		super(context, textViewResourceId, messages);
		this.context = context;
		this.messages = messages;
		this.cd = CommonData.getInstance();
	}
	
	@Override
	public int getCount() {
		return this.messages.size();
	}

	@Override
	public Message getItem(int index) {
		return this.messages.get(index);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		View row = convertView;
		Message message = messages.get(position);
		
		if (row == null) {
			// Inflate rows
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_message_item, parent, false);
		}
		
		((TextView) row.findViewById(R.id.username)).setText(message.getUser());
		((TextView) row.findViewById(R.id.timestamp)).setText(cd.getSimpleDateFormat().format(message.getTimeStamp()));

		((TextView) row.findViewById(R.id.message)).setText(MessageFormatter.getAndroidRepresentationForMessageText(message, ((TextView) row.findViewById(R.id.message))), BufferType.SPANNABLE);
		((TextView) row.findViewById(R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

		//Linkify.addLinks(((TextView) row.findViewById(R.id.message)), Linkify.ALL);


		return row;
	}
}
