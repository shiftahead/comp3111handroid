package com.comp3111.localendar.test;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.PlacesAutoCompleteAdapter;
import com.google.android.gms.drive.internal.am;

public class LocalendarTest extends ActivityInstrumentationTestCase2<Localendar> {

	private Localendar localendar;
	private RadioButton calendar, map, settings;
	private ClearableAutoCompleteTextView searchBox;
	private ImageView searchIcon;
	private MenuItem mi_type;
	private MenuItem mi_color;
	private MenuItem mi_normal;
	private MenuItem mi_satellite;
	private MenuItem mi_hybrid;
	
	public LocalendarTest() {
		super(Localendar.class);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
	    setActivityInitialTouchMode(false);
	    
		localendar = (Localendar) getActivity();
		calendar = (RadioButton) localendar.findViewById(R.id.calendar_button);
		map = (RadioButton) localendar.findViewById(R.id.map_button);
		settings = (RadioButton) localendar.findViewById(R.id.settings_button);
		searchBox = (ClearableAutoCompleteTextView) localendar.findViewById(R.id.search_box);
	    searchIcon = (ImageView) localendar.findViewById(R.id.place_view);
	    mi_type = (MenuItem) localendar.findViewById(R.id.map_type);
	    mi_color = (MenuItem) localendar.findViewById(R.id.map_color);
	    mi_normal = (MenuItem) localendar.findViewById(R.id.map_normal);
	    mi_satellite = (MenuItem) localendar.findViewById(R.id.map_satellite);
	    mi_hybrid = (MenuItem) localendar.findViewById(R.id.map_hybrid);
	    
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
	
	public void testActionBar() throws InterruptedException {
		TouchUtils.tapView(this, map);
		assertTrue(View.VISIBLE == searchIcon.getVisibility());
		assertTrue(View.GONE == searchBox.getVisibility());
		TouchUtils.tapView(this, searchIcon);
		
	    this.sendKeys(KeyEvent.KEYCODE_H);
	    Thread.sleep(1000);
	    this.sendKeys(KeyEvent.KEYCODE_O);
	    Thread.sleep(1000);
	    this.sendKeys(KeyEvent.KEYCODE_N);
	    Thread.sleep(1000);
	    this.sendKeys(KeyEvent.KEYCODE_G);
	    Thread.sleep(5000);
	    assertEquals(searchBox.getAdapter().getItem(1).toString(), "Hong Kong");
	    Thread.sleep(1000);
		assertTrue(View.GONE == searchIcon.getVisibility());
		assertTrue(View.VISIBLE == searchBox.getVisibility());
		
		this.sendKeys(KeyEvent.KEYCODE_MENU);
		//assertTrue(View.VISIBLE == mi_type.getActionView().getVisibility());
		//TouchUtils.tapView(this, mi_type.getActionView());
	    //Thread.sleep(5000);
		
		
	}
}
