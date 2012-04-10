package awesome.app;

import android.app.Dialog;
import android.content.Context;
import android.widget.EditText;

public class PasswordDialog extends Dialog {
	
	private PasswordManager mManager;

	public PasswordDialog(Context context) {
		super(context);
		this.setContentView(R.layout.password_dialog); 
		mManager = new PasswordManager(context);
		((EditText)findViewById(R.id.passwordUsername)).setText(mManager.getDefaultUsername());
		((EditText)findViewById(R.id.passwordPassword)).setText(mManager.getDefaultUsername());
	}

}
