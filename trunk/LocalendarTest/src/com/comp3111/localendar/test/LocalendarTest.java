package com.comp3111.localendar.test;

import java.util.Calendar;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
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
	private Button add,settings;
	private ClearableAutoCompleteTextView searchBox;
	private ImageView searchIcon;
	
	private TextView projectSettingView1; 
	private TextView projectSettingView2;

	public LocalendarTest() {
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
		settings = (Button) localendar.findViewById(R.id.settings_button);
		searchBox = (ClearableAutoCompleteTextView) localendar.findViewById(R.id.search_box);
	    searchIcon = (ImageView) localendar.findViewById(R.id.place_view);
	    
	    projectSettingView1= (TextView)localendar.findViewById(R.id.my_account);
	    projectSettingView2= (TextView)localendar.findViewById(R.id.about_us);
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
	/*
	public void testSetting_labelText1() {
        String expected ="My Account";
        String actual = projectSettingView1.getText().toString();
        assertEquals(expected, actual);
    }
	public void testSetting_labelText2() {
        String expected ="About us";
        String actual = projectSettingView2.getText().toString();
        assertEquals(expected, actual);
    }
    */
}
