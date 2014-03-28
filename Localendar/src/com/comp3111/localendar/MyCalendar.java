package com.comp3111.localendar;

import java.util.GregorianCalendar;

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
import android.widget.TimePicker;
import android.widget.Toast;
import static android.provider.BaseColumns._ID;
import static com.comp3111.localendar.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.DatabaseConstants.TITLE;
import static com.comp3111.localendar.DatabaseConstants.DESCRIPTION;
import static com.comp3111.localendar.DatabaseConstants.DATE;
import static com.comp3111.localendar.DatabaseConstants.TIME;
import static com.comp3111.localendar.DatabaseConstants.DURATION_HOUR;
import static com.comp3111.localendar.DatabaseConstants.DURATION_MINUTE;
import static com.comp3111.localendar.DatabaseConstants.TRANSPORTATION;
import static com.comp3111.localendar.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.DatabaseConstants.COMPULSORY;

public class MyCalendar extends Fragment {

	public static MyCalendar instance = null;
	private static DatabaseHelper dbhelper;
	private static ListView eventList;
	private View view;
	private static Cursor cursor;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		instance = this;
		view = inflater.inflate(R.layout.day_fragment, container, false); 
		dbhelper = new DatabaseHelper(this.getActivity());
		eventList = (ListView) view.findViewById(R.id.events_list);
		
		refresh();
        return view;
    }  
	
	private static void refresh() {
		eventList.setAdapter(null);
		
		String[] from = {_ID, TITLE, DESCRIPTION, DATE, TIME, DURATION_HOUR, DURATION_MINUTE};
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		cursor = db.query(TABLE_NAME, from, null, null, null, null, null);
		
		int[] to = {R.id.item_id, R.id.item_title, R.id.item_description, R.id.item_date, R.id.item_time, R.id.item_dhour, R.id.item_dminute};
		@SuppressWarnings("deprecation")
		final SimpleCursorAdapter adapter = new SimpleCursorAdapter(instance.getActivity(), R.layout.event_listitem, cursor, from, to);
        eventList.setAdapter(adapter);
        eventList.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	
	public static class AddEventActivity extends Activity implements OnClickListener{
		
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
		private int year, month, day, hour, minute, dhour, dminute;
		
		protected void onCreate(Bundle savedInstanceState) {	
			super.onCreate(savedInstanceState);
			setContentView(R.layout.add_event_dialog);
			getActionBar().hide();
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
			eventTitle = (EditText) findViewById(R.id.event_title);
			eventDescription = (EditText) findViewById(R.id.event_description);
			eventDate = (DatePicker) findViewById(R.id.date_picker);
			eventTime = (TimePicker) findViewById(R.id.time_picker);
			eventHour = (EditText) findViewById(R.id.duration_hour);
			eventMinute = (EditText) findViewById(R.id.duration_minute);
		}

		@Override
		public void onClick(View v) {
			if(getResources().getResourceEntryName(v.getId()).equals("confirm_add")) {
				title = eventTitle.getText().toString();
				if(title.equals("")) {	
					Toast.makeText(instance.getActivity(), "Please input the title", Toast.LENGTH_SHORT).show();
					return;
				}
				description = eventDescription.getText().toString();
				year = eventDate.getYear();
				month = eventDate.getMonth();
				day = eventDate.getDayOfMonth();
				hour = eventTime.getCurrentHour();
				minute = eventTime.getCurrentMinute();
				if(eventHour.getText().toString().equals("")) {
					Toast.makeText(instance.getActivity(), "Please input the duration hour", Toast.LENGTH_SHORT).show();
					return;
				}
				dhour = Integer.parseInt((eventHour.getText().toString()));
				if(eventMinute.getText().toString().equals("")) {
					Toast.makeText(instance.getActivity(), "Please input the duration minute", Toast.LENGTH_SHORT).show();
					return;
				}
				dminute = Integer.parseInt((eventMinute.getText().toString()));
				
				add(); 
				refresh();
			}
			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
		}
		
		private void add(){
	        SQLiteDatabase db = dbhelper.getWritableDatabase();
	        ContentValues values = new ContentValues();
	        values.put(TITLE, title);
	        values.put(DESCRIPTION, description);
	        values.put(DATE, Integer.toString(year) + Integer.toString(month) + Integer.toString(day));
	        values.put(TIME, Integer.toString(hour) + Integer.toString(minute));
	        values.put(DURATION_HOUR, dhour);
	        values.put(DURATION_MINUTE, dminute);
	        db.insert(TABLE_NAME, null, values);
	    }
		
	}
	
	public static class MyOnItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String rid = "";
			String title = "";
			if (cursor.moveToPosition(position)) {
				rid = cursor.getString(cursor.getColumnIndex(_ID));
				title = cursor.getString(cursor.getColumnIndex(TITLE));

    		}
			delete(rid);
			refresh();
	        Toast.makeText(instance.getActivity(), title + " Removed", Toast.LENGTH_SHORT).show();
		}
		
		private void delete(String id) {
			SQLiteDatabase db = dbhelper.getWritableDatabase();
	        db.delete(TABLE_NAME, _ID + "=" + id, null);
		}	
    	
	}

}
