package awesome.app;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SubwayCam extends Activity {
	private ImageView mImage;
	private Context mContext;
	private Bitmap mBitmap;
	private ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.subway_cam);
		mContext = this;
		mImage = (ImageView) findViewById(R.id.subwayImage);
		((Button) findViewById(R.id.refreshButton))
				.setOnClickListener(new OnClickListener() {
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
			Toast.makeText(this, "No Network Connection Available",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void updateDisplay() {
		mImage.setImageBitmap(mBitmap);
	}

	class DownloadDataAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mDialog = ProgressDialog.show(mContext, "", mContext.getString(R.string.loading_dialog), true);
		}

		@Override
		protected void onPostExecute(String unused) {
			mDialog.dismiss();
			if (mBitmap == null) {
				Toast.makeText(mContext, "Error Retrieving Image",
						Toast.LENGTH_SHORT).show();
			} else {
				updateDisplay();
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			try {
				URL url = new URL(
						mContext.getString(R.string.serverURLUnsecure)
								+ mContext.getString(R.string.subwayURL));
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream is = connection.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				mBitmap = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
			} catch (Exception e) {
				// :)
			}
			return null;
		}
	}
}
