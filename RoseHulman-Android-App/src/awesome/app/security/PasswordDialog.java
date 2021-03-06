package awesome.app.security;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import awesome.app.R;

public class PasswordDialog extends Dialog implements
		android.view.View.OnClickListener {

	private PasswordManager mManager;
	private CheckBox mSavePassword;
	private EditText mUsernameBox;
	private EditText mPasswordBox;
	private Context mContext;
	private Intent mIntent;

	public PasswordDialog(Context context, PasswordManager manager,
			Intent intent) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.password_dialog);
		mContext = context;
		mManager = manager;
		mIntent = intent;
		mUsernameBox = ((EditText) findViewById(R.id.passwordUsername));
		mUsernameBox.setText(mManager.getUsername());
		mPasswordBox = ((EditText) findViewById(R.id.passwordPassword));
		mPasswordBox.setText(mManager.getPublicPassword());
		mSavePassword = (CheckBox) findViewById(R.id.savePassword);
		mSavePassword.setChecked(mManager.getSavePassword());
		((Button) findViewById(R.id.passwordSubmit)).setOnClickListener(this);
		((Button) findViewById(R.id.clearPassword)).setOnClickListener(this);
		((Button) findViewById(R.id.cancelPassword)).setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.passwordSubmit:
			if (mManager.update(mUsernameBox.getText().toString(), mPasswordBox
					.getText().toString())) {

				if (mSavePassword.isChecked()) {
					mManager.save();
				}
				if (mIntent != null) {
					mIntent.putExtra(PasswordManager.USERNAME,
							mManager.getUsername());
					mIntent.putExtra(PasswordManager.PASSWORD,
							mManager.getPassword());
					mContext.startActivity(mIntent);
				}
				dismiss();
			} else {
				Toast.makeText(mContext, mContext.getString(R.string.incorrectPassword),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.clearPassword:
			mManager.clear();
			dismiss();
			break;
		}
	}
}
