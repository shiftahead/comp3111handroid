package com.comp3111.localendar;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3111.localendar.ClearableAutoCompleteTextView.OnClearListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class MainActivity extends Activity implements OnClickListener, OnCheckedChangeListener, OnItemClickListener {
	
	public static MainActivity instance = null;
	private ActionBar actionBar;
	private NonSwipeableViewPager pager;	//view pager
	private int currentTabIndex;	//tab index, maps is set default 
	private RadioButton mapButton, settingsButton;
	private Button addButton, calendarButton;
	private ClearableAutoCompleteTextView searchBox;
	private boolean searchBoxShown;
	private ImageView searchIcon;
	//Search result will returned as this string attribute
	String PlaceSearch;
	AutoCompleteTextView MapSearchAutoCompTextView;
	//The place will be transferred to the 2 double latitude & longitude
	Double latituedPlace;
	Double longitudePlace;
	
	// MyLocalendar is map object
	MyGoogleMap MyLocalendar;
	
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
	
	//Attributes for the searchAutoComplete function
    private static final String LOG_TAG = "Localendar";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyAQ_VeQa3QVGxD3C7UxFAjHTF-bUsSC6Y4";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// map activities  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//hide the keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        instance = this;
        
		//set action bar
        actionBar = getActionBar();
        
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflator.inflate(R.layout.map_actionbar, null);
        
        actionBar.setCustomView(actionBarView);
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
        searchBox.setOnClearListener(new OnClearListener() {
    		
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
	    searchBox.setVisibility(View.VISIBLE);
	    searchIcon.setVisibility(View.GONE);
	    
        //initialize pager and tab buttons
        pager = (NonSwipeableViewPager)findViewById(R.id.tabpager);
        pager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        addButton = (Button) findViewById(R.id.add_button);
        calendarButton = (Button) findViewById(R.id.calendar_button);
        mapButton = (RadioButton) findViewById(R.id.map_button);
        settingsButton = (RadioButton) findViewById(R.id.settings_button);
        
        addButton.setOnClickListener(this);
        calendarButton.setOnClickListener(this);
        mapButton.setOnCheckedChangeListener(this);  
        settingsButton.setOnCheckedChangeListener(this);  
        
        /*
        //get screen pixel to fulfill animation
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        animationShiftOneScale = screenWidth / 3;
        animationShiftTwoScale = animationShiftOneScale * 2;
        */
        
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
		
		//Set up map
		MyLocalendar = new MyGoogleMap(this);
		MyLocalendar.setMap(((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap());	
	}
	
	
	//Click the items on the autocomplete list and the result will be allocated to the String and Doubles
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    	PlaceSearch = (String) adapterView.getItemAtPosition(position);
        getLatLongFromAddress(PlaceSearch);
        MyLocalendar.addmarker(latituedPlace, longitudePlace, true);
        Toast.makeText(this, PlaceSearch, Toast.LENGTH_SHORT).show();
    }
    
    @Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {  
			//invalidateOptionsMenu();
            switch (buttonView.getId()) {  
              
            case R.id.map_button: {
                pager.setCurrentItem(1);
                searchBoxShown = true;
        	    searchBox.setVisibility(View.VISIBLE);
        	    searchIcon.setVisibility(View.GONE);
                /*
                if (currentTabIndex == 2)
                	overridePendingTransition(R.anim.left_in, R.anim.left_out);
                else if (currentTabIndex == 0)
                	overridePendingTransition(R.anim.right_in, R.anim.right_out);
                	*/
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                currentTabIndex = 1;
            }
                break;  
            case R.id.settings_button: {
            		searchBoxShown = false;
        		    searchBox.setVisibility(View.INVISIBLE);
        		    searchIcon.setVisibility(View.INVISIBLE);
            	
                pager.setCurrentItem(2);  
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
			/*
			Intent intent = new Intent(this, AddEventActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			*/
			startEventDialog();
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
			break;
		case R.id.calendar_button: {
			startCalendar();
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
			break;
    	
    	
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
    	}
		
	}
    
	/* set the pop up menu
	 * in this menu people can set up map type
	 */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    menu.clear();
	    
	    if(currentTabIndex == 1) {
	    	getMenuInflater().inflate(R.menu.main_menu, menu);
	    }
	    else {
	    	getMenuInflater().inflate(R.menu.calendar, menu);
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

	//AutoComplete adapter used for the autocompletetextview
    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }
        
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }

    //Autocomplete function, pass a input and output a Arraylist<String> of places
    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?sensor=false&key=" + API_KEY);
            //sb.append("&components=country:uk");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    //Input a string and get the place's Latitude and Longtitude
    private void getLatLongFromAddress(String youraddress) {
    	Geocoder geoCoder = new Geocoder(this);
    	
    	try {
            List<Address> addresses = geoCoder.getFromLocationName(youraddress, 1); 
            if (addresses.size() >  0) {
            	latituedPlace = addresses.get(0).getLatitude(); 
            	longitudePlace = addresses.get(0).getLongitude();
            }

        } catch (IOException e) { // TODO Auto-generated catch block
        e.printStackTrace(); }
    }
    
    private void startEventDialog() {
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
	}
    
    private void startCalendar() {
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
