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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class BandwidthActivity extends Activity {
	String[] data;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bandwidth_monitor);
		mContext = this;
		if (isOnline()) {
			new DownloadDataAsync().execute("");           
		} else {
			Toast.makeText(this, "No Network Connection Available",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please Wait");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}

	}

	public void updateDisplay() {
		((TextView) findViewById(R.id.sent_bandwidth)).setText(data[0]);
		((TextView) findViewById(R.id.received_bandwidth)).setText(data[1]);
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

	class DownloadDataAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			updateDisplay();
		}

		@Override
		protected String doInBackground(String... arg0) {
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
				Toast.makeText(mContext, "Parser Error", Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			} catch (MalformedURLException e) {
				Toast.makeText(mContext, "Error Fetching Data",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(mContext, "I/O Exception!", Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			}
			return null;
		}
	}
}
