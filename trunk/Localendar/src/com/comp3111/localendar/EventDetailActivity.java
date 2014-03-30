package com.comp3111.localendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import static com.comp3111.localendar.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.DatabaseConstants.TITLE;
import static com.comp3111.localendar.DatabaseConstants.DESCRIPTION;
import static com.comp3111.localendar.DatabaseConstants.DATE;
import static com.comp3111.localendar.DatabaseConstants.HOUR;
import static com.comp3111.localendar.DatabaseConstants.MINUTE;
import static com.comp3111.localendar.DatabaseConstants.DURATION_HOUR;
import static com.comp3111.localendar.DatabaseConstants.DURATION_MINUTE;
import static com.comp3111.localendar.DatabaseConstants.TRANSPORTATION;
import static com.comp3111.localendar.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.DatabaseConstants.COMPULSORY;


public class EventDetailActivity extends Activity implements OnClickListener {
	private String id;
	private TextView titleView, descriptionView, timeView, durationView;
	private String title, description, date, hour, minute, dhour, dminute;
	private Button deleteButton;
	private SQLiteDatabase db;
	
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		id = intent.getExtras().getString("ID");
		setContentView(R.layout.event_detail);
		
		titleView = (TextView) findViewById(R.id.detail_title);
		descriptionView = (TextView) findViewById(R.id.detail_description);
		timeView = (TextView) findViewById(R.id.detail_time);
		durationView = (TextView) findViewById(R.id.detail_duration);
		deleteButton = (Button) findViewById(R.id.delete_event);
		
		db = MyCalendar.dbhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
	               "SELECT " + TITLE + "," + DESCRIPTION + ","
	            		   + DATE + "," + HOUR + "," + MINUTE + ","
	            		   + DURATION_HOUR + "," + DURATION_MINUTE + 
	               " FROM " + TABLE_NAME + " WHERE _ID=?",
	               new String[]{id});
		while (cursor.moveToNext()) {
		    title = cursor.getString(0);
		    description = cursor.getString(1);
		    date = cursor.getString(2);
		    hour = cursor.getString(3);
		    minute = cursor.getString(4);
		    dhour = cursor.getString(5);
		    dminute = cursor.getString(6);
		}
		titleView.setText(title);
		descriptionView.setText(description);
		timeView.setText(date + hour + ":" + minute);
		durationView.setText(dhour + ":" + dminute);
		deleteButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.delete_event: {
			MyCalendar.deleteEvent(id);
			MyCalendar.calendarInstance.refresh();
			this.finish();
		}
		break;
		}
	}
}
