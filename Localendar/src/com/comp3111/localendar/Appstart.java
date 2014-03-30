package com.comp3111.localendar;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;

public class Appstart extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
				
		new Handler().postDelayed(new Runnable(){
		@Override
		public void run(){
			Intent intent = new Intent (Appstart.this, Localendar.class);			
			startActivity(intent);
			finish();
		}
	}, 1000);
	
   }
   
	
	
}
