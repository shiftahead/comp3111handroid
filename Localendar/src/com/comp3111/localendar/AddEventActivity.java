package com.comp3111.localendar;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import static com.comp3111.localendar.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.DatabaseConstants.TITLE;
import static com.comp3111.localendar.DatabaseConstants.DESCRIPTION;
import static com.comp3111.localendar.DatabaseConstants.YEAR;
import static com.comp3111.localendar.DatabaseConstants.MONTH;
import static com.comp3111.localendar.DatabaseConstants.DAY;
import static com.comp3111.localendar.DatabaseConstants.HOUR;
import static com.comp3111.localendar.DatabaseConstants.MINUTE;
import static com.comp3111.localendar.DatabaseConstants.DURATION_HOUR;
import static com.comp3111.localendar.DatabaseConstants.DURATION_MINUTE;
import static com.comp3111.localendar.DatabaseConstants.TRANSPORTATION;
import static com.comp3111.localendar.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.DatabaseConstants.COMPULSORY;


public class AddEventActivity extends Activity implements OnClickListener{
	
	private EditText eventTitle;
	private EditText eventDescription;
	private DatePicker eventDate;
	private TimePicker eventTime;
	private EditText eventHour;
	private EditText eventMinute;
	//private String eventLocation;
	//private String eventVehicle;
	//private boolean eventCompulsory;
	
	private String title, description;
	private String year, month, day, hour, minute, dhour, dminute;
	
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_event_dialog);
		getActionBar().hide();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); 
		eventTitle = (EditText) findViewById(R.id.event_title);
		eventDescription = (EditText) findViewById(R.id.event_description);
		eventDate = (DatePicker) findViewById(R.id.date_picker);
		eventTime = (TimePicker) findViewById(R.id.time_picker);
		eventHour = (EditText) findViewById(R.id.duration_hour);
		eventMinute = (EditText) findViewById(R.id.duration_minute);
		eventTime.setIs24HourView(true);
	}

	@Override
	public void onClick(View v) {
		if(getResources().getResourceEntryName(v.getId()).equals("confirm_add")) {
			title = eventTitle.getText().toString();
			if(title.equals("")) {	
				Toast.makeText(this, "Please input the title", Toast.LENGTH_SHORT).show();
				return;
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
				return;
			}
			dhour = eventHour.getText().toString();
			if(eventMinute.getText().toString().equals("")) {
				Toast.makeText(this, "Please input the duration minute", Toast.LENGTH_SHORT).show();
				return;
			}
			if(Integer.parseInt(eventMinute.getText().toString()) > 59) {
				Toast.makeText(this, "Please check the duration minute: " + eventMinute.getText().toString()
						+" is improper", Toast.LENGTH_SHORT).show();
				return;
			}
			dminute = eventMinute.getText().toString();
			
			addEvent(); 
			MyCalendar.calendarInstance.refresh();
		}
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	private void addEvent(){
        SQLiteDatabase db = MyCalendar.dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(DESCRIPTION, description);
        values.put(YEAR, year);
        values.put(MONTH, month);
        values.put(DAY, day);
        values.put(HOUR, hour);
        values.put(MINUTE, minute);
        values.put(DURATION_HOUR, dhour);
        values.put(DURATION_MINUTE, dminute);
        db.insert(TABLE_NAME, null, values);
    }
	
}
