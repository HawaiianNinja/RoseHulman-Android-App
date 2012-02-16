package awesome.app;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.os.Bundle;
import android.view.MotionEvent;
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
				Toast.makeText(this, "Offset is " + dayOffset,
						Toast.LENGTH_SHORT).show();
				changeDate();
			}
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			if (dayOffset < 2) {
				dayOffset++;
				Toast.makeText(this, "Offset is " + dayOffset,
						Toast.LENGTH_SHORT).show();
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
		Calendar date = Calendar.getInstance();
		String formattedDate;
		formattedDate = (date.get(Calendar.MONTH) + 1) + "_"
				+ (date.get(Calendar.DATE) + dayOffset) + "_"
				+ date.get(Calendar.YEAR);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(getString(R.string.serverURL)
					+ getString(R.string.menuServerPage));
			String fieldName = getString(R.string.menuDateVariableName);
			Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair(fieldName, formattedDate));
			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String results = EntityUtils.toString(entity);
			// URL url = new URL(getString(R.string.serverURL)
			// + getString(R.string.menuServerPage)) + "?"
			// + getString(R.string.menuDateVariableName) + "="
			// + formattedDate);
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			AraHandler araHandler = new AraHandler();
			xr.setContentHandler(araHandler);
			// xr.parse(new InputSource(url.openStream()));
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
			Toast.makeText(this, "I/O Exception!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	private void setDisplay(MenuData menu) {
		TextView breakfastEntreeTextView = ((TextView) findViewById(R.id.breakfastTextView));
		TextView lunchEntreeTextView = ((TextView) findViewById(R.id.lunchTextView));
		TextView dinnerEntreeTextView = ((TextView) findViewById(R.id.dinnerTextView));
		String outputString = "";
		if (menu.breakfastEntrees != null) {
			for (String item : menu.breakfastEntrees) {
				outputString += item + "\n";
			}
		}
		breakfastEntreeTextView.setText(outputString);
		outputString = "";
		if (menu.lunchEntrees != null) {
			for (String item : menu.lunchEntrees) {
				outputString += item + "\n";
			}
		}
		lunchEntreeTextView.setText(outputString);
		outputString = "";
		if (menu.dinnerEntrees != null) {
			for (String item : menu.dinnerEntrees) {
				outputString += item + "\n";
			}
		}
		dinnerEntreeTextView.setText(outputString);
	}
}
