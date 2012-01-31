package awesome.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InAppFeedback extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.in_app_feedback);

		// Button Method
		Button button = (Button) findViewById(R.id.feedback_button);
		button.setOnClickListener(this);
	}

	public void onClick(View arg0) {
		Toast toast;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(getString(R.string.feedbackURL));
		String feedback = ((EditText) findViewById(R.id.feedback_edittext))
				.getText().toString();
		if (feedback.equals("")) {
			toast = Toast
					.makeText(
							getApplicationContext(),
							"You have to put feedback in the textbox in order to send!",
							Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		String fieldName = getString(R.string.fieldName);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair(fieldName, feedback));

		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
			client.execute(post);
		} catch (Exception e) {
		}

		toast = Toast.makeText(getApplicationContext(),
				"Feedback sent. Thank you so much!", Toast.LENGTH_SHORT);
		toast.show();
	}
}