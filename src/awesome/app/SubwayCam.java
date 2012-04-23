package awesome.app;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SubwayCam extends Activity {
	private ImageView mImage;
	private Context mContext;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private Bitmap mBitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subway_cam);
		mContext = this;
		mImage = (ImageView) findViewById(R.id.subwayImage);
		((Button) findViewById(R.id.refreshButton)).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				refreshData();
			}
		});
		refreshData();
		mImage.setImageBitmap(mBitmap);
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

	class DownloadDataAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			if (mBitmap == null) {
				Toast.makeText(mContext, "Error Retrieving Image", Toast.LENGTH_SHORT).show();
			} else {
				updateDisplay();
			}
		}

		private void updateDisplay() {
			mImage.setImageBitmap(mBitmap);
		}

		@Override
		protected String doInBackground(String... arg0) {
			try {
				URL url = new URL("http://subway-cam.rose-hulman.edu/snapshot1.jpg");
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream is = connection.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				mBitmap = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
