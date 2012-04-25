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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButton;

public class StudentLookupActivity extends Activity implements ICallbackable {

	private Button searchButton;
	private LinearLayout lookupLayout;
	private LinearLayout backgroundLayout;
	private static Handler scheduleButtonClickHandler;
	private static Runnable launchSchedulePageTask;
	private static String selectedUsername;

	public static Handler getScheduleButtonClickHandler() {
		return scheduleButtonClickHandler;
	}

	public static Runnable getLaunchSchedulePageTask() {
		return launchSchedulePageTask;
	}

	public static String getSelectedUsername() {
		return selectedUsername;
	}

	public static void setSelectedUsername(String username) {
		selectedUsername = username;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.student_lookup);
		lookupLayout = (LinearLayout) findViewById(R.id.lookup_layout);
		backgroundLayout = (LinearLayout) findViewById(R.id.backgroundLayout);
		scheduleButtonClickHandler = new Handler();
		launchSchedulePageTask = new Runnable() {
			public void run() {
				Bundle bundle = new Bundle();
				bundle.putString("username", getSelectedUsername());
				Intent newIntent = new Intent(getApplicationContext(), ScheduleLookupActivity.class);
				newIntent.putExtras(bundle);
				startActivity(newIntent);
			}
		};
		searchButton = (Button) findViewById(R.id.lookup_button);
		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				lookupLayout.removeAllViews();
				doStudentSearch();
			}
		});
	}

	private void doStudentSearch() {
		String searchString = ((EditText) findViewById(R.id.lookup_text)).getText().toString().trim();
		if (!isValidSearchString(searchString)) {
			showBackground();
			Toast.makeText(getApplicationContext(), getString(R.string.emptySearchErr), Toast.LENGTH_SHORT).show();
			return;
		}
		NodeList resultNodes = peformStudentSearchRequest(searchString);
		if (resultNodes == null) {
			String searchResultInfo = getString(R.string.resultParsingErr);
			createAndShowTextView(searchResultInfo);
			return;
		}
		showSearchResults(resultNodes);
	}

	private boolean isValidSearchString(String searchString) {
		return searchString != null && searchString.trim().length() > 0;
	}

	private NodeList peformStudentSearchRequest(String searchString) {
		//HttpClient client = new DefaultHttpClient();
		HttpClient client = SecurityHole.getNewHttpClient();
		String searchURL = getString(R.string.serverURL) + getString(R.string.searchPage);
		Log.v("URL Request", searchURL);
		HttpPost post = new HttpPost(searchURL);
		String fieldName = getString(R.string.fieldNameLookup);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(fieldName, searchString));
		NodeList resultNodes = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(post);
			HttpEntity responseEntity = response.getEntity();
			String httpResponse = EntityUtils.toString(responseEntity);
			Document document = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(httpResponse));
			document = db.parse(inputSource);
			resultNodes = document.getElementsByTagName(getString(R.string.studentNodesTagName));
		} catch (Exception e) {
			resultNodes = null;
		}
		return resultNodes;
	}

	private ArrayList<StudentData> parseStudentList(NodeList nodelist) {
		ArrayList<StudentData> studentList = new ArrayList<StudentData>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			studentList.add(parseResultNodeIntoAStudent(nodelist.item(i)));
		}
		return studentList;
	}

	private StudentData parseResultNodeIntoAStudent(Node studentInfoElement) {
		StudentData student = new StudentData();
		for (Node dataNode = studentInfoElement.getFirstChild(); dataNode != null; dataNode = dataNode.getNextSibling()) {
			String nodeName = dataNode.getNodeName().replaceAll("#text", "").replaceAll(":", "").trim();
			if (nodeName.equals("username")) {
				student.setUsername(dataNode.getTextContent().trim());
			} else if (nodeName.equals("name")) {
				student.setName(dataNode.getTextContent().trim());
			} else if (nodeName.equals("status")) {
				student.setStatus(dataNode.getTextContent().trim());
			} else if (nodeName.equals("cm")) {
				student.setCm(dataNode.getTextContent().trim());
			} else if (nodeName.equals("room")) {
				student.setRoom(dataNode.getTextContent().trim());
			} else if (nodeName.equals("phone")) {
				student.setPhone(dataNode.getTextContent().trim());
			} else if (nodeName.equals("dept")) {
				student.setDepartment(dataNode.getTextContent().trim());
			}
		}
		return student;
	}

	private void showSearchResults(NodeList resultNodes) {
		hideBackground();
		String searchResultInfo = getResources().getQuantityString(R.plurals.numberOfResultsAvailable,
				resultNodes.getLength(), resultNodes.getLength());
		createAndShowTextView(searchResultInfo);
		ArrayList<StudentData> students = parseStudentList(resultNodes);
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		ListView resultList = (ListView) inflater.inflate(R.layout.student_list, null);
		resultList.setAdapter(new StudentListAdapter(getApplicationContext(), students));
		lookupLayout.addView(resultList);
	}

	private void createAndShowTextView(String content) {
		TextView newTextView = new TextView(StudentLookupActivity.this);
		newTextView.setText(content);
		newTextView.setGravity(Gravity.CENTER);
		newTextView.setTextColor(Color.DKGRAY);
		newTextView.setFocusable(false);
		lookupLayout.addView(newTextView);
	}

	private void hideBackground() {
		backgroundLayout.setVisibility(View.GONE);
	}

	private void showBackground() {
		backgroundLayout.setVisibility(View.VISIBLE);
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}
}

class StudentListAdapter extends ArrayAdapter<StudentData> {
	private final Context context;
	private final ArrayList<StudentData> values;

	public StudentListAdapter(Context context, ArrayList<StudentData> values) {
		super(context, R.layout.student_data_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, final View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.student_data_row, parent, false);
		TextView usernameView = (TextView) rowView.findViewById(R.id.studentUsername);
		TextView nameView = (TextView) rowView.findViewById(R.id.studentName);
		TextView statusView = (TextView) rowView.findViewById(R.id.studentStatus);
		TextView cmView = (TextView) rowView.findViewById(R.id.studentCM);
		TextView departmentView = (TextView) rowView.findViewById(R.id.studentDepartment);
		TextView phoneView = (TextView) rowView.findViewById(R.id.studentPhone);
		TextView roomView = (TextView) rowView.findViewById(R.id.studentRoom);

		usernameView.setText(values.get(position).getEntitledUsername());
		nameView.setText(values.get(position).getName());
		statusView.setText(values.get(position).getEntitledStatus());
		cmView.setText(values.get(position).getEntitledCm());
		departmentView.setText(values.get(position).getEntitledDepartment());
		phoneView.setText(values.get(position).getEntitledPhone());
		roomView.setText(values.get(position).getEntitledRoom());

		ZoomButton goToSchedule = (ZoomButton) rowView.findViewById(R.id.checkScheduleButton);
		goToSchedule.setTag(values.get(position).getUsername());
		goToSchedule.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				StudentLookupActivity.setSelectedUsername((String) v.getTag());
				StudentLookupActivity.getScheduleButtonClickHandler().post(
						StudentLookupActivity.getLaunchSchedulePageTask());
			}
		});
		return rowView;
	}
}
