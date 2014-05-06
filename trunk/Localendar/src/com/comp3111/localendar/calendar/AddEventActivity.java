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

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.comp3111.localendar.R;
import com.comp3111.localendar.SettingsFragment;
import com.comp3111.localendar.map.MyGoogleMap;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.ClearableEditText;
import com.comp3111.localendar.support.PlacesAutoCompleteAdapter;


public class AddEventActivity extends Activity {
	
	private ClearableEditText eventTitle;
	private ClearableEditText eventDescription;
	private DatePicker eventDate;
	private TimePicker eventTime;
	private EditText eventHour;
	private EditText eventMinute;
	private ClearableAutoCompleteTextView eventLocation;
	private Spinner eventTransportation;
	private Spinner eventReminderTime;
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
        
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String defaultDuration = sharedPreferences.getString(SettingsFragment.DURATION, "1.5");
        String defaultTransportation = sharedPreferences.getString(SettingsFragment.TRANSPORTATION, "Drive");
        boolean isCompulsory = sharedPreferences.getBoolean(SettingsFragment.IS_COMPULSORY, false);
        
		
		eventTitle = (ClearableEditText) findViewById(R.id.event_title);
		eventDescription = (ClearableEditText) findViewById(R.id.event_description);
		eventDate = (DatePicker) findViewById(R.id.date_picker);
		eventTime = (TimePicker) findViewById(R.id.time_picker);
		eventHour = (EditText) findViewById(R.id.duration_hour);
		eventMinute = (EditText) findViewById(R.id.duration_minute);
		String text = getIntent().getStringExtra("location");
		eventLocation = (ClearableAutoCompleteTextView) findViewById(R.id.event_location);
		eventLocation.setText(text);
		eventTransportation = (Spinner) findViewById(R.id.event_transportation);
		eventReminderTime = (Spinner) findViewById(R.id.remind_time);
		eventCompulsory = (CheckBox) findViewById(R.id.event_compulsory);
		
		if (defaultDuration.equals("0.25")) {
			eventHour.setText("0");
			eventMinute.setText("15");
		} else if (defaultDuration.equals("0.5")) {
			eventHour.setText("0");
			eventMinute.setText("30");
		} else if (defaultDuration.equals("1")) {
			eventHour.setText("1");
			eventMinute.setText("0");
		} else if (defaultDuration.equals("1.5")) {
			eventHour.setText("1");
			eventMinute.setText("30");
		} else if (defaultDuration.equals("2")) {
			eventHour.setText("2");
			eventMinute.setText("0");
		}
		
		if (defaultTransportation.equals("Drive")) {
			eventTransportation.setSelection(0);
		} else if (defaultTransportation.equals("Public transportation")) {
			eventTransportation.setSelection(1);
		} else if (defaultTransportation.equals("On foot")) {
			eventTransportation.setSelection(2);
		}
		
		if (isCompulsory) {
			eventCompulsory.setChecked(true);
		} else {
			eventCompulsory.setChecked(false);
		}
			
		
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
		
		if(eventCompulsory.isChecked() && eventReminderTime.getSelectedItemPosition() == 0){
			Toast.makeText(this, "Compulsory event must be reminded", Toast.LENGTH_SHORT).show();
			return false;			
		}

		if(eventReminderTime.getSelectedItemPosition() != 0){
			long remindtime = getRemindTime(eventReminderTime.getSelectedItemPosition());
			scheduleAlarm(remindtime);
		}
		
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
	
    private static final float Min_to_Millis = 60000;
    private static final float Hour_to_Millis = 3600000; 

    private long getRemindTime(int id) {
		// TODO Auto-generated method stub
    	switch (id) {
		case 1:
			return (long) (5 * Min_to_Millis);
		case 2:
			return (long) (10 * Min_to_Millis);
		case 3:
			return (long) (30 * Min_to_Millis);
		case 4:
			return (long) (1 * Hour_to_Millis);
		case 5:
			return (long) (1.5 * Hour_to_Millis);
		case 6:
			return (long) (2 * Hour_to_Millis);
		default:
			break;
		}
		return 0;
	}

	public void scheduleAlarm(long rt)
    {
    	Calendar cal=Calendar.getInstance();
    	cal.set(Calendar.YEAR,eventDate.getYear());
    	cal.set(Calendar.MONTH,eventDate.getMonth());
    	cal.set(Calendar.DAY_OF_MONTH,eventDate.getDayOfMonth());
    	cal.set(Calendar.HOUR_OF_DAY,eventTime.getCurrentHour());
    	cal.set(Calendar.MINUTE,eventTime.getCurrentMinute());
    	    	    	
    	// create an Intent and set the class which will execute when Alarm triggers
    	Intent intentAlarm = new Intent(this, AlarmReceiverActivity.class);
    	intentAlarm.putExtra("title", title);
    	intentAlarm.putExtra("year", year);
    	intentAlarm.putExtra("month", month);
    	intentAlarm.putExtra("day", day);
    	intentAlarm.putExtra("hour", hour);
    	intentAlarm.putExtra("minute", minute);
    	intentAlarm.putExtra("venue", location);
    	intentAlarm.putExtra("transportation", transportation);
    	intentAlarm.putExtra("ExpectTimeNeeded", rt);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                1, intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
    	
    	// create the object
    	AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    	
    	//set the alarm for particular time
    	alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis() - rt, pendingIntent);
    }
	
}
