package com.comp3111.localendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddEventActivity extends Activity implements OnClickListener{
		
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event_dialog);
		getActionBar().hide();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
	}

	@Override
	public void onClick(View v) {
		if(getResources().getResourceEntryName(v.getId()).equals("confirm_add")){
			
		}
		
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
		
	
}
