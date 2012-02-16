package awesome.app;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleLookupActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_lookup);

		// Button Method
		Button button = (Button) findViewById(R.id.searchButton);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				((LinearLayout) findViewById(R.id.linearLayout2))
						.removeAllViews();
				Toast toast;
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(getString(R.string.searchURL));
				String searchString = ((EditText) findViewById(R.id.editText1))
						.getText().toString();
				if (searchString.equals("")) {
					toast = Toast
							.makeText(
									getApplicationContext(),
									"You have to put something in the textbox in order to search!",
									Toast.LENGTH_SHORT);
					toast.show();
					return;
				}
				String fieldName = getString(R.string.fieldNameLookup);
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair(fieldName, searchString));
				pairs.add(new BasicNameValuePair("templar", "201230"));
				String searchResult = new String();
				NodeList nodes = null;
				try {
					post.setEntity(new UrlEncodedFormEntity(pairs));
					HttpResponse response = client.execute(post);
					HttpEntity resultEntity = response.getEntity();
					searchResult = EntityUtils.toString(resultEntity);
					Document doc = null;
					DocumentBuilderFactory dbf = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					InputSource is = new InputSource();
					is.setCharacterStream(new StringReader(searchResult));
					doc = db.parse(is);
					nodes = doc.getElementsByTagName("class");
					searchResult = new String();
				} catch (Exception e) {
					searchResult = new String("Read Error!");
				}
				// TextView myTextView = new
				// TextView(ScheduleLookupActivity.this);
				TextView textView1 = new TextView(ScheduleLookupActivity.this);
				// myTextView.setText(searchResult);
				// ((LinearLayout)
				// findViewById(R.id.linearLayout2)).addView(myTextView);
				if (nodes != null) {
					String[] classes = new String[50];
					for (int i = 0; i < nodes.getLength(); i++) {
						Element e = (Element) nodes.item(i);
						ArrayList<String> classNames = new ArrayList<String>();
						ArrayList<String> classNumbers = new ArrayList<String>();
						ArrayList<String> instructors = new ArrayList<String>();
						ArrayList<ArrayList<String>> meetingData = new ArrayList<ArrayList<String>>();

						for (Node j = e.getFirstChild(); j != null; j = j
								.getNextSibling()) {
							String nodeName = j.getNodeName()
									.replaceAll("#text", "")
									.replaceAll(":", "").trim();
							if (nodeName.equals("number"))
								classNumbers.add(j.getTextContent());
							if (nodeName.equals("name"))
								classNames.add(j.getTextContent());
							if (nodeName.equals("instructer"))
								instructors.add(j.getTextContent());
							if (nodeName.equals("meeting")) {
								ArrayList<String> days;
								ArrayList<String> times;
								ArrayList<String> places;
								if (meetingData.size() == 0) {
									days = new ArrayList<String>();
									times = new ArrayList<String>();
									places = new ArrayList<String>();
								} else {
									days = meetingData.get(0);
									times = meetingData.get(1);
									places = meetingData.get(2);
								}

								for (Node subNode = j.getFirstChild(); subNode != null; subNode = subNode
										.getNextSibling()) {
									String subNodeName = subNode.getNodeName()
											.replaceAll("#text", "")
											.replaceAll(":", "").trim();
									if (subNodeName.equals("days"))
										days.add(subNode.getTextContent());
									if (subNodeName.equals("hours"))
										times.add(subNode.getTextContent());
									if (subNodeName.equals("room"))
										places.add(subNode.getTextContent());
								}
								meetingData.add(days);
								meetingData.add(times);
								meetingData.add(places);
							}
							if (nodeName.equals("finalData"))
								nodeName = new String("Final Data: ");
						}
						for (int index = 0; index < meetingData.get(0).size(); index++) {
							// textView1 = new
							// TextView(ScheduleLookupActivity.this);
							// textView1.setText(classNumbers.get(0)+"\n in "+
							// meetingData.get(2).get(0));
							int[] hours = getTimesInSession(meetingData.get(1)
									.get(index));
							int[] array = getDaysInSession(meetingData.get(0)
									.get(index));
							for (int day : array) {
								// textView1 = new
								// TextView(ScheduleLookupActivity.this);
								// textView1.setText(classNumbers.get(0)+"\t\n in "+
								// meetingData.get(2).get(0)+"\t");
								// textView1.setLayoutParams(new
								// TableRow.LayoutParams(hour));
								for (int hour : hours) {
									switch (day) {
									case 1:
										classes[hour] = classNumbers.get(0)
												+ "\t\n in "
												+ meetingData.get(2).get(index)
												+ "\t";
										// ((LinearLayout)
										// findViewById(R.id.tableRow1)).addView(textView1);
										break;
									case 2:
										classes[hour + 10] = classNumbers
												.get(0)
												+ "\t\n in "
												+ meetingData.get(2).get(index)
												+ "\t";
										// ((LinearLayout)
										// findViewById(R.id.tableRow2)).addView(textView1);
										break;
									case 3:
										classes[hour + 20] = classNumbers
												.get(0)
												+ "\t\n in "
												+ meetingData.get(2).get(index)
												+ "\t";
										// ((LinearLayout)
										// findViewById(R.id.tableRow3)).addView(textView1);
										break;
									case 4:
										classes[hour + 30] = classNumbers
												.get(0)
												+ "\t\n in "
												+ meetingData.get(2).get(index)
												+ "\t";
										// ((LinearLayout)
										// findViewById(R.id.tableRow4)).addView(textView1);
										break;
									case 5:
										classes[hour + 40] = classNumbers
												.get(0)
												+ "\t\n in "
												+ meetingData.get(2).get(index)
												+ "\t";
										// ((LinearLayout)
										// findViewById(R.id.tableRow5)).addView(textView1);
										break;
									}
								}
							}

						}
					}

					for (int x = 1; x <= 50; x++) {
						textView1 = new TextView(ScheduleLookupActivity.this);

						textView1.setText(classes[x - 1]);
						if (x <= 10) {
							textView1
									.setLayoutParams(new TableRow.LayoutParams(
											x - 10));
							((LinearLayout) findViewById(R.id.tableRow1))
									.addView(textView1);
						} else if (x <= 20) {
							textView1
									.setLayoutParams(new TableRow.LayoutParams(
											x - 10));
							((LinearLayout) findViewById(R.id.tableRow2))
									.addView(textView1);
						} else if (x <= 30) {
							textView1
									.setLayoutParams(new TableRow.LayoutParams(
											x - 20));
							((LinearLayout) findViewById(R.id.tableRow3))
									.addView(textView1);
						} else if (x <= 40) {
							textView1
									.setLayoutParams(new TableRow.LayoutParams(
											x - 30));
							((LinearLayout) findViewById(R.id.tableRow4))
									.addView(textView1);
						} else {
							textView1
									.setLayoutParams(new TableRow.LayoutParams(
											x - 40));
							((LinearLayout) findViewById(R.id.tableRow5))
									.addView(textView1);
						}
					}
				}
			}
		});
	}

	public int[] getDaysInSession(String str) {
		ArrayList<Integer> returnVal = new ArrayList<Integer>();

		if (str.toUpperCase().contains("M"))
			returnVal.add(1);
		if (str.toUpperCase().contains("T"))
			returnVal.add(2);
		if (str.toUpperCase().contains("W"))
			returnVal.add(3);
		if (str.toUpperCase().contains("R"))
			returnVal.add(4);
		if (str.toUpperCase().contains("F"))
			returnVal.add(5);
		int[] returnArray = new int[returnVal.size()];
		for (int x = 0; x < returnVal.size(); x++) {
			returnArray[x] = returnVal.get(x).intValue();
		}
		return returnArray;
	}

	public int[] getTimesInSession(String str) {
		ArrayList<Integer> returnVal = new ArrayList<Integer>();
		if (1 == str.trim().length())
			return new int[] { Character.getNumericValue(str.charAt(0)) };
		else if (3 == str.trim().length()) {
			int x = Character.getNumericValue(str.charAt(0));
			int y = Character.getNumericValue(str.charAt(2));
			while (x <= y) {
				returnVal.add(x);
				x++;
			}
		} else {
			String[] strings = str.split("-");
			int x = Integer.getInteger(strings[0]).intValue();
			int y = Integer.getInteger(strings[1]).intValue();
			if (y > 1710) {
				while (x <= 10) {
					returnVal.add(x);
					x++;
				}
			} else {
				int z = 805 + 55 * x;
				while (z <= 1710) {
					returnVal.add(x);
					x++;
				}
			}
		}
		int[] returnArray = new int[returnVal.size()];
		for (int x = 0; x < returnVal.size(); x++) {
			returnArray[x] = returnVal.get(x).intValue();
		}
		return returnArray;
	}

}