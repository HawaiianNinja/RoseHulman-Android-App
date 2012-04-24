package awesome.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Bandwidth extends Activity {
	BandwidthHandler mHandler;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private Context mContext;
	private String mUsername;
	private String mPassword;
	private TextView mSentLabel;
	private TextView mReceivedLabel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bandwidth_monitor);
		mUsername = getIntent().getStringExtra(PasswordManager.USERNAME);
		mPassword = getIntent().getStringExtra(PasswordManager.PASSWORD);
		mContext = this;
		mHandler = new BandwidthHandler();
		mSentLabel = ((TextView) findViewById(R.id.sentLabel));
		mReceivedLabel = ((TextView) findViewById(R.id.receivedLabel));
		mReceivedLabel.setVisibility(View.GONE);
		mSentLabel.setVisibility(View.GONE);
		((Button) findViewById(R.id.refreshButton)).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				refreshData();
			}
		});
		refreshData();
	}

	public void refreshData() {
		if (NetworkManager.isOnline(this)) {
			new DownloadDataAsync().execute("");
		} else {
			Toast.makeText(this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Please Wait");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
			return progressDialog;
		default:
			return null;
		}
	}

	public void updateDisplay() {
		mReceivedLabel.setVisibility(View.VISIBLE);
		mSentLabel.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.sent_bandwidth)).setText(mHandler.getSentAmount());
		((TextView) findViewById(R.id.received_bandwidth)).setText(mHandler.getReceivedAmount());
		((TextView) findViewById(R.id.linkToIAIT)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(mHandler.getAddress()));
				startActivity(i);
			}
		});
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
			if (mHandler.isError()) {
				Toast.makeText(mContext, "Error Retrieving Bandwidth Data", Toast.LENGTH_SHORT).show();
			} else {
				updateDisplay();
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair(getString(R.string.bandwidthUsernameVariableName), mUsername));
			pairs.add(new BasicNameValuePair(getString(R.string.bandwidthPasswordVariableName), mPassword));
			String url = getString(R.string.serverURL) + getString(R.string.bandwidthAddress);
			NetworkManager.getData(url, mHandler, pairs, mContext);
			return null;
		}
	}
}
