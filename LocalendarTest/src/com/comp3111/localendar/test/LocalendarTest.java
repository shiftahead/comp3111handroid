package com.comp3111.localendar.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.calendar.AddEventActivity;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.PlacesAutoCompleteAdapter;
import com.robotium.solo.*;


public class LocalendarTest extends ActivityInstrumentationTestCase2<Localendar> {

	Solo solo;
	
	private Localendar localendar;
	private RadioButton calendar, map, settings;
	private Button add;
	private ClearableAutoCompleteTextView searchBox;
	private ImageView searchIcon;
	private MenuItem mi_type;
	private MenuItem mi_color;
	private MenuItem mi_normal;
	private MenuItem mi_satellite;
	private MenuItem mi_hybrid;
	
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
		settings = (RadioButton) localendar.findViewById(R.id.settings_button);
		searchBox = (ClearableAutoCompleteTextView) localendar.findViewById(R.id.search_box);
	    searchIcon = (ImageView) localendar.findViewById(R.id.place_view);
	    mi_type = (MenuItem) localendar.findViewById(R.id.map_type);
	    mi_color = (MenuItem) localendar.findViewById(R.id.map_color);
	    mi_normal = (MenuItem) localendar.findViewById(R.id.map_normal);
	    mi_satellite = (MenuItem) localendar.findViewById(R.id.map_satellite);
	    mi_hybrid = (MenuItem) localendar.findViewById(R.id.map_hybrid);
	    
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
		TouchUtils.tapView(this, settings);
	    Thread.sleep(500);
		TouchUtils.tapView(this, map);
	    Thread.sleep(500);
		TouchUtils.tapView(this, calendar);
		Thread.sleep(500);
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
	    Thread.sleep(2000);
	    assertEquals(searchBox.getAdapter().getItem(1).toString(), "Hong Kong");
	    Thread.sleep(1000);
		assertTrue(View.GONE == searchIcon.getVisibility());
		assertTrue(View.VISIBLE == searchBox.getVisibility());
		
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Type");
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Satellite");
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Type");
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Normal");
	    Thread.sleep(1000);
	    
		//TouchUtils.tapView(this, mi_satellite.getActionView());
	    //Thread.sleep(1000);
		//assertTrue(View.VISIBLE == mi_type.getActionView().getVisibility());
		//TouchUtils.tapView(this, mi_type.getActionView());
	    //Thread.sleep(5000);
		
	}
	
	public void testAddEvent() {
		
		  Instrumentation instrumentation = getInstrumentation();
	      Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(AddEventActivity.class.getName(), null, false);

	      // Start another activity...
	      /*
	      Intent intent = new Intent(Intent.ACTION_MAIN);
	      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	      intent.setClassName(instrumentation.getTargetContext(), AddEventActivity.class.getName());
	      instrumentation.startActivitySync(intent);
	       */
	      TouchUtils.tapView(this, add);
	      
	      // Wait for it to start...
	      Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	      assertNotNull(currentActivity);
	      currentActivity.finish();
	      
	      
	}
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
}
