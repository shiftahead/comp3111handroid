package com.comp3111.localendar;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class SigninActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Settings");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
	    onBackPressed();
	    overridePendingTransition(R.anim.left_in, R.anim.left_out);
	    return true;
	}
}
