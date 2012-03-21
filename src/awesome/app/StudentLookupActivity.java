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
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StudentLookupActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_lookup);

		// Button Method
		Button button = (Button) findViewById(R.id.lookup_button);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((LinearLayout) findViewById(R.id.lookup_layout))
						.removeAllViews();
				Toast toast;
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(getString(R.string.searchURL));
				String searchString = ((EditText) findViewById(R.id.lookup_text))
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
					nodes = doc.getElementsByTagName("Student");
					searchResult = new String("We have " + nodes.getLength()
							+ " result(s):");
				} catch (Exception e) {
					searchResult = new String("Read Error!");
				}
				TextView myTextView = new TextView(StudentLookupActivity.this);
				myTextView.setText(searchResult);
				((LinearLayout) findViewById(R.id.lookup_layout))
						.addView(myTextView);
				if (nodes != null) {
					for (int i = 0; i < nodes.getLength(); i++) {
						Element e = (Element) nodes.item(i);
						String itemDetail = new String();
						for (Node j = e.getFirstChild(); j != null; j = j
								.getNextSibling()) {
							String nodeName = j.getNodeName()
									.replaceAll("#text", "")
									.replaceAll(":", "").trim();
							if (nodeName.equals("username"))
								nodeName = new String("Username: ");
							if (nodeName.equals("name"))
								nodeName = new String("Full Name: ");
							if (nodeName.equals("status"))
								nodeName = new String("Status: ");
							if (nodeName.equals("cm"))
								nodeName = new String("CM ");
							if (nodeName.equals("room"))
								nodeName = new String("Room: ");
							if (nodeName.equals("phone"))
								nodeName = new String("Phone Number: ");
							if (nodeName.equals("dept"))
								nodeName = new String("Department: ");
							itemDetail += nodeName + j.getTextContent().trim()
									+ "\n";
						}
						myTextView = new TextView(StudentLookupActivity.this);
						myTextView.setText(itemDetail);
						((LinearLayout) findViewById(R.id.lookup_layout))
								.addView(myTextView);
					}
				}
			}
		});
	}
}