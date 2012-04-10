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
	//private final int PASSWORD_DIALOG = 1;

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
//			showDialog(PASSWORD_DIALOG);
			Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.password_dialog);
			dialog.show();
			return true;
		}
		return false;
	}
//
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		super.onCreateDialog(id);
//		AlertDialog dialog = null;
//		switch (id) {
//		case PASSWORD_DIALOG:
//			AlertDialog.Builder builder;
//			Context mContext = getApplicationContext();
//			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//			View layout = inflater.inflate(R.layout.password_dialog, (ViewGroup) findViewById(R.id.password_root));
//			builder = new AlertDialog.Builder(mContext);
//			builder.setView(layout);
//			dialog = builder.create();
//		}
//		return dialog;
//	}

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