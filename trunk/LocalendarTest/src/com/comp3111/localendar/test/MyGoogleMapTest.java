package com.comp3111.localendar.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.robotium.solo.Solo;

public class MyGoogleMapTest extends ActivityInstrumentationTestCase2<Localendar> {

	Solo solo;
	
	private Localendar localendar;
	private RadioButton calendar, map, settings;
	private Button add;
	private ClearableAutoCompleteTextView searchBox;
	private ImageView searchIcon;
	
	public MyGoogleMapTest() {
		super(Localendar.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    setActivityInitialTouchMode(false);
	    
	    solo = new Solo(getInstrumentation(), getActivity());
		localendar = (Localendar) getActivity();
		add = (Button) localendar.findViewById(R.id.add_button);
		calendar = (RadioButton) localendar.findViewById(R.id.calendar_button);
		map = (RadioButton) localendar.findViewById(R.id.map_button);
		settings = (RadioButton) localendar.findViewById(R.id.settings_button);
		searchBox = (ClearableAutoCompleteTextView) localendar.findViewById(R.id.search_box);
	    searchIcon = (ImageView) localendar.findViewById(R.id.place_view);
	}
	
	public void testMap() {
		
	}
}

