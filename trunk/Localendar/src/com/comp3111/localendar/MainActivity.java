package com.comp3111.localendar;


import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {

	public static MainActivity instance = null;
	
	private ViewPager pager;	
	private ImageView image, tab0, tab1, tab2;
	private int currentTabIndex = 0;
	private LayoutInflater inflator;
	private int animationShiftOneScale, animationShiftTwoScale;
	private GoogleMap localenderMap;
	private Marker testMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
        pager = (ViewPager)findViewById(R.id.tabpager);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
        tab0 = (ImageView) findViewById(R.id.img_list);
        tab1 = (ImageView) findViewById(R.id.img_map);
        tab2 = (ImageView) findViewById(R.id.img_settings);
        tab0.setOnClickListener(new MyOnClickListener(0));
        tab1.setOnClickListener(new MyOnClickListener(1));
        tab2.setOnClickListener(new MyOnClickListener(2));
        image = (ImageView) findViewById(R.id.img_tab_now);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        animationShiftOneScale = metrics.widthPixels / 3;
        animationShiftTwoScale = animationShiftOneScale * 2;
        
        LayoutInflater myLayout = LayoutInflater.from(this);
        View view0 = myLayout.inflate(R.layout.tab_list, null);
        View view1 = myLayout.inflate(R.layout.tab_map, null);
        View view2 = myLayout.inflate(R.layout.tab_settings, null);
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view0);
        views.add(view1);
        views.add(view2);
        
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
		pager.setAdapter(myPagerAdapter);
		pager.setCurrentItem(1);
       
		//map setting
		UiSettings localenderMapSettings = localenderMap.getUiSettings();
        localenderMapSettings.setZoomControlsEnabled(true);
        localenderMapSettings.setCompassEnabled(true);
        localenderMapSettings.setMyLocationButtonEnabled(true);
        localenderMapSettings.setScrollGesturesEnabled(true);
        localenderMapSettings.setRotateGesturesEnabled(true);
        localenderMap.setMyLocationEnabled(true);
        
        //get my location 
        localenderMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                        
                           @Override
                              public boolean onMyLocationButtonClick() {
                                    // TODO Auto-generated method stub
                                    return false;
                              }
                     });  
        
      //Do not add it in the onCreate method!
       testMarker = localenderMap.addMarker(new MarkerOptions()
       .position(new LatLng(22.3375, 114.2630))
       .title("COMP3111H Lecture").snippet("Today\n15:00 - 16:30\nLT-E").draggable(true));
        
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
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
				if (currentTabIndex == 1) {
					animation = new TranslateAnimation(animationShiftOneScale, 0, 0, 0);
					tab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_normal));
				} else if (currentTabIndex == 2) {
					animation = new TranslateAnimation(animationShiftTwoScale, 0, 0, 0);
					tab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				tab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_pressed));
				if (currentTabIndex == 0) {
					animation = new TranslateAnimation(0, animationShiftOneScale, 0, 0);
					tab0.setImageDrawable(getResources().getDrawable(R.drawable.tab_list_normal));
				} else if (currentTabIndex == 2) {
					animation = new TranslateAnimation(animationShiftTwoScale, animationShiftOneScale, 0, 0);
					tab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
				tab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currentTabIndex == 0) {
					animation = new TranslateAnimation(0, animationShiftTwoScale, 0, 0);
					tab0.setImageDrawable(getResources().getDrawable(R.drawable.tab_list_normal));
				} else if (currentTabIndex == 1) {
					animation = new TranslateAnimation(animationShiftOneScale, animationShiftTwoScale, 0, 0);
					tab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_map_normal));
				}
				break;
				
			}
			currentTabIndex = index;
			animation.setFillAfter(true);
			animation.setDuration(150);
			image.startAnimation(animation);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
}
