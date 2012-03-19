package awesome.app;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Bandwidth extends Activity {
	String[] data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bandwidth_monitor);
		if (isOnline()) {
			getBandwidthData();
			updateDisplay();
		} else {
			Toast.makeText(this, "No Network Connection Available",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void getBandwidthData() {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(getString(R.string.serverURL)
				+ getString(R.string.bandwidthAddress));
		SAXParserFactory spf = SAXParserFactory.newInstance();
		HttpResponse response = null;
		String results = "";
		try {
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			results = EntityUtils.toString(entity);
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			BandwidthHandler handler = new BandwidthHandler();
			xr.setContentHandler(handler);
			xr.parse(new InputSource(new StringReader(results)));
			data = handler.getValues();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(this, "Parser Error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (MalformedURLException e) {
			Toast.makeText(this, "Error Fetching Data", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "I/O Exception!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public void updateDisplay() {
		((TextView) findViewById(R.id.received_bandwidth)).setText(data[0]);
		((TextView) findViewById(R.id.sent_bandwidth)).setText(data[1]);
		((TextView) findViewById(R.id.linkToIAIT))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(data[2]));
						startActivity(i);
					}
				});
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
}
