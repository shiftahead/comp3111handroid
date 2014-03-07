package com.comp3111.localendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AboutusActivity extends Activity implements OnClickListener{
	
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
	}

	@Override
	public void onClick(View v) {
		if(getResources().getResourceEntryName(v.getId()).equals("back_from_aboutus_to_settings")){
			finish();
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
		
	}
}
