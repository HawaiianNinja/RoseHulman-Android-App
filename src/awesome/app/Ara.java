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
import android.widget.TextView;
import android.widget.Toast;

public class Ara extends Activity implements SimpleGestureListener {
	private SimpleGestureFilter detector;
	private int dayOffset;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ara);
		detector = new SimpleGestureFilter(this, this);
		dayOffset = 0;
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
			Format displayFormatter = new SimpleDateFormat("EEEE, MM/dd/yyyy");
			String formattedDate = serverFormatter.format(date);
			TextView dateTextView = ((TextView) findViewById(R.id.titleTextView));
			dateTextView.setText(displayFormatter.format(date));
			String url = getString(R.string.serverURL) + getString(R.string.menuServerPage);
			String fieldName = getString(R.string.menuDateVariableName);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair(fieldName, formattedDate));
			AraHandler handler = new AraHandler();
			NetworkManager.getData(url, handler, pairs, this);
			setDisplay(handler.getMenu());
		} else {
			Toast.makeText(this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
		}
	}

	private void setDisplay(MenuData menu) {
		TextView breakfastEntreeTextView = ((TextView) findViewById(R.id.breakfastTextView));
		TextView lunchEntreeTextView = ((TextView) findViewById(R.id.lunchTextView));
		TextView dinnerEntreeTextView = ((TextView) findViewById(R.id.dinnerTextView));
		View breakfastLayout = (View) findViewById(R.id.breakfastLayout);
		View lunchLayout = (View) findViewById(R.id.lunchLayout);
		View dinnerLayout = (View) findViewById(R.id.dinnerLayout);
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
}
