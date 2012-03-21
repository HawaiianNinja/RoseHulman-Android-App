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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ScheduleLookupActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_lookup);

		// Button Method
		Button button = (Button) findViewById(R.id.schedule_lookup_button);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String searchString = ((EditText) findViewById(R.id.schedule_text))
						.getText().toString();
				if (searchString.equals("")) {
					Toast.makeText(
							getApplicationContext(),
							"You have to put something in the textbox in order to search!",
							Toast.LENGTH_SHORT).show();
				} else {
					doScheduleSearch(searchString);
				}
			}
		});
	}

	private void doScheduleSearch(String searchString) {
		ArrayList<ScheduleData> classList = getClassList(searchString);
		if (classList != null) {
			// TODO Render Table Here
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
		}
		return null;
	}
}