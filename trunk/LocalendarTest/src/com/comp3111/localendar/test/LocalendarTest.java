package com.comp3111.localendar.test;

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
import com.comp3111.localendar.calendar.*;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.ClearableEditText;
import com.robotium.solo.Solo;


public class LocalendarTest extends ActivityInstrumentationTestCase2<Localendar> {

	Solo solo;
	
	private Localendar localendar;
	private RadioButton calendar, map, settings;
	private Button add;
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
		settings = (RadioButton) localendar.findViewById(R.id.settings_button);
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
	}
	
	public void testMenu() throws InterruptedException {
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Type");
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Satellite");
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Type");
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Hybrid");
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Type");
	    Thread.sleep(1000);
	    solo.clickOnMenuItem("Normal");
	    Thread.sleep(1000);
	    
		
	}
	
	public void testEventDetail() throws InterruptedException {
		 Instrumentation instrumentation = getInstrumentation();
	     Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(EventDetailActivity.class.getName(), null, false);
	     TouchUtils.tapView(this, calendar);
	     ListView eventList = (ListView) localendar.findViewById(R.id.events_list);
	     View child = eventList.getChildAt(0);
	     assertNotNull(child);
	     Thread.sleep(1000);
	     solo.clickOnView(child);
	     
	     Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	     assertNotNull(currentActivity);
	     monitor = instrumentation.addMonitor(EditEventActivity.class.getName(), null, false);
	     
	     solo.sendKey(Solo.MENU);
		 Thread.sleep(1000);
		 solo.clickOnMenuItem("Edit");
		 currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		 Thread.sleep(1000);
	     assertNotNull(currentActivity);
		 Button cancel = (Button) currentActivity.findViewById(R.id.cancel_add);
		 solo.clickOnView(cancel);
		 Thread.sleep(1000);
		 
		 monitor = instrumentation.addMonitor(EventDetailActivity.class.getName(), null, false);
		 solo.clickOnView(child);
		 currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	     assertNotNull(currentActivity);
	     
	     monitor = instrumentation.addMonitor(EditEventActivity.class.getName(), null, false);
	     solo.sendKey(Solo.MENU);
		 Thread.sleep(1000);
		 solo.clickOnMenuItem("Edit");
		 currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		 Thread.sleep(1000);
	     assertNotNull(currentActivity);
		 Button confirm = (Button) currentActivity.findViewById(R.id.confirm_add);
		 solo.clickOnView(confirm);
		 Thread.sleep(1000);
	     
	}
	
	public void testAddEvent() throws InterruptedException {
		
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
	      //currentActivity.finish();
	      
	      ClearableEditText eventTitle = (ClearableEditText) currentActivity.findViewById(R.id.event_title);
	      ClearableEditText eventDescription = (ClearableEditText) currentActivity.findViewById(R.id.event_description);
	      DatePicker eventDate = (DatePicker) currentActivity.findViewById(R.id.date_picker);
	      TimePicker eventTime = (TimePicker) currentActivity.findViewById(R.id.time_picker);
	      EditText eventHour = (EditText) currentActivity.findViewById(R.id.duration_hour);
	      EditText eventMinute = (EditText) currentActivity.findViewById(R.id.duration_minute);
	      ClearableAutoCompleteTextView eventLocation = (ClearableAutoCompleteTextView) currentActivity.findViewById(R.id.event_location);
	      Spinner eventTransportation = (Spinner) currentActivity.findViewById(R.id.event_transportation);
	      CheckBox eventCompulsory = (CheckBox) currentActivity.findViewById(R.id.event_compulsory);
	      Button confirmAdd = (Button) currentActivity.findViewById(R.id.confirm_add);
	      Button cancelAdd = (Button) currentActivity.findViewById(R.id.cancel_add);
	      
	      Point size = new Point();
	      currentActivity.getWindowManager().getDefaultDisplay().getSize(size);
	      
	      assertTrue(View.VISIBLE == eventTitle.getVisibility());
	      
	      TouchUtils.tapView(this, eventTitle);
	      Thread.sleep(500);
	      sendKeys("A B C ENTER");
	      Thread.sleep(500);
	      
	      TouchUtils.tapView(this, eventDescription);
	      Thread.sleep(500);
	      sendKeys("A B C ENTER");
	      Thread.sleep(500);
	      
	      //Drag screen
	      TouchUtils.drag(this, 0, 0, size.y/4, size.y/20, 100);
	      Thread.sleep(500);
	      
	      //6 lines of code modify Date
	      TouchUtils.drag(this, size.x/5, 2*size.x/5, size.y/5, 0, 20);
	      Thread.sleep(500);
	      
	      TouchUtils.drag(this, size.x/2, size.x/2, size.y/5, 0, 20);
	      Thread.sleep(500);
	      
	      TouchUtils.drag(this, 2*size.x/3, 2*size.x/3, size.y/5, 0, 20);
	      Thread.sleep(500);
	      
	      //Drag screen
	      TouchUtils.drag(this, 0, 0, size.y/4, size.y/16, 100);
	      Thread.sleep(500);
	      
	      //4 lines of code modify Time
	      TouchUtils.drag(this, size.x/5, 2*size.x/5, size.y/5, 0, 20);
	      Thread.sleep(500);
	      
	      TouchUtils.drag(this, size.x/2, size.x/2, size.y/5, 0, 20);
	      Thread.sleep(500);
	      
	      //Drag screen
	      TouchUtils.drag(this, 0, 0, size.y/4, size.y/16, 100);
	      Thread.sleep(500);
	      
	      TouchUtils.tapView(this, eventHour);
	      Thread.sleep(500);
	      sendKeys("1");
	      Thread.sleep(500);	    
	      
	      TouchUtils.tapView(this, eventMinute);
	      Thread.sleep(500);
	      sendKeys("1 0");
	      Thread.sleep(500);
	      
	      TouchUtils.tapView(this, eventLocation);
	      this.sendKeys("H ENTER");
	      Thread.sleep(1000);
	      this.sendKeys("O ENTER");
	      Thread.sleep(1000);
	      this.sendKeys("N ENTER");
	      Thread.sleep(1000);
	      this.sendKeys("G ENTER");
	      Thread.sleep(1000);
	      assertEquals(eventLocation.getAdapter().getItem(1).toString(), "Hong Kong");
	      Thread.sleep(1000);
	      TouchUtils.tapView(this, eventLocation);
	         
	      //Drag screen
	      TouchUtils.drag(this, 0, 0, size.y/4, size.y/16, 100);
	      Thread.sleep(500);
	      
	      TouchUtils.tapView(this, eventTransportation);
	      TouchUtils.tapView(this, eventCompulsory);
	      TouchUtils.tapView(this, confirmAdd);
	      
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
