package com.comp3111.localendar;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
	private ViewPager pager;	//view pager
	private ImageView image, tab0, tab1, tab2, addView;	//tabs for list, map and settings
	private TextView text0, text1, text2, addText;	//text tabs for list, map and settings
	private int currentTabIndex;	//tab index, maps is set default 
	private int animationShiftOneScale, animationShiftTwoScale;	//animation effect
	private int screenWidth;
	
	// MyLocalendar is map object
	MyGoogleMap MyLocalendar;
	ConnectionDetector internetConnectionDetector;
	GPSTracker gpsDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// map activities  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//hide the keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        //initialize pager and tabs
        pager = (ViewPager)findViewById(R.id.tabpager);
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
        View view0 = myLayout.inflate(R.layout.tab_list, null);
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
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.main, menu); 
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.mapNormal :
			MyLocalendar.getMyGoogleMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case R.id.mapSatellite :
			MyLocalendar.getMyGoogleMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case R.id.mapHybrid :
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
		case R.id.add_event_button:
		case R.id.add_event_text: {
			Intent intent = new Intent (MainActivity.this, AddEventActivity.class);			
			startActivity(intent);
		}
		break;
		}
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	/* Class to be defined
	 * 1. MyOnClickListener (for pager index)
	 * 2. MyOnPageChangeListener (for tab change)
	 * 3. MyOnTouchListener (for add event button)
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);
		}
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			Animation animation = null;
			switch (index) {
			case 0:
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
