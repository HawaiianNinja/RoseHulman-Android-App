package awesome.app;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class Ara extends CallBackActivity implements SimpleGestureListener {
	private SimpleGestureFilter detector;
	private NetworkManager mNetworkManager;
	private AraHandler araHandler;
	private TextView dateTextView;
	private TextView breakfastEntreeTextView;
	private TextView lunchEntreeTextView;
	private TextView dinnerEntreeTextView;
	private View breakfastLayout;
	private View lunchLayout;
	private View dinnerLayout;
	private Calendar currentDateInDisplay;
	private Calendar actualDateToday;

	static final int DATE_DIALOG_ID = 0;
	static final int BACK_TODAY = 1;
	static final int MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			currentDateInDisplay.set(Calendar.YEAR, year);
			currentDateInDisplay.set(Calendar.MONTH, monthOfYear);
			currentDateInDisplay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			showARAMenuFromDate(currentDateInDisplay.getTime());
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ara);
		detector = new SimpleGestureFilter(this, this);
		currentDateInDisplay = Calendar.getInstance();
		actualDateToday = Calendar.getInstance();
		araHandler = new AraHandler();
		mNetworkManager = new NetworkManager(getString(R.string.serverURL) + getString(R.string.menuServerPage),
				araHandler, this);
		clearMenu();
		showMenuFromDisplayDate();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	public void onSwipe(int direction) {
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			// if (getDifferenceBetweenDates() > -1) { //removed as discussed during last meeting
				currentDateInDisplay.add(Calendar.DATE, -1);
				showMenuFromDisplayDate();
			//}
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			// if (getDifferenceBetweenDates() < 2) { //removed as discussed during last meeting
				currentDateInDisplay.add(Calendar.DATE, 1);
				showMenuFromDisplayDate();
			//}
			break;
		default:
			break;
		}
	}

	public void onDoubleTap() {
	}

	private Date getDisplayDate() {
		return currentDateInDisplay.getTime();
	}

	private void showMenuFromDisplayDate() {
		showARAMenuFromDate(getDisplayDate());
	}

	private void showARAMenuFromDate(Date date) {
		if (NetworkManager.isOnline(this)) {
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
		loadViews();
		breakfastLayout.setVisibility(View.GONE);
		lunchLayout.setVisibility(View.GONE);
		dinnerLayout.setVisibility(View.GONE);
		breakfastEntreeTextView.setVisibility(View.GONE);
		lunchEntreeTextView.setVisibility(View.GONE);
		dinnerEntreeTextView.setVisibility(View.GONE);
	}

	public void update() {
		MenuData menu = araHandler.getMenu();
		Date date = getDisplayDate();
		Format displayFormatter = new SimpleDateFormat("EEEE, MM/dd/yyyy");
		clearMenu();
		dateTextView.setText(displayFormatter.format(date));
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

	private void loadViews() {
		dateTextView = ((TextView) findViewById(R.id.titleTextView));
		breakfastEntreeTextView = ((TextView) findViewById(R.id.breakfastTextView));
		lunchEntreeTextView = ((TextView) findViewById(R.id.lunchTextView));
		dinnerEntreeTextView = ((TextView) findViewById(R.id.dinnerTextView));
		breakfastLayout = (View) findViewById(R.id.breakfastLayout);
		lunchLayout = (View) findViewById(R.id.lunchLayout);
		dinnerLayout = (View) findViewById(R.id.dinnerLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ara_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.go_to_date:
			showDialog(DATE_DIALOG_ID);
			return true;
		case R.id.go_to_today:
			currentDateInDisplay = (Calendar) actualDateToday.clone();
			showARAMenuFromDate(actualDateToday.getTime());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, actualDateToday.get(Calendar.YEAR),
					actualDateToday.get(Calendar.MONTH), actualDateToday.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}

	/*  //removed as discussed during last meeting
	private int getDifferenceBetweenDates() {
		return getDifferenceBetweenDates(actualDateToday, currentDateInDisplay);
	}

	private int getDifferenceBetweenDates(Calendar startDate, Calendar endDate) {
		long endL = endDate.getTimeInMillis() + endDate.getTimeZone().getOffset(endDate.getTimeInMillis());
		long startL = startDate.getTimeInMillis() + startDate.getTimeZone().getOffset(startDate.getTimeInMillis());
		return (int) ((endL - startL) / MILLISECS_PER_DAY);
	}
	*/
}
