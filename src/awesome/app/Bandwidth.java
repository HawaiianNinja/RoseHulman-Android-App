package awesome.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Bandwidth extends CallBackActivity{
	BandwidthHandler mHandler;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private String mUsername;
	private String mPassword;
	private TextView mSentLabel;
	private TextView mReceivedLabel;
	private NetworkManager mNetworkManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bandwidth_monitor);
		mUsername = getIntent().getStringExtra(PasswordManager.USERNAME);
		mPassword = getIntent().getStringExtra(PasswordManager.PASSWORD);
		mHandler = new BandwidthHandler();
		mSentLabel = ((TextView) findViewById(R.id.sentLabel));
		mReceivedLabel = ((TextView) findViewById(R.id.receivedLabel));
		mReceivedLabel.setVisibility(View.GONE);
		mSentLabel.setVisibility(View.GONE);
		mNetworkManager = new NetworkManager(getString(R.string.serverURL) + getString(R.string.bandwidthAddress), mHandler, this);
		((Button) findViewById(R.id.refreshButton)).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				refreshData();
			}
		});
		refreshData();
	}

	public void refreshData() {
		if (NetworkManager.isOnline(this)) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair(getString(R.string.bandwidthUsernameVariableName), mUsername));
			pairs.add(new BasicNameValuePair(getString(R.string.bandwidthPasswordVariableName), mPassword));
			mNetworkManager.getData(pairs);
		} else {
			Toast.makeText(this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
		}
	}

	public void update() {
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
}
