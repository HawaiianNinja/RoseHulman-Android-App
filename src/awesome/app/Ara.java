package awesome.app;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
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
		if (isOnline()) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, dayOffset);
			Date date = calendar.getTime();
			Format serverFormatter = new SimpleDateFormat("MM_dd_yyyy");
			Format displayFormatter = new SimpleDateFormat("EEEE, MM/dd/yyyy");
			String formattedDate = serverFormatter.format(date);
			TextView dateTextView = ((TextView) findViewById(R.id.titleTextView));
			dateTextView.setText(displayFormatter.format(date));
			SAXParserFactory spf = SAXParserFactory.newInstance();
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(getString(R.string.serverURL)
						+ getString(R.string.menuServerPage));
				String fieldName = getString(R.string.menuDateVariableName);
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair(fieldName, formattedDate));
				post.setEntity(new UrlEncodedFormEntity(pairs));
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				String results = EntityUtils.toString(entity);
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				AraHandler araHandler = new AraHandler();
				xr.setContentHandler(araHandler);
				xr.parse(new InputSource(new StringReader(results)));
				MenuData menu = araHandler.getMenu();
				setDisplay(menu);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				Toast.makeText(this, "Parser Error", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (MalformedURLException e) {
				Toast.makeText(this, "Error Fetching Menu", Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(this, "I/O Exception!", Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "No Network Connection Available",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void setDisplay(MenuData menu) {
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

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
}
