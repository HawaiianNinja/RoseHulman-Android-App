package awesome.app.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Base64;
import awesome.app.R;
import awesome.app.connectivity.NetworkManager;

public class PasswordManager {

	ISharedPreferences mPrefs;

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	static final String DEFAULTPASSWORD = ".......";
	private String mUsername;
	private String mPassword;
	private Context mContext;

	public PasswordManager(Context context) {
		this(new SharedPreferencesAdapter(context), context);
	}

	public PasswordManager(ISharedPreferences pref, Context context) {
		mPrefs = pref;
		mContext = context;
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

	public boolean update(String username, String password) {
		mUsername = username;
		if (!password.equals(DEFAULTPASSWORD)) {
			mPassword = Base64.encodeToString(password.getBytes(),
					Base64.DEFAULT);
		}
		return checkCreds();
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

	public boolean checkCreds() {
		List<NameValuePair> creds = new ArrayList<NameValuePair>();
		creds.add(new BasicNameValuePair(mContext
				.getString(R.string.bandwidthUsernameVariableName), mUsername));
		creds.add(new BasicNameValuePair(mContext
				.getString(R.string.bandwidthPasswordVariableName), mPassword));
		String url = mContext.getString(R.string.serverURL) + mContext.getString(R.string.authCheckURL);
		return NetworkManager.checkCreds(url, creds);
	}

}
