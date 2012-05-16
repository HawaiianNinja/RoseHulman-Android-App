package awesome.app.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import awesome.app.R;
import awesome.app.connectivity.NetworkManager;
import awesome.app.data.HelpItem;
import awesome.app.handler.HelpHandler;

public class HelpActivity extends CallBackActivity {

	private HelpHandler mHelpHandler;
	private NetworkManager mNetworkManager;
	private ArrayList<HelpItem> helpItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help);
		mHelpHandler = new HelpHandler();
		//makeButtonsWork();
		mNetworkManager = new NetworkManager(getString(R.string.serverURL) + getString(R.string.helpPage),
				mHelpHandler, this);
		if(mNetworkManager.isOnline(this))
			this.mNetworkManager.getData();
		//update();
	}

	public void makeButtonsWork() {
//		LinearLayout helpPageLayout = (LinearLayout) findViewById(R.id.helpLinearLayout);
//		int numOfItems = 0;
//		
//		if (NetworkManager.isOnline(this)) {
//			mNetworkManager.getData();
//		}
//		else
//		{
//			Toast.makeText(this, "No Network Connection Available", Toast.LENGTH_SHORT).show();
//		}		
//		ArrayList<HelpItem> items = this.helpItems;
//		if(items!=null)
//			numOfItems = items.size();
//		
//		for(int x = 0; x < numOfItems; x++)
//		{
//			final HelpItem currentItem = items.get(x);
//			final TextView textView = new TextView(this);			
//			textView.setText(currentItem.getName());
//			textView.setTextColor(R.color.link_blue);
//			textView.setTextSize(12);
//			textView.setVisibility(View.VISIBLE);
//			
//			textView.setOnClickListener(new OnClickListener() {
//				public void onClick(View v) {
//					TextView separatorTextView = (TextView) findViewById(R.id.helpSeparatorTextView);
//					separatorTextView.setText(textView.getText());
//					doHelpSearch(currentItem.getInfo());
//				}
//			});
//			helpPageLayout.addView(textView);
//		}		
	}

	private void doHelpSearch(String helpInfoString) {
		TextView bodyText = (TextView) findViewById(R.id.helpBodyTextView);
		bodyText.setText(helpInfoString);
		bodyText.setTextSize(12);
	}
	
	@Override
	public void update() {
		this.helpItems = this.mHelpHandler.getHelpItems();
		LinearLayout helpPageLayout = (LinearLayout) findViewById(R.id.helpLinearLayout);
		int numOfItems = 0;		
		ArrayList<HelpItem> items = this.helpItems;
		if(items!=null)
		{
			numOfItems = items.size();
			for(int x = 0; x < numOfItems; x++)
			{
				HelpItem currentItem = new HelpItem();
				if(items.size() > x)
					currentItem = items.get(x);
			
				final TextView textView = new TextView(this);			
				textView.setText(currentItem.getName().replace('_', ' '));
				textView.setTextColor(R.color.link_blue);
				textView.setTextSize(24);
				textView.setVisibility(View.VISIBLE);
				final HelpItem tempItem = currentItem;
				textView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						TextView separatorTextView = (TextView) findViewById(R.id.helpSeparatorTextView);
						separatorTextView.setText(textView.getText());
						doHelpSearch(tempItem.getInfo());
					}
				});
				helpPageLayout.addView(textView);
			}
			TextView separatorTextView = (TextView) findViewById(R.id.helpSeparatorTextView);
			TextView helpBodyTextView = (TextView) findViewById(R.id.helpBodyTextView);
			helpPageLayout.removeView(separatorTextView);
			helpPageLayout.removeView(helpBodyTextView);
			helpPageLayout.addView(separatorTextView);
			helpPageLayout.addView(helpBodyTextView);
		}
	}
}

