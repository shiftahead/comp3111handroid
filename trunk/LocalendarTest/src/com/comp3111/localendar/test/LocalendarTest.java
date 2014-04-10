package com.comp3111.localendar.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.comp3111.localendar.*;

public class LocalendarTest extends ActivityInstrumentationTestCase2<Localendar> {

	private Localendar localendar;
	private RadioButton calendar, map, settings;
	private ImageView place;
	public LocalendarTest() {
		super(Localendar.class);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		localendar = (Localendar) getActivity();
		calendar = (RadioButton) localendar.findViewById(com.comp3111.localendar.R.id.calendar_button);
		map = (RadioButton) localendar.findViewById(com.comp3111.localendar.R.id.map_button);
		settings = (RadioButton) localendar.findViewById(com.comp3111.localendar.R.id.settings_button);
		place = (ImageView) localendar.findViewById(com.comp3111.localendar.R.id.place_view);
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
		TouchUtils.tapView(this, place);
		sendKeys("hkust");
		
	}
}
