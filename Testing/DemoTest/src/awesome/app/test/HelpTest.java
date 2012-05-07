package awesome.app.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import awesome.app.activity.HelpActivity;;

public class HelpTest extends ActivityInstrumentationTestCase2<HelpActivity> {

	public int scheduleId = awesome.app.R.id.scheduleLookupTextView;
	
	public HelpTest() {
		super("awesome.app.activity", HelpActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testButtonsWorkBundle() {
		final HelpActivity hActivity = getActivity();
		
		
//		hActivity.makeButtonWork(scheduleId, awesome.app.R.string.scheduleHelpString);
//		TextView separator = (TextView) hActivity.findViewById(awesome.app.R.id.helpSeparatorTextView);
//		TextView scheduleTextView = (TextView) hActivity.findViewById(awesome.app.R.id.scheduleLookupTextView);
//		scheduleTextView.performClick();
//					
//		assertEquals("Schedule Lookup Help", separator.getText(), "");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		hActivity.makeButtonWork(scheduleId, awesome.app.R.string.scheduleHelpString);
		TextView separator = (TextView) hActivity.findViewById(awesome.app.R.id.helpSeparatorTextView);
		
		//Schedule Help Test
		hActivity.runOnUiThread(new Runnable() {
			public void run() {
				TextView scheduleTextView = (TextView) hActivity.findViewById(awesome.app.R.id.scheduleLookupTextView);
				scheduleTextView.performClick();
			}
		});				
		assertEquals("Schedule Lookup Help", separator.getText(), "");
	}

}
