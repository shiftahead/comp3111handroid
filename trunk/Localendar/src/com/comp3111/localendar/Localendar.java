package com.comp3111.localendar;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Parcelable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3111.localendar.calendar.AddEventActivity;
import com.comp3111.localendar.calendar.MyCalendar;
import com.comp3111.localendar.map.MyGoogleMap;
import com.comp3111.localendar.map.Place;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.NonSwipeableViewPager;
import com.comp3111.localendar.support.PlacesAutoCompleteAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
//import com.google.maps.android.PolyUtil;


public class Localendar extends Activity implements OnClickListener, OnCheckedChangeListener, OnItemClickListener {
	
	public static Localendar instance = null;
	public static Calendar calendar = null;
	private ActionBar actionBar;
	private NonSwipeableViewPager pager;	//view pager
	private int currentTabIndex;	//tab index, maps is set default 
	private RadioButton calendarButton, mapButton, settingsButton;
	private Button addButton;
	private ClearableAutoCompleteTextView searchBox;
	private boolean searchBoxShown;
	private ImageView searchIcon;
	private static TextView calendarTitle;
	
	
	// MyLocalendar is map object
	MyGoogleMap myGoogleMap;
	
	//ConnectionDetector internetConnectionDetector;
	//GPSTracker gpsDetector;
	
