package awesome.app;

import android.content.Context;

public class PasswordManager {

	ISharedPreferences mPrefs;
	
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";

	public PasswordManager(Context context) {
		this(new SharedPreferencesAdapter(context));
	}

	public PasswordManager(ISharedPreferences pref) {
		mPrefs = pref;
	}
	
	public String getDefaultUsername(){
		return mPrefs.getString(USERNAME, "");
	}
	
	public String getDefaultPassword(){
		return mPrefs.getString(PASSWORD, "");
	}

}
