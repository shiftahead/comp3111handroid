package com.comp3111.localendar.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.comp3111.localendar.*;
import com.comp3111.localendar.R;
import com.comp3111.localendar.support.*;

public class LocalendarTest extends ActivityInstrumentationTestCase2<Localendar> {

	private Localendar localendar;
	private RadioButton calendar, map, settings;
	private ClearableAutoCompleteTextView searchBox;
	private ImageView searchIcon;
	
	public LocalendarTest() {
		super(Localendar.class);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		localendar = (Localendar) getActivity();
		calendar = (RadioButton) localendar.findViewById(R.id.calendar_button);
		map = (RadioButton) localendar.findViewById(R.id.map_button);
		settings = (RadioButton) localendar.findViewById(R.id.settings_button);
		searchBox = (ClearableAutoCompleteTextView) localendar.findViewById(R.id.search_box);
	    searchIcon = (ImageView) localendar.findViewById(R.id.place_view);
	    
	}
	 
	public void testView() { 
		// checks if the activity is created
		assertNotNull(getActivity());
	}
	
	public void testRadioButtons() {
		TouchUtils.tapView(this, calendar);
		TouchUtils.tapView(this, map);
		TouchUtils.tapView(this, settings);
		TouchUtils.tapView(this, map);
		TouchUtils.tapView(this, calendar);
		TouchUtils.tapView(this, calendar);
		TouchUtils.tapView(this, map);
		TouchUtils.tapView(this, settings);
		TouchUtils.tapView(this, map);
		TouchUtils.tapView(this, calendar);
		TouchUtils.tapView(this, map);
	}
	
	public void testActionBar() {
		TouchUtils.tapView(this, map);
		assertTrue(View.VISIBLE == searchIcon.getVisibility());
		assertTrue(View.GONE == searchBox.getVisibility());
		TouchUtils.tapView(this, searchIcon);
		assertTrue(View.GONE == searchIcon.getVisibility());
		assertTrue(View.VISIBLE == searchBox.getVisibility());
	}
}
