package com.comp3111.localendar.calendar;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.R.anim;
import com.comp3111.localendar.R.id;
import com.comp3111.localendar.R.layout;
import com.comp3111.localendar.R.menu;
import com.comp3111.localendar.map.MyGoogleMap;
import com.comp3111.localendar.map.Place;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import static com.comp3111.localendar.database.DatabaseConstants.COMPULSORY;
import static com.comp3111.localendar.database.DatabaseConstants.DAY;
import static com.comp3111.localendar.database.DatabaseConstants.DESCRIPTION;
import static com.comp3111.localendar.database.DatabaseConstants.DURATION_HOUR;
import static com.comp3111.localendar.database.DatabaseConstants.DURATION_MINUTE;
import static com.comp3111.localendar.database.DatabaseConstants.HOUR;
import static com.comp3111.localendar.database.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.database.DatabaseConstants.MINUTE;
import static com.comp3111.localendar.database.DatabaseConstants.MONTH;
import static com.comp3111.localendar.database.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.database.DatabaseConstants.TITLE;
import static com.comp3111.localendar.database.DatabaseConstants.TRANSPORTATION;
import static com.comp3111.localendar.database.DatabaseConstants.YEAR;


public class EventDetailActivity extends Activity {
	private String id;
	private TextView titleView, descriptionView, timeView, durationView, locationView, transportationView, compulsoryView;
	private String title, description, year, month, day, hour, minute, dhour, dminute, location, transportation, compulsory;
	private SQLiteDatabase db;
	
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		id = intent.getExtras().getString("ID");
		setContentView(R.layout.event_detail);
		
		ActionBar actionBar = getActionBar();
        
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Event Details");
        
        titleView = (TextView) findViewById(R.id.detail_title);
		descriptionView = (TextView) findViewById(R.id.detail_description);
		timeView = (TextView) findViewById(R.id.detail_time);
		durationView = (TextView) findViewById(R.id.detail_duration);
		locationView = (TextView) findViewById(R.id.detail_location);
		transportationView = (TextView) findViewById(R.id.detail_transportation);
		compulsoryView = (TextView) findViewById(R.id.detail_compulsory);
		
		db = MyCalendar.dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
	               "SELECT " + TITLE + ", " + DESCRIPTION + ", "
	            		   + YEAR + ", " + MONTH + ", " + DAY + ", "
	            		   + HOUR + ", " + MINUTE + ", "
	            		   + DURATION_HOUR + ", " + DURATION_MINUTE + ", "
	            		   + LOCATION + ", " + TRANSPORTATION + ", "
	            		   + COMPULSORY +
	               " FROM " + TABLE_NAME + 
	               " WHERE _ID=?", new String[]{id});
		while (cursor.moveToNext()) {
		    title = cursor.getString(0);
		    description = cursor.getString(1);
		    year = cursor.getString(2);
		    month = cursor.getString(3);
		    day = cursor.getString(4);
		    hour = cursor.getString(5);
		    minute = cursor.getString(6);
		    dhour = cursor.getString(7);
		    dminute = cursor.getString(8);
		    location = cursor.getString(9);
		    transportation = cursor.getString(10);
		    compulsory = cursor.getString(11);
		}
		
		titleView.setText(title);
		descriptionView.setText(description);
		timeView.setText(month + "/" + day + "/" + year + " " + hour + ":" + minute);
		durationView.setText(dhour + " hr " + dminute + " m");
		locationView.setText(location);
		transportationView.setText(transportation);
		compulsoryView.setText(compulsory);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
	    switch(menuItem.getItemId()) {
	    case R.id.edit_event: {
	    	Intent intent = new Intent (this, EditEventActivity.class);	
			intent.putExtra("ID", id);
			startActivity(intent);
			this.finish();
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
//			MyGoogleMap.refresh();
			return true;
	    }
	    case R.id.delete_event: {
	    	MyCalendar.deleteEvent(id);
			MyCalendar.calendarInstance.refresh();
			MyGoogleMap.refresh(id);
			this.finish();
			return true;
	    }
	    case R.id.addmarker:{
	    	Place mlocation= Place.getPlaceFromAddress(location);
//	    	if(MyGoogleMap.addmarker(mlocation, true)) {
		    if(MyGoogleMap.addmarker(mlocation, true, new String(id + "." + title), new String(hour + ":" + minute))) {
	    		Localendar.instance.setPagerIndex(1);
				this.finish();
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
				return true;
	    	}
	    	else
	    		return false;
	    }
	    default: {
	    	onBackPressed();
		    return true;
	    }
	    }
	}
}
