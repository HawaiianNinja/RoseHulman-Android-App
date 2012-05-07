package awesome.app.test;

import org.junit.Test;

import junit.framework.TestCase;
import awesome.app.security.PasswordManager;
import awesome.app.security.ISharedPreferences;
import static org.easymock.EasyMock.*;

public class PasswordManagerTest extends TestCase {
	
	@Test
	public void testLoadBlankCrediantials(){
		ISharedPreferences pref = createMock(ISharedPreferences.class);
		expect(pref.getString(PasswordManager.USERNAME, "")).andReturn("");
		expect(pref.getString(PasswordManager.PASSWORD, "")).andReturn("");
		replay(pref);
		PasswordManager manager = new PasswordManager(pref);
		assertEquals("", manager.getUsername());
		assertEquals("", manager.getPassword());
	}
}
