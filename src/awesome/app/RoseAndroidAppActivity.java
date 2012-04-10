package awesome.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class RoseAndroidAppActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		((View) findViewById(R.id.schedule_lookup_button)).setOnClickListener(this);
		((View) findViewById(R.id.student_lookup_button)).setOnClickListener(this);
		((View) findViewById(R.id.ara_menu_button)).setOnClickListener(this);
		((View) findViewById(R.id.help_button)).setOnClickListener(this);
		((View) findViewById(R.id.feedback_button)).setOnClickListener(this);
		((View) findViewById(R.id.bandwidth_button)).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.editButton:
			Dialog dialog = new PasswordDialog(this);
			dialog.show();
			return true;
		}
		return false;
	}

	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.feedback_button:
			intent = new Intent(this, InAppFeedback.class);
			break;
		case R.id.student_lookup_button:
			intent = new Intent(this, StudentLookupActivity.class);
			break;
		case R.id.ara_menu_button:
			intent = new Intent(this, Ara.class);
			break;
		case R.id.help_button:
			intent = new Intent(this, Help.class);
			break;
		case R.id.schedule_lookup_button:
			intent = new Intent(this, ScheduleLookupActivity.class);
			break;
		case R.id.bandwidth_button:
			intent = new Intent(this, Bandwidth.class);
			break;
		}
		startActivity(intent);
	}
}