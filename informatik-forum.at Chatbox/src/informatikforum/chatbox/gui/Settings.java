package informatikforum.chatbox.gui;


import informatikforum.chatbox.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		Editor editor = preferences.edit();
		editor.putBoolean("configured", true);
		editor.commit();
	}
}
