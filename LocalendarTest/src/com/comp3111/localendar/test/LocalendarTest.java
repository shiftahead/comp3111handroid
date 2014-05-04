package com.comp3111.localendar.test;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.calendar.AddEventActivity;
import com.comp3111.localendar.calendar.EditEventActivity;
import com.comp3111.localendar.calendar.EventDetailActivity;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.ClearableEditText;
import com.robotium.solo.Solo;


public class LocalendarTest extends ActivityInstrumentationTestCase2<Localendar> {

	Solo solo;
	
	private Localendar localendar;
	private RadioButton calendar, map;
	private Button settings;
	private ClearableAutoCompleteTextView searchBox;
	private ImageView searchIcon;

	public LocalendarTest() {
		super(Localendar.class);
	}
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    setActivityInitialTouchMode(false);
	    
	    solo = new Solo(getInstrumentation(), getActivity());
		localendar = (Localendar) getActivity();
		calendar = (RadioButton) localendar.findViewById(R.id.calendar_button);
		map = (RadioButton) localendar.findViewById(R.id.map_button);
		settings = (Button) localendar.findViewById(R.id.settings_button);
		searchBox = (ClearableAutoCompleteTextView) localendar.findViewById(R.id.search_box);
	    searchIcon = (ImageView) localendar.findViewById(R.id.place_view);
	}
	 
	public void testView() { 
		// checks if the activity is created
		assertNotNull(getActivity());
	}
	
	public void testRadioButtons() throws InterruptedException {
		TouchUtils.tapView(this, calendar);
	    Thread.sleep(500);
		TouchUtils.tapView(this, map);
	    Thread.sleep(500);
		TouchUtils.tapView(this, map);
	    Thread.sleep(500);
		TouchUtils.tapView(this, calendar);
		Thread.sleep(500);
	}
	public void testActionBar() throws InterruptedException {
		
		TouchUtils.tapView(this, map);
		Thread.sleep(300);
		assertTrue(View.VISIBLE == searchIcon.getVisibility());
		assertTrue(View.GONE == searchBox.getVisibility());
		TouchUtils.tapView(this, searchIcon);
		
		TouchUtils.tapView(this, searchBox);
		
	    this.sendKeys(KeyEvent.KEYCODE_H);
	    Thread.sleep(300);
	    this.sendKeys(KeyEvent.KEYCODE_O);
	    Thread.sleep(300);
	    this.sendKeys(KeyEvent.KEYCODE_N);
	    Thread.sleep(300);
	    this.sendKeys(KeyEvent.KEYCODE_G);
	    Thread.sleep(300);
	    this.sendKeys(KeyEvent.KEYCODE_ENTER);
	    Thread.sleep(3000);
	    assertEquals(searchBox.getAdapter().getItem(1).toString(), "Hong Kong");
	    Thread.sleep(300);
	   
	    
		assertTrue(View.GONE == searchIcon.getVisibility());
		assertTrue(View.VISIBLE == searchBox.getVisibility());
		
		solo.clickOnText("Hong Kong");
	    Thread.sleep(5000);
	}
	
	
	public void testMenu() throws InterruptedException {
	    
		TouchUtils.tapView(this, map);
	    Thread.sleep(200);
		solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Type");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Satellite");
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Type");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Hybrid");
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Type");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Normal");
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Color");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Red");
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Color");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Blue");
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Color");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Green");
	    Thread.sleep(200);
	    TouchUtils.tapView(this, settings);
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Add Shortcut");
	    Thread.sleep(200);
		
	}
	public void testAddeventFromMap() throws InterruptedException {

		 Instrumentation instrumentation = getInstrumentation();
	     Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(AddEventActivity.class.getName(), null, false);
	     
		 TouchUtils.tapView(this, map);
	     Thread.sleep(200);
	    
		 TouchUtils.tapView(this, searchIcon);
			
		 TouchUtils.tapView(this, searchBox);
			
		 this.sendKeys(KeyEvent.KEYCODE_B);
		 Thread.sleep(300);
	     this.sendKeys(KeyEvent.KEYCODE_E);
		 Thread.sleep(300);
		 this.sendKeys(KeyEvent.KEYCODE_I);
		 Thread.sleep(300);
		 this.sendKeys(KeyEvent.KEYCODE_J);
		 Thread.sleep(300);
		 this.sendKeys(KeyEvent.KEYCODE_ENTER);
		 Thread.sleep(3000);
		 
		 solo.clickOnText("Beijing, China");
		 Thread.sleep(5000);
		 
		 TouchUtils.tapView(this, searchIcon);
		 Thread.sleep(1000);
//		 assertTrue(solo.searchText("Beijing"));
//		 solo.clickOnText("Beijing, China");
//		 Thread.sleep(5000);
		 Display display = localendar.getWindowManager().getDefaultDisplay();
		 Point size = new Point();
		 display.getSize(size);
		 float x = size.x;
		 float y = size.y;
		 solo.clickOnScreen(x/2, y/6);
		 Thread.sleep(5000);
		 Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	     assertNotNull(currentActivity);
		 Thread.sleep(3000);
		 currentActivity.finish();
	}
}
