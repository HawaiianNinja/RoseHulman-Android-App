package awesome.app;

import android.content.Context;
import android.util.Base64;

public class PasswordManager {

	ISharedPreferences mPrefs;
	
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";
	private String mUsername;
	private String mPassword;

	public PasswordManager(Context context) {
		this(new SharedPreferencesAdapter(context));
	}

	public PasswordManager(ISharedPreferences pref) {
		mPrefs = pref;
		loadSettings();
	}
	
	private void loadSettings() {
		mUsername = mPrefs.getString(USERNAME, "");
		mPassword = mPrefs.getString(PASSWORD, "");;
	}

	public String getUsername(){
		return mUsername;
	}
	
	public String getPassword(){
		return mPassword;
	}
	
	public boolean infoExists(){
		return !mUsername.equals("");
	}
	
	public void update(String username, String password){
		mUsername = username;
		mPassword = password;
	}
	
	public void save(){
		mPrefs.putString(PASSWORD, Base64.encodeToString(mPassword.getBytes(), Base64.DEFAULT));
		mPrefs.putString(USERNAME, mUsername);
	}
	
	public void clear(){
		mUsername = "";
		mPassword = "";
		mPrefs.clear();
	}

}
