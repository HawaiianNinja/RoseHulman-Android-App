package awesome.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import awesome.app.R;
import awesome.app.security.PasswordDialog;
import awesome.app.security.PasswordManager;

public class RoseAndroidAppActivity extends Activity implements OnClickListener {

	private PasswordManager mManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		((View) findViewById(R.id.schedule_lookup_button)).setOnClickListener(this);
		((View) findViewById(R.id.student_lookup_button)).setOnClickListener(this);
		((View) findViewById(R.id.ara_menu_button)).setOnClickListener(this);
		((View) findViewById(R.id.help_button)).setOnClickListener(this);
		((View) findViewById(R.id.feedback_button)).setOnClickListener(this);
		((View) findViewById(R.id.bandwidth_button)).setOnClickListener(this);
		((View) findViewById(R.id.subway_button)).setOnClickListener(this);
		mManager = new PasswordManager(this);
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
			Dialog dialog = new PasswordDialog(this, mManager, null);
			dialog.show();
			return true;
		}
		return false;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_button:
			startActivity(new Intent(this, InAppFeedbackActivity.class));
			break;
		case R.id.student_lookup_button:
			getPasswordAndLaunch(new Intent(this, StudentLookupActivity.class));
			break;
		case R.id.ara_menu_button:
			startActivity(new Intent(this, AraActivity.class));
			break;
		case R.id.help_button:
			startActivity(new Intent(this, HelpActivity.class));
			break;
		case R.id.schedule_lookup_button:
			getPasswordAndLaunch(new Intent(this, ScheduleLookupActivity.class));
			break;
		case R.id.bandwidth_button:
			getPasswordAndLaunch(new Intent(this, BandwidthActivity.class));
			break;
		case R.id.subway_button:
			startActivity(new Intent(this, SubwayCamActivity.class));
			break;
		}
	}

	public void getPasswordAndLaunch(Intent intent) {
		if (!mManager.infoExists()) {
			Dialog dialog = new PasswordDialog(this, mManager, intent);
			dialog.show();
		} else {
			intent.putExtra(PasswordManager.USERNAME, mManager.getUsername());
			intent.putExtra(PasswordManager.PASSWORD, mManager.getPassword());
			startActivity(intent);
		}
	}
}