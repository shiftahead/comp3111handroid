package com.comp3111.localendar.calendar;

import com.comp3111.localendar.R;
import com.comp3111.localendar.R.anim;
import com.comp3111.localendar.R.id;
import com.comp3111.localendar.R.layout;
import com.comp3111.localendar.map.MyGoogleMap;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.ClearableEditText;
import com.comp3111.localendar.support.PlacesAutoCompleteAdapter;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
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


public class AddEventActivity extends Activity {
	
	private ClearableEditText eventTitle;
	private ClearableEditText eventDescription;
	private DatePicker eventDate;
	private TimePicker eventTime;
	private EditText eventHour;
	private EditText eventMinute;
	private ClearableAutoCompleteTextView eventLocation;
	private Spinner eventTransportation;
	private CheckBox eventCompulsory;
	private ActionBar actionBar;
	private Button confirmAdd, cancelAdd;
	
	private String title, description;
	private String year, month, day, hour, minute, dhour, dminute, location, transportation, compulsory;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event_dialog);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflator.inflate(R.layout.addevent_actionbar, null);
        actionBar.setCustomView(actionBarView);
		
		eventTitle = (ClearableEditText) findViewById(R.id.event_title);
		eventDescription = (ClearableEditText) findViewById(R.id.event_description);
		eventDate = (DatePicker) findViewById(R.id.date_picker);
		eventTime = (TimePicker) findViewById(R.id.time_picker);
		eventHour = (EditText) findViewById(R.id.duration_hour);
		eventMinute = (EditText) findViewById(R.id.duration_minute);
		eventLocation = (ClearableAutoCompleteTextView) findViewById(R.id.event_location);
		eventTransportation = (Spinner) findViewById(R.id.event_transportation);
		eventCompulsory = (CheckBox) findViewById(R.id.event_compulsory);
		
		eventTime.setIs24HourView(true);
		eventLocation.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_result_list_item));
		
		confirmAdd = (Button) findViewById(R.id.confirm_add);
		cancelAdd = (Button) findViewById(R.id.cancel_add);
		confirmAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if(addEvent()) {
					MyCalendar.calendarInstance.refresh();
			        MyGoogleMap.refresh("");
					finish();
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
				}
			}
		});
		cancelAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				
			}
		});
	}
	
	private boolean addEvent(){
		
        SQLiteDatabase db = MyCalendar.dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        title = eventTitle.getText().toString();
		if(title.equals("")) {	
			Toast.makeText(this, "Please input the title", Toast.LENGTH_SHORT).show();
			return false;
		}
		description = eventDescription.getText().toString();
		year = Integer.toString(eventDate.getYear());
		month = Integer.toString(eventDate.getMonth() + 1);
		day = Integer.toString(eventDate.getDayOfMonth());
		hour = Integer.toString(eventTime.getCurrentHour());
		minute = Integer.toString(eventTime.getCurrentMinute());
		if(hour.length() == 1) hour = "0" + hour;
		if(minute.length() == 1) minute = "0" + minute;
		if(eventHour.getText().toString().equals("")) {
			Toast.makeText(this, "Please input the duration hour", Toast.LENGTH_SHORT).show();
			return false;
		}
		dhour = eventHour.getText().toString();
		if(eventMinute.getText().toString().equals("")) {
			Toast.makeText(this, "Please input the duration minute", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(Integer.parseInt(eventMinute.getText().toString()) > 59) {
			Toast.makeText(this, "Please check the duration minute: " + eventMinute.getText().toString()
					+" is improper", Toast.LENGTH_SHORT).show();
			return false;
		}
		dminute = eventMinute.getText().toString();
		location = eventLocation.getText().toString();
		if(location.equals("")) {
			Toast.makeText(this, "Please input the location", Toast.LENGTH_SHORT).show();
			return false;
		}
		transportation = eventTransportation.getSelectedItem().toString();
		compulsory = eventCompulsory.isChecked()? "YES" : "NO";
		
        values.put(TITLE, title);
        values.put(DESCRIPTION, description);
        values.put(YEAR, year);
        values.put(MONTH, month);
        values.put(DAY, day);
        values.put(HOUR, hour);
        values.put(MINUTE, minute);
        values.put(DURATION_HOUR, dhour);
        values.put(DURATION_MINUTE, dminute);
        values.put(LOCATION, location);
        values.put(TRANSPORTATION, transportation);
        values.put(COMPULSORY, compulsory);
        db.insert(TABLE_NAME, null, values);
        return true;
    }
	
}
