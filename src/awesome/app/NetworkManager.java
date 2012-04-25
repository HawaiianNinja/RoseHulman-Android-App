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
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.Toast;

public class NetworkManager {

	private ProgressDialog mDialog;
	private CallBackActivity mActivity;
	private String mUrl;
	private DefaultHandler mHandler;
	private List<NameValuePair> mPairs;

	public NetworkManager(String url, DefaultHandler handler, CallBackActivity activity) {
		mActivity = activity;
		mUrl = url;
		mHandler = handler;
	}

	public void getData() {
		getData(new ArrayList<NameValuePair>());
	}

	public void getData(List<NameValuePair> pairs) {
		mPairs = pairs;
		new DownloadDataAsync().execute("");
	}
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			return cm.getActiveNetworkInfo().isAvailable();
		} catch (Exception e) {
			return false;
		}
	}

	class DownloadDataAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog = ProgressDialog.show(mActivity, "", "Loading. Please wait...", true);
		}

		@Override
		protected void onPostExecute(String unused) {
			mDialog.dismiss();
			mActivity.update();
			// if (mHandler.isError()) {
			// Toast.makeText(mContext, "Error Retrieving Bandwidth Data",
			// Toast.LENGTH_SHORT).show();
			// } else {
			// }
		}

		@Override
		protected String doInBackground(String... arg0) {
			try {
				// HttpClient client = new DefaultHttpClient();
				HttpClient client = SecurityHole.getNewHttpClient();
				HttpPost post = new HttpPost(mUrl);
				post.setEntity(new UrlEncodedFormEntity(mPairs));
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				String results = EntityUtils.toString(entity);
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				xr.setContentHandler(mHandler);
				xr.parse(new InputSource(new StringReader(results)));
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				Toast.makeText(mActivity, "Parser Error", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (MalformedURLException e) {
				Toast.makeText(mActivity, "Error Fetching Data", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(mActivity, "I/O Exception!", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			return null;
		}
	}
}
