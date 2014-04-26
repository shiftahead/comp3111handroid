package com.comp3111.localendar;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutusActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("About us");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
	    onBackPressed();
	    overridePendingTransition(R.anim.left_in, R.anim.right_out);
	    return true;
	}
}
