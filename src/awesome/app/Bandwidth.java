package awesome.app;

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

public class Bandwidth extends Activity {
	BandwidthHandler handler;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bandwidth_monitor);
		mContext = this;
		if (isOnline()) {
			new DownloadDataAsync().execute("");
		} else {
			Toast.makeText(this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
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
		((TextView) findViewById(R.id.sent_bandwidth)).setText(handler.getSentAmount());
		((TextView) findViewById(R.id.received_bandwidth)).setText(handler.getReceivedAmount());
		((TextView) findViewById(R.id.linkToIAIT)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(handler.getAddress()));
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
			String url = getString(R.string.serverURL) + getString(R.string.bandwidthAddress);
			handler = (BandwidthHandler) NetworkManager.getData(url, new BandwidthHandler(), mContext);
			return null;
		}
	}
}
