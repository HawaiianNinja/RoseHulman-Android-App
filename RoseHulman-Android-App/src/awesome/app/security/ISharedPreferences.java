package awesome.app.security;

public interface ISharedPreferences {
	public void putString(String key, String value);
	public String getString(String key, String defaultValue);
	public void clear();
}
