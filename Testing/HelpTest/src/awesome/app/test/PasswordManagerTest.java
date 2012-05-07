package awesome.app.test;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import awesome.app.security.PasswordManager;
import awesome.app.security.ISharedPreferences;
import static org.easymock.EasyMock.*;

public class PasswordManagerTest extends TestCase {
	public PasswordManager manager;
	public ISharedPreferences pref;
	
	@Before
	public void setUp(){
		pref = createMock(ISharedPreferences.class);
		manager = new PasswordManager(pref);
	}
	
	@Test
	public void testLoadBlankCrediantials(){
		expect(pref.getString(PasswordManager.USERNAME, "")).andReturn("");
		expect(pref.getString(PasswordManager.PASSWORD, "")).andReturn("");
		replay(pref);
		assertEquals("", manager.getUsername());
		assertEquals("", manager.getPassword());
	}
	
	@Test
	public void testUpdateWithoutSave(){
		expect(pref.getString(PasswordManager.USERNAME, "")).andReturn("");
		expect(pref.getString(PasswordManager.PASSWORD, "")).andReturn("");
		replay(pref);
		assertEquals("", manager.getUsername());
		assertEquals("", manager.getPassword());
	}
}