	// Parameters for quering the calendar
	// Projection array. Creating indices for this array instead of doing
	// dynamic lookups improves performance.
	public static final String[] EVENT_PROJECTION = new String[] {
		Calendars._ID, // 0
		Calendars.ACCOUNT_NAME, // 1
		Calendars.CALENDAR_DISPLAY_NAME // 2
	};

	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainframe);
		
		//Raymond: bad practice, just for testing purpose
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		
		//hide the keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
		//set action bar
        actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflator.inflate(R.layout.main_actionbar, null);
        
        actionBar.setCustomView(actionBarView);
        calendarTitle = (TextView) findViewById(R.id.calendar_title);
        
		//Get the AutoCompTextView
	    searchBox = (ClearableAutoCompleteTextView) findViewById(R.id.search_box);
	    searchIcon = (ImageView) findViewById(R.id.place_view);
	    searchIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if(view.equals(searchIcon)) {
					
					//show the search box
					searchBox.setVisibility(View.VISIBLE);
					searchIcon.setVisibility(View.GONE);
					searchBoxShown = true;
					
					// show the keyboard
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
				}
				
			}
	    	
	    });
        searchBox.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_result_list_item));
        searchBox.setOnClearListener(new ClearableAutoCompleteTextView.OnClearListener() {
    		
    		@Override
    		public void onClear() {
    			if(searchBox.getText().toString().equals("")) {
    				if(searchBoxShown) {
    					//hide the search box
    					searchBox.setText("");
    					searchIcon.setVisibility(View.VISIBLE);
    					searchBox.setVisibility(View.GONE);
    					searchBoxShown = false;
    					
    					// hide the keyboard
    					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    					imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
    				}
    			}
    			else {
    				searchBox.setText("");
    			}
    		}
    	});
		searchBox.setOnItemClickListener(this);
		
	    searchBoxShown = true;
	    searchBox.setVisibility(View.GONE);
	    searchIcon.setVisibility(View.VISIBLE);
	    
	    calendar = Calendar.getInstance();
	    setCalendarTitle();
	    calendarTitle.setVisibility(View.GONE);
	    
        //initialize pager and tab buttons
        pager = (NonSwipeableViewPager)findViewById(R.id.tabpager);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        addButton = (Button) findViewById(R.id.add_button);
        calendarButton = (RadioButton) findViewById(R.id.calendar_button);
        mapButton = (RadioButton) findViewById(R.id.map_button);
        settingsButton = (RadioButton) findViewById(R.id.settings_button);
        
        addButton.setOnClickListener(this);
        calendarButton.setOnCheckedChangeListener(this);  
        mapButton.setOnCheckedChangeListener(this);  
        settingsButton.setOnCheckedChangeListener(this);  
        
        //initialize views for each tab
        LayoutInflater myLayout = LayoutInflater.from(this);
        View viewCalendar = myLayout.inflate(R.layout.tab_calendar, null);
        View viewMap = myLayout.inflate(R.layout.tab_map, null);
        View viewSettings = myLayout.inflate(R.layout.tab_settings, null);
        final ArrayList<View> views = new ArrayList<View>();
        views.add(viewCalendar);
        views.add(viewMap);
        views.add(viewSettings);
        
        //initialize the pager adapter
        PagerAdapter myPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		//set the map as the startup page and load the map
		pager.setAdapter(myPagerAdapter);
		pager.setCurrentItem(1);
		currentTabIndex = 1;
		
		//Initialize map
		myGoogleMap = new MyGoogleMap(((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap());
		
	}
	
	
	//Click the items on the autocomplete list and the result will be allocated to the String and Doubles
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    	String placeSearch = (String) adapterView.getItemAtPosition(position);
    	//myGoogleMap.addmarker(Place.getPlaceFromAddress(placeSearch), true, placeSearch, placeSearch);
        myGoogleMap.moveCamera(Place.getPlaceFromAddress(placeSearch));
    	Toast.makeText(this, placeSearch, Toast.LENGTH_SHORT).show();
        // hide the keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
    }
    
    @Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {  
			//invalidateOptionsMenu();
            switch (buttonView.getId()) {  
            case R.id.calendar_button: {
            	searchBoxShown = false;
        		searchBox.setVisibility(View.GONE);
        		searchIcon.setVisibility(View.GONE);
        		calendarTitle.setVisibility(View.VISIBLE);
                pager.setCurrentItem(0);  
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                currentTabIndex = 0;
            }
            	break;
            case R.id.map_button: {
                pager.setCurrentItem(1);
                searchBoxShown = true;
        	    searchBox.setVisibility(View.GONE);
        		calendarTitle.setVisibility(View.GONE);
        	    searchIcon.setVisibility(View.VISIBLE);
                
                if (currentTabIndex == 2)
                	overridePendingTransition(R.anim.left_in, R.anim.right_out);
                else if (currentTabIndex == 0)
                	overridePendingTransition(R.anim.right_in, R.anim.left_out);
                	
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                currentTabIndex = 1;
            }
                break;  
            case R.id.settings_button: {
            	searchBoxShown = false;
        		searchBox.setVisibility(View.INVISIBLE);
        		searchIcon.setVisibility(View.INVISIBLE);
        		calendarTitle.setVisibility(View.INVISIBLE);
            	
                pager.setCurrentItem(2);  
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                currentTabIndex = 2;
            }
                break;  
            }  
        }  
	}
    
    @Override
	public void onClick(View v) {
		
    	switch(v.getId()) {
		case R.id.add_button: {
			
			Intent intent = new Intent(this, AddEventActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
		}
			break;
		
		case R.id.about_us_arrow:
		case R.id.about_us: {
			Intent intent = new Intent (this, AboutusActivity.class);			
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}
			break;
			
		case R.id.my_account_arrow:
		case R.id.my_account: {
			Intent intent = new Intent (this, SigninActivity.class);			
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		}
			break;
    	}
		
	}
    
	/* set the pop up menu
	 * in this menu people can set up map type
	 */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    menu.clear();
	    
	    if(currentTabIndex == 0) {
	    	getMenuInflater().inflate(R.menu.calendar_menu, menu);
	    }
	    else if(currentTabIndex == 1) {
	    	getMenuInflater().inflate(R.menu.map_menu, menu);
	    }
	    else if (currentTabIndex == 2) {
	    	getMenuInflater().inflate(R.menu.settings_menu, menu);
	    }
	    return super.onPrepareOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.day_view :
			MyCalendar.setViewModeToMonth(false);
			setCalendarTitle();
			return true;
		case R.id.month_view :
			MyCalendar.setViewModeToMonth(true);
			MyCalendar.setTimeInMillis(calendar.getTimeInMillis());
			setCalendarTitle();
			return true;
		case R.id.go_to_today :
			calendar = Calendar.getInstance();
			MyCalendar.calendarInstance.refresh();
			MyCalendar.setTimeInMillis(calendar.getTimeInMillis());
			MyCalendar.setViewModeToMonth(false);
			setCalendarTitle();
			return true;
		case R.id.map_normal :
			MyGoogleMap.localenderMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case R.id.map_satellite :
			MyGoogleMap.localenderMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case R.id.map_hybrid :
			MyGoogleMap.localenderMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			return true;
		case R.id.add_shortcut:
			addShortcut();
			return true;
		}
	
		return false;
	}
	
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		
	}
	
	public void setPagerIndex(int index) {
		pager.setCurrentItem(index);
		currentTabIndex = index;
		switch(index){
		case 0:
			calendarButton.setChecked(true);
			break;
		case 1:
			mapButton.setChecked(true);
			break;
		case 2:
			settingsButton.setChecked(true);
			break;
		default:
			break;
		}
		
	}
	
	public void setCalendarTitle() {
		SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy");
		String currentDate = sdf.format(calendar.getTime());
		if(MyCalendar.viewMode == MyCalendar.DAY_VIEW)
			calendarTitle.setText(" " + currentDate);
		else if(MyCalendar.viewMode == MyCalendar.MONTH_VIEW)
			calendarTitle.setText(" " + MyCalendar.calendarInstance.getCurrentMonth());
	}
    
    private void addShortcut() {
		Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        String title = getResources().getString(R.string.app_name); 
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.localendar_logo);
        Intent intent = new Intent(getApplicationContext(), Appstart.class); 
        intent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title); 
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);  
        shortcutIntent.putExtra("duplicate", false); 
        getApplicationContext().sendBroadcast(shortcutIntent);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isShortcutCreated", true);
        editor.commit();
        Toast.makeText(this, "Shortcut added", Toast.LENGTH_SHORT).show();
	}
       

}
