package awesome.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class PasswordDialog extends Dialog implements android.view.View.OnClickListener {

	private PasswordManager mManager;
	private CheckBox mSavePassword;
	private EditText mUsernameBox;
	private EditText mPasswordBox;
	private Context mContext;
	private Intent mIntent;

	public PasswordDialog(Context context, PasswordManager manager, Intent intent) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.password_dialog);
		mContext = context;
		mManager = manager;
		mIntent = intent;
		mUsernameBox = ((EditText) findViewById(R.id.passwordUsername));
		mUsernameBox.setText(mManager.getUsername());
		mPasswordBox = ((EditText) findViewById(R.id.passwordPassword));
		mPasswordBox.setText("");
		mSavePassword = (CheckBox) findViewById(R.id.savePassword);
		mSavePassword.setChecked(mManager.getSavePassword());
		((Button) findViewById(R.id.passwordSubmit)).setOnClickListener(this);
		((Button) findViewById(R.id.clearPassword)).setOnClickListener(this);
		((Button) findViewById(R.id.cancelPassword)).setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.passwordSubmit:
			mManager.update(mUsernameBox.getText().toString(), mPasswordBox.getText().toString());
			Log.d("RH", "password " + mManager.getPassword());
			if (mSavePassword.isChecked()) {
				mManager.save();
			}
			Log.d("RH", "checking the class");
			if (mIntent != null) {
				Log.d("RH", "Hi starting the thingy");
				mIntent.putExtra(PasswordManager.USERNAME, mManager.getUsername());
				mIntent.putExtra(PasswordManager.PASSWORD, mManager.getPassword());
				mContext.startActivity(mIntent);
			}
			break;
		case R.id.clearPassword:
			mManager.clear();
			break;
		}
		dismiss();
	}
}
