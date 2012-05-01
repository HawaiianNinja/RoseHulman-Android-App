package awesome.app.security;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesAdapter implements ISharedPreferences {
	private SharedPreferences mPrefs;
	private static final String PREFS = "MiniGameMadnessPreferences";

	public SharedPreferencesAdapter(Context context) {
		mPrefs = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key, String defaultValue) {
		return mPrefs.getString(key, defaultValue);
	}

	public void clear() {
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.clear();
		editor.commit();
	}

}
