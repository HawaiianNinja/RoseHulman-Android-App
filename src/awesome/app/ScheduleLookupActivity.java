package awesome.app;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleLookupActivity extends Activity {

	private String currentStudent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.schedule_lookup);

		// // Button Method
		// Button button = (Button) findViewById(R.id.schedule_lookup_button);
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// String searchString = ((EditText) findViewById(R.id.schedule_text))
		// .getText().toString();
		// if (searchString.equals("")) {
		// Toast.makeText(
		// getApplicationContext(),
		// "You have to put something in the textbox in order to search!",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// doScheduleSearch(searchString);
		// }
		// }
		// });
	}

	private void doScheduleSearch(String searchString) {
		if (searchString == currentStudent)
			return;
		currentStudent = searchString;
		clearTable();
		ArrayList<ScheduleData> classList = getClassList(searchString);
		if (classList.size() > 0) {
			String[] days = { "M", "T", "W", "R", "F" };
			for (String day : days) {
				TableRow currentRow = new TableRow(this);
				if (day == "M") {
					currentRow = (TableRow) findViewById(R.id.mondayRow);
				} else if (day == "T") {
					currentRow = (TableRow) findViewById(R.id.tuesdayRow);
				} else if (day == "W") {
					currentRow = (TableRow) findViewById(R.id.wednesdayRow);
				} else if (day == "R") {
					currentRow = (TableRow) findViewById(R.id.thursdayRow);
				} else if (day == "F") {
					currentRow = (TableRow) findViewById(R.id.fridayRow);
				}

				for (int period = 1; period <= 10; period++) {
					TextView textView = new TextView(this);
					textView.setPadding(8, 3, 8, 3);
					textView.setBackgroundDrawable((Drawable) getResources()
							.getDrawable(R.drawable.cell_border));
					textView.setTextSize(18);
					for (ScheduleData eachClass : classList) {
						if (eachClass.MeetsOn(day)
								&& eachClass.MeetingDuringPeriod(period)) {
							textView.setText(eachClass.className);
						}
					}
					currentRow.addView(textView);
				}
			}
		} else {
			Toast.makeText(this, "No classes found.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private ArrayList<ScheduleData> getClassList(String username) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(getString(R.string.serverURL)
				+ getString(R.string.scheduleSearchURL));

		// Search Parameters
		String fieldName = getString(R.string.fieldNameLookup);
		String quarterName = getString(R.string.quarterNameLookup);
		String quarterSelected = getString(R.string.quarterSelectionLookup);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(fieldName, username));
		pairs.add(new BasicNameValuePair(quarterName, quarterSelected));

		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(post);
			HttpEntity resultEntity = response.getEntity();
			String results = EntityUtils.toString(resultEntity);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			ScheduleHandler scheduleHandler = new ScheduleHandler();
			xr.setContentHandler(scheduleHandler);
			xr.parse(new InputSource(new StringReader(results)));
			return scheduleHandler.getClassList();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(this, "Parser Error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (MalformedURLException e) {
			Toast.makeText(this, "Error Fetching Menu", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "I/O Exception!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (NullPointerException e) {
			Toast.makeText(this, "Null Pointer Exception!", Toast.LENGTH_SHORT)
					.show();
		}
		return null;
	}

	private void clearTable() {
		TableRow mondayRow = (TableRow) findViewById(R.id.mondayRow);
		mondayRow.removeAllViews();
		TableRow fridayRow = (TableRow) findViewById(R.id.fridayRow);
		fridayRow.removeAllViews();
		TableRow thursdayRow = (TableRow) findViewById(R.id.thursdayRow);
		thursdayRow.removeAllViews();
		TableRow wednesdayRow = (TableRow) findViewById(R.id.wednesdayRow);
		wednesdayRow.removeAllViews();
		TableRow tuesdayRow = (TableRow) findViewById(R.id.tuesdayRow);
		tuesdayRow.removeAllViews();
		setContentView(R.layout.schedule_lookup);
	}
}