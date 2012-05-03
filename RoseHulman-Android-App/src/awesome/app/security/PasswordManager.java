package awesome.app.security;

import android.content.Context;
import android.util.Base64;

public class PasswordManager {

	ISharedPreferences mPrefs;

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	static final String DEFAULTPASSWORD = ".......";
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
		mPassword = mPrefs.getString(PASSWORD, "");
	}

	public String getUsername() {
		return mUsername;
	}

	public String getPassword() {
		return mPassword;
	}

	public String getPublicPassword() {
		if (infoExists()) {
			return DEFAULTPASSWORD;
		}
		return "";
	}

	public boolean getSavePassword() {
		return !mPrefs.getString(USERNAME, "").equals("");
	}

	public boolean infoExists() {
		return !mUsername.equals("");
	}

	public void update(String username, String password) {
		mUsername = username;
		if (!password.equals(DEFAULTPASSWORD)) {
			mPassword = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
		}
	}

	public void save() {
		mPrefs.putString(PASSWORD, mPassword);
		mPrefs.putString(USERNAME, mUsername);
	}

	public void clear() {
		mUsername = "";
		mPassword = "";
		mPrefs.clear();
	}

}
