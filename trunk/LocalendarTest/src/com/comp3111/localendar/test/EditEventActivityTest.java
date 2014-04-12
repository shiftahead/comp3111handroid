package com.comp3111.localendar.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.comp3111.localendar.calendar.EditEventActivity;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.ClearableEditText;

public class EditEventActivityTest extends ActivityInstrumentationTestCase2<EditEventActivity> {
	
	private EditEventActivity mActivity;
	private ClearableAutoCompleteTextView eventLocation;
	private ClearableEditText eventTitle;
	private ClearableEditText eventDescription;
	private DatePicker eventDate;
	private TimePicker eventTime;
	private EditText eventHour, eventMinute;
	private Spinner eventTransportation;
	private CheckBox eventCompulsory;
	private Button confirmAdd, cancelAdd;
	
	
		
	public EditEventActivityTest() {
		super(EditEventActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		
		mActivity = getActivity();
		eventLocation = (ClearableAutoCompleteTextView) mActivity.findViewById(com.comp3111.localendar.R.id.event_location);
		eventTitle = (ClearableEditText) mActivity.findViewById(com.comp3111.localendar.R.id.event_title);
		eventDescription = (ClearableEditText) mActivity.findViewById(com.comp3111.localendar.R.id.event_description);
		eventDate = (DatePicker) mActivity.findViewById(com.comp3111.localendar.R.id.date_picker);
		eventTime = (TimePicker) mActivity.findViewById(com.comp3111.localendar.R.id.time_picker);
		eventHour = (EditText) mActivity.findViewById(com.comp3111.localendar.R.id.duration_hour);
		eventMinute = (EditText) mActivity.findViewById(com.comp3111.localendar.R.id.duration_minute);
		eventTransportation = (Spinner) mActivity.findViewById(com.comp3111.localendar.R.id.event_transportation);
		eventCompulsory = (CheckBox) mActivity.findViewById(com.comp3111.localendar.R.id.event_compulsory);
		confirmAdd = (Button) mActivity.findViewById(com.comp3111.localendar.R.id.confirm_add);
		cancelAdd = (Button) mActivity.findViewById(com.comp3111.localendar.R.id.cancel_add);		
	}
	
	//TODO: pre-conditions not yet done
	public void testPreConditions() {
		assertTrue(eventTransportation.getOnItemSelectedListener() != null);
	}
	
		
}
