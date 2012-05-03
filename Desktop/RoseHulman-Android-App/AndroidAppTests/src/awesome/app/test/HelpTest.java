package awesome.app.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import awesome.app.activity.HelpActivity;

public class HelpTest extends ActivityInstrumentationTestCase2<HelpActivity> {

	public HelpTest() {
		super("awesome.app", HelpActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();		
	}
	
	public void testButtonsWorkBundle() {
		final HelpActivity hActivity = getActivity();
		int scheduleId = awesome.app.R.id.scheduleLookupTextView;
		int studentId = awesome.app.R.id.studentLookupTextView;
		int feedbackId = awesome.app.R.id.feedbackTextView;
		int bandwidthId = awesome.app.R.id.bandwidthMonitorTextView;
		int araMenuId = awesome.app.R.id.araMenuTextView;
		hActivity.makeButtonWork(scheduleId, awesome.app.R.string.scheduleHelpString);
		TextView separator = (TextView) hActivity.findViewById(awesome.app.R.id.helpSeparatorTextView);
		
		
		hActivity.makeButtonWork(studentId, awesome.app.R.string.studentHelpString);		
		hActivity.makeButtonWork(feedbackId, awesome.app.R.string.feedbackHelpString);
		hActivity.makeButtonWork(bandwidthId, awesome.app.R.string.bandwidthHelpString);
		hActivity.makeButtonWork(araMenuId, awesome.app.R.string.araHelpString);
		
		//Schedule Click Test
		hActivity.runOnUiThread(new Runnable() {
			public void run() {
				TextView scheduleTextView = (TextView) hActivity.findViewById(awesome.app.R.id.scheduleLookupTextView);
				scheduleTextView.performClick();
			}
		});				
		assertEquals("Schedule Lookup Help", separator.getText(), "");
		
		//Student Click Test
		hActivity.runOnUiThread(new Runnable() {
			public void run() {
				TextView studentTextView = (TextView) hActivity.findViewById(awesome.app.R.id.studentLookupTextView);
				studentTextView.performClick();
			}
		});
		assertEquals("Student Lookup Help", separator.getText(), "");
		
		//Feedback Click Test
		hActivity.runOnUiThread(new Runnable() {
			public void run() {
				TextView feedbackTextView = (TextView) hActivity.findViewById(awesome.app.R.id.feedbackTextView);
				feedbackTextView.performClick();
			}
		});
		assertEquals("Feedback Help", separator.getText(), "");
		
		//Bandwidth Click Test
		hActivity.runOnUiThread(new Runnable() {
			public void run() {
				TextView bandwidthTextView = (TextView) hActivity.findViewById(awesome.app.R.id.bandwidthMonitorTextView);
				bandwidthTextView.performClick();
			}
		});
		assertEquals("Bandwidth Monitor Help", separator.getText(), "");
		
		//ARA click test
		hActivity.runOnUiThread(new Runnable() {
			public void run() {
				TextView araTextView = (TextView) hActivity.findViewById(awesome.app.R.id.araMenuTextView);
				araTextView.performClick();
			}
		});
		assertEquals("ARA Menu Help", separator.getText(), "");
	}
}