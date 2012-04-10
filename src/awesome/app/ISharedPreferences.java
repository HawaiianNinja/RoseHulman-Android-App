package awesome.app;

public interface ISharedPreferences {
	public void putString(String key, String value);
	public String getString(String key, String defaultValue);
}
