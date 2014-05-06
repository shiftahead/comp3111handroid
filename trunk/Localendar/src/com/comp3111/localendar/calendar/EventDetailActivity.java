package com.comp3111.localendar.calendar;

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

import java.util.GregorianCalendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.facebook.FacebookLogin;
import com.comp3111.localendar.map.MyGoogleMap;
import com.comp3111.localendar.map.Place;
import com.google.android.gms.plus.PlusShare;


public class EventDetailActivity extends Activity {
	private static String TAG = "EventDetail";
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
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
		    if(MyGoogleMap.addmarker(mlocation, true, id, title, new String(hour + ":" + minute))) {
	    		Localendar.instance.setPagerIndex(1);
				this.finish();
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				return true;
	    	}
	    	else
	    		return false;
	    }
	    case R.id.share_to_fb:{
	    	Intent intent = new Intent(this, FacebookLogin.class);
	    	intent.putExtra("TITLE", title);
	    	intent.putExtra("DESCRIPTION", description);
	    	intent.putExtra("TIME", month + "/" + day + "/" + year + " " + hour + ":" + minute);
	    	intent.putExtra("LOCATION", location);
	    	startActivity(intent);
	    	return true;
	    }
	    case R.id.share_to_googlePlus :{
	         shareToGooglePlus();	
	    }
	    default: {
	    	onBackPressed();
		    return true;
	    }
	    }
	}
    public void shareToGooglePlus() {
    	String shareTextString = title +" happens at"+ description;
    	PlusShare.Builder share = new PlusShare.Builder(this);
    	share.setText(shareTextString);
    	share.setContentUrl(Uri.parse("https://plus.google.com/"));
    	share.setType("text/plain");
    	startActivityForResult(share.getIntent(), 0);
	}
    
    public void addeventInGooglecalendar() {
    	
    	Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, title);
		intent.putExtra(Events.EVENT_LOCATION, location);
		intent.putExtra(Events.DESCRIPTION, description);

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
}
