package com.comp3111.localendar;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainTab extends TabActivity implements OnCheckedChangeListener, OnClickListener{

	
	private TabHost mHost;
	private Intent mCalendarIntent;  
	private Intent mMapIntent;  
	private Intent mSettingsIntent;
	private int mCurrentTab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintab);
		//getActionBar().hide();
		mCurrentTab = 1;
		setIntetns();
		mHost.setCurrentTabByTag("tab_map");
		initButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_menu, menu);
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    menu.clear();
	    if(mCurrentTab == 1) {
	    	getMenuInflater().inflate(R.menu.map_menu, menu);
	    }
	    
	    return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {  
			invalidateOptionsMenu();
            switch (buttonView.getId()) {  
            case R.id.calendar_button: {
                mHost.setCurrentTabByTag("tab_calendar");
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                mCurrentTab = 0;
            }
                break;  
            case R.id.map_button: {
                mHost.setCurrentTabByTag("tab_map");
                if (mCurrentTab == 2)
                	overridePendingTransition(R.anim.left_in, R.anim.left_out);
                else if (mCurrentTab == 0)
                	overridePendingTransition(R.anim.right_in, R.anim.right_out);
                mCurrentTab = 1;
            }
                break;  
            case R.id.settings_button: {
            	overridePendingTransition(R.anim.right_in, R.anim.right_out);
                mHost.setCurrentTabByTag("tab_settings");  
                mCurrentTab = 2;
            }
                break;  
            }  
        }  
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.add_button:
			Intent intent = new Intent(this, AddEventActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			break;
		}
	}
	
	private void setIntetns() {
		
		mHost = getTabHost();
		
		mCalendarIntent = new Intent(this, CalendarActivity.class);
		mMapIntent = new Intent(this, MapActivity.class);
		mSettingsIntent = new Intent(this, SettingsActivity.class);
		
		mHost.addTab(buildTabSpec("tab_calendar", R.string.calendar, mCalendarIntent));
		mHost.addTab(buildTabSpec("tab_map", R.string.map, mMapIntent));
		mHost.addTab(buildTabSpec("tab_settings", R.string.settings, mSettingsIntent));
	}
	
	private void initButtons() {  
        ((RadioButton) findViewById(R.id.calendar_button)).setOnCheckedChangeListener(this);  
        ((RadioButton) findViewById(R.id.map_button)).setOnCheckedChangeListener(this);  
        ((RadioButton) findViewById(R.id.settings_button)).setOnCheckedChangeListener(this); 
        ((Button) findViewById(R.id.add_button)).setOnClickListener(this);
	}
	
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, final Intent content) {  
        //return mHost.newTabSpec(tag).setIndicator(getString(resLabel),getResources().getDrawable(resIcon)).setContent(content);  
		return mHost.newTabSpec(tag).setIndicator(getString(resLabel)).setContent(content);
	}	

}

