package awesome.app;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class PasswordDialog extends Dialog implements android.view.View.OnClickListener {

	private PasswordManager mManager;
	private CheckBox mSavePassword;
	private EditText mUsernameBox;
	private EditText mPasswordBox;

	public PasswordDialog(Context context, PasswordManager manager) {
		super(context);
		this.setContentView(R.layout.password_dialog);
		mManager = manager;
		mUsernameBox = ((EditText) findViewById(R.id.passwordUsername));
		mUsernameBox.setText(mManager.getUsername());
		mPasswordBox = ((EditText) findViewById(R.id.passwordPassword));
		mPasswordBox.setText("");
		mSavePassword = (CheckBox) findViewById(R.id.savePassword);
		mSavePassword.setChecked(mManager.infoExists());
		((Button) findViewById(R.id.passwordSubmit)).setOnClickListener(this);
	}

	public void onClick(View v) {
		Log.d("RH", "HI");
		switch (v.getId()) {
		case R.id.passwordSubmit:

			mManager.update(mUsernameBox.getText().toString(), mPasswordBox.getText().toString());
			if (mSavePassword.isChecked()) {
				mManager.save();
			} else {
				mManager.clear();
			}

		}
		Log.d("RH", "username: " + mManager.getUsername() + " password: " + mManager.getPassword());
		dismiss();
	}

}
