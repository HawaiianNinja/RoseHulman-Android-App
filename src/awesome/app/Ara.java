package awesome.app;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Ara extends Activity implements SimpleGestureListener, ICallbackable {
	private SimpleGestureFilter detector;
	private int dayOffset;
	private NetworkManager mNetworkManager;
	private AraHandler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ara);
		detector = new SimpleGestureFilter(this, this);
		dayOffset = 0;
		mHandler = new AraHandler();
		mNetworkManager = new NetworkManager(getString(R.string.serverURL) + getString(R.string.menuServerPage), mHandler, this);
		clearMenu();
		changeDate();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	public void onSwipe(int direction) {
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			if (dayOffset > -1) {
				dayOffset--;
				changeDate();
			}
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			if (dayOffset < 2) {
				dayOffset++;
				changeDate();
			}
			break;
		default:
			break;
		}
	}

	public void onDoubleTap() {
	}

	private void changeDate() {
		if (NetworkManager.isOnline(this)) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, dayOffset);
			Date date = calendar.getTime();
			Format serverFormatter = new SimpleDateFormat("MM_dd_yyyy");
			String formattedDate = serverFormatter.format(date);
			String fieldName = getString(R.string.menuDateVariableName);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair(fieldName, formattedDate));
			
			mNetworkManager.getData(pairs);
		} else {
			Toast.makeText(this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
		}
	}

	private void clearMenu() {
		TextView breakfastEntreeTextView = ((TextView) findViewById(R.id.breakfastTextView));
		TextView lunchEntreeTextView = ((TextView) findViewById(R.id.lunchTextView));
		TextView dinnerEntreeTextView = ((TextView) findViewById(R.id.dinnerTextView));
		View breakfastLayout = (View) findViewById(R.id.breakfastLayout);
		View lunchLayout = (View) findViewById(R.id.lunchLayout);
		View dinnerLayout = (View) findViewById(R.id.dinnerLayout);
		breakfastLayout.setVisibility(View.GONE);
		lunchLayout.setVisibility(View.GONE);
		dinnerLayout.setVisibility(View.GONE);
		breakfastEntreeTextView.setVisibility(View.GONE);
		lunchEntreeTextView.setVisibility(View.GONE);
		dinnerEntreeTextView.setVisibility(View.GONE);
	}

	public void update() {
		MenuData menu = mHandler.getMenu();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, dayOffset);
		Date date = calendar.getTime();
		Format displayFormatter = new SimpleDateFormat("EEEE, MM/dd/yyyy");
		TextView dateTextView = ((TextView) findViewById(R.id.titleTextView));
		TextView breakfastEntreeTextView = ((TextView) findViewById(R.id.breakfastTextView));
		TextView lunchEntreeTextView = ((TextView) findViewById(R.id.lunchTextView));
		TextView dinnerEntreeTextView = ((TextView) findViewById(R.id.dinnerTextView));
		View breakfastLayout = (View) findViewById(R.id.breakfastLayout);
		View lunchLayout = (View) findViewById(R.id.lunchLayout);
		View dinnerLayout = (View) findViewById(R.id.dinnerLayout);
		dateTextView.setText(displayFormatter.format(date));
		clearMenu();
		String outputString = "";
		breakfastEntreeTextView.setText(outputString);
		lunchEntreeTextView.setText(outputString);
		dinnerEntreeTextView.setText(outputString);
		if (menu.breakfastEntrees != null) {
			for (String item : menu.breakfastEntrees) {
				outputString += item + "\n";
			}
			breakfastLayout.setVisibility(View.VISIBLE);
			breakfastEntreeTextView.setVisibility(View.VISIBLE);
		}
		breakfastEntreeTextView.setText(outputString);
		outputString = "";
		if (menu.lunchEntrees != null) {
			for (String item : menu.lunchEntrees) {
				outputString += item + "\n";
			}
			lunchLayout.setVisibility(View.VISIBLE);
			lunchEntreeTextView.setVisibility(View.VISIBLE);
		}
		lunchEntreeTextView.setText(outputString);
		outputString = "";
		if (menu.dinnerEntrees != null) {
			for (String item : menu.dinnerEntrees) {
				outputString += item + "\n";
			}
			dinnerLayout.setVisibility(View.VISIBLE);
			dinnerEntreeTextView.setVisibility(View.VISIBLE);
		}
		dinnerEntreeTextView.setText(outputString);
	}
}
