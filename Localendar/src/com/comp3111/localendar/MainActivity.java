package com.comp3111.localendar;


import java.util.ArrayList;
import java.util.GregorianCalendar;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class MainActivity extends Activity implements View.OnClickListener{
	
	public static MainActivity instance = null;
	private ActionBar actionBar;
	private NonSwipeableViewPager pager;	//view pager
	private ImageView image, tab0, tab1, tab2, addView;	//tabs for list, map and settings
	private TextView text0, text1, text2, addText;	//text tabs for list, map and settings
	private int currentTabIndex;	//tab index, maps is set default 
	private int animationShiftOneScale, animationShiftTwoScale;	//animation effect
	private int screenWidth;
	
	// MyLocalendar is map object
	
	MyGoogleMap MyLocalendar;
	ConnectionDetector internetConnectionDetector;
	GPSTracker gpsDetector;
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
		
		// map activities  
		super.onCreate(savedInstanceState);
		//set action bar
        actionBar = getActionBar();
        actionBar.show();
        actionBar.setTitle("Map");
		setContentView(R.layout.activity_main);
		
		//hide the keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        
        //initialize pager and tabs
        pager = (NonSwipeableViewPager)findViewById(R.id.tabpager);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        tab0 = (ImageView) findViewById(R.id.img_list);
        tab1 = (ImageView) findViewById(R.id.img_map);
        tab2 = (ImageView) findViewById(R.id.img_settings);
        tab0.setOnClickListener(new MyOnClickListener(0));
        tab1.setOnClickListener(new MyOnClickListener(1));
        tab2.setOnClickListener(new MyOnClickListener(2));
        image = (ImageView) findViewById(R.id.img_tab_now);
        
        text0 = (TextView) findViewById(R.id.text_list);
        text1 = (TextView) findViewById(R.id.text_map);
        text2 = (TextView) findViewById(R.id.text_settings);
        text0.setOnClickListener(new MyOnClickListener(0));
        text1.setOnClickListener(new MyOnClickListener(1));
        text2.setOnClickListener(new MyOnClickListener(2));
        
        addView = (ImageView) findViewById(R.id.add_event_button);
        addText = (TextView) findViewById(R.id.add_event_text);
        addView.setOnTouchListener(new MyOnTouchListener());
        addText.setOnTouchListener(new MyOnTouchListener());
        
        //get screen pixel to fulfill animation
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        animationShiftOneScale = screenWidth / 3;
        animationShiftTwoScale = animationShiftOneScale * 2;
        
        
        //initialize views for each tab
        LayoutInflater myLayout = LayoutInflater.from(this);
        View view0 = myLayout.inflate(R.layout.tab_calendar, null);
        View view1 = myLayout.inflate(R.layout.tab_map, null);
        View view2 = myLayout.inflate(R.layout.tab_settings, null);
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view0);
        views.add(view1);
        views.add(view2);
        
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
		//Set up map
		MyLocalendar = new MyGoogleMap(this);
		MyLocalendar.setMap(((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(), internetConnectionDetector, gpsDetector);	
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
	    if(currentTabIndex == 1) {
	    	getMenuInflater().inflate(R.menu.map_menu, menu);
	    }
	    
	    return super.onPrepareOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.map_normal :
			MyLocalendar.getMyGoogleMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case R.id.map_satellite :
			MyLocalendar.getMyGoogleMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case R.id.map_hybrid :
			MyLocalendar.getMyGoogleMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
			return true;
		}
		return false;
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
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
		case R.id.add_event_text: 
		case R.id.add_event_button:{
			Intent intent = new Intent(Intent.ACTION_INSERT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra(Events.TITLE, "");
			intent.putExtra(Events.EVENT_LOCATION, "");
			intent.putExtra(Events.DESCRIPTION, "");

			// Setting dates
			GregorianCalendar calDate = new GregorianCalendar(2014, 3, 10);
			intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,calDate.getTimeInMillis());
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,calDate.getTimeInMillis());

			// Make it a full day event
			intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);

			// Make it a recurring Event
			//intent.putExtra(Events.RRULE,"FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");

			// Making it private and shown as busy
			intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
			intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);

			startActivity(intent);
			break;
		}
		}
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	/* Class to be defined
	 * 1. MyOnClickListener (change pager index)
	 * 2. MyOnPageChangeListener (tab change)
	 * 3. MyOnTouchListener (add event button)
	 */
	

	
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			if(index!=0)
			pager.setCurrentItem(index);
			else{
				  long startMillis ;
		    	  GregorianCalendar calDate = new GregorianCalendar(2014, 2, 9);
		    	  startMillis = calDate.getTimeInMillis();
		    	  Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
		    	  builder.appendPath("time");
		    	  ContentUris.appendId(builder, startMillis);
		    	  Intent intent = new Intent(Intent.ACTION_VIEW);
		    	  intent.setData(builder.build());
		    	  startActivity(intent);
			}
		}
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			Animation animation = null;
			invalidateOptionsMenu();
			switch (index) {
			case 0:
				actionBar.setTitle("Calendar");
				tab0.setImageDrawable(getResources().getDrawable(R.drawable.tab_list_pressed));
				text0.setTextColor(Color.parseColor("#3399FF"));
				if (currentTabIndex == 1) {
					animation = new TranslateAnimation(animationShiftOneScale, 0, 0, 0);
					tab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_normal));
					text1.setTextColor(Color.parseColor("#000000"));
				} else if (currentTabIndex == 2) {
					animation = new TranslateAnimation(animationShiftTwoScale, 0, 0, 0);
					tab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
					text2.setTextColor(Color.parseColor("#000000"));
				}
				break;
			case 1:
				actionBar.setTitle("Map");
				tab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_pressed));
				text1.setTextColor(Color.parseColor("#3399FF"));
				if (currentTabIndex == 0) {
					animation = new TranslateAnimation(0, animationShiftOneScale, 0, 0);
					tab0.setImageDrawable(getResources().getDrawable(R.drawable.tab_list_normal));
					text0.setTextColor(Color.parseColor("#000000"));
				} else if (currentTabIndex == 2) {
					animation = new TranslateAnimation(animationShiftTwoScale, animationShiftOneScale, 0, 0);
					tab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
					text2.setTextColor(Color.parseColor("#000000"));
				}
				break;
			case 2:
				actionBar.setTitle("Settings");
				tab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				text2.setTextColor(Color.parseColor("#3399FF"));
				if (currentTabIndex == 0) {
					animation = new TranslateAnimation(0, animationShiftTwoScale, 0, 0);
					tab0.setImageDrawable(getResources().getDrawable(R.drawable.tab_list_normal));
					text0.setTextColor(Color.parseColor("#000000"));
				} else if (currentTabIndex == 1) {
					animation = new TranslateAnimation(animationShiftOneScale, animationShiftTwoScale, 0, 0);
					tab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_normal));
					text1.setTextColor(Color.parseColor("#000000"));
				}
				break;
				
			}
			currentTabIndex = index;
			animation.setFillAfter(true);
			animation.setDuration(250);
			image.startAnimation(animation);
		}
		
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		
	}
	
	public class MyOnTouchListener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch(event.getAction())
            {
            case MotionEvent.ACTION_DOWN :
            	addView.setImageResource(R.drawable.add_pressed);
            	addText.setTextColor(Color.parseColor("#3399FF"));
                break;
            case MotionEvent.ACTION_UP :
            	addView.setImageResource(R.drawable.add_normal);
            	addText.setTextColor(Color.parseColor("#000000"));
                break;
            }
			return false;
		}
		
	}
}
