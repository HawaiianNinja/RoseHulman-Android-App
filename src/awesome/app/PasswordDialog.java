package awesome.app;

import android.app.Dialog;
import android.content.Context;

public class PasswordDialog extends Dialog {

	public PasswordDialog(Context context) {
		super(context);
		this.setContentView(R.layout.password_dialog); 

	}

}
