package com.comp3111.localendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingsActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.about_us_arrow:
		case R.id.about_us: {
			Intent intent = new Intent (this, AboutusActivity.class);			
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
		}
		break;
		case R.id.my_account_arrow:
		case R.id.my_account: {
			Intent intent = new Intent (this, SigninActivity.class);			
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
		}
		break;
		}
	}

}
