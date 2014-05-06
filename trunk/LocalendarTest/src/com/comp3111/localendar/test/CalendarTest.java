package com.comp3111.localendar.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Point;
import android.graphics.PointF;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.calendar.AddEventActivity;
import com.comp3111.localendar.calendar.CalendarSearch;
import com.comp3111.localendar.calendar.CalendarView;
import com.comp3111.localendar.calendar.DayChooseActivity;
import com.comp3111.localendar.calendar.EditEventActivity;
import com.comp3111.localendar.calendar.EventDetailActivity;
import com.comp3111.localendar.calendar.SearchResult;
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.ClearableEditText;
import com.google.android.gms.drive.internal.t;
import com.robotium.solo.Solo;

public class CalendarTest extends ActivityInstrumentationTestCase2<Localendar>{

	Solo solo;
	
	private Localendar localendar;
	private RadioButton calendar, map;
	private Button add,settings;
	private ClearableAutoCompleteTextView searchBox;
	private ImageView searchIcon;
	
	public CalendarTest() {
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
	}
	public void testCalendarMenu() throws InterruptedException {
		TouchUtils.tapView(this, calendar);
		Thread.sleep(200);
		
		Display display = localendar.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		float width = size.x;
		float height = size.y;
		
		
		solo.drag(width/2, width/2, height-400, 200, 1);
		Thread.sleep(500);
		solo.drag(width/2, width/2, height-700, height-300, 1);
		Thread.sleep(500);
		solo.drag(width/2, width/2, 200, height-300, 1);
		Thread.sleep(500);
		solo.drag(width/2, width/2, height-400, 100, 1);
		Thread.sleep(500);
		
		solo.drag(10, width-10, height-300, height-300, 1);
		Thread.sleep(500);
		solo.drag(width-10, 10, height-300, height-300, 1);
		Thread.sleep(500);
		solo.drag(10, width-10, height-800, height-800, 1);
		Thread.sleep(500);
		
		CalendarView month = (CalendarView) localendar.findViewById(R.id.calendar_monthview);
		solo.drag(width/2, width/2, height-400, 200, 1);
		Thread.sleep(500);
		solo.drag(width/2, width/2, height-700, height-300, 1);
		Thread.sleep(500);
		solo.clickOnView(month);
		solo.clickOnMenuItem("Go to");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Today");
	    Thread.sleep(200);
	    
	    
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Month View");
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Day View");
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Go to");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Today");
	    Thread.sleep(200);
	}
	public void testGotoDate() throws InterruptedException {
		
		Instrumentation instrumentation = getInstrumentation();
	    Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(DayChooseActivity.class.getName(), null, false);
	    
		TouchUtils.tapView(this, calendar);
		Thread.sleep(1000);
		solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
	    solo.sendKey(Solo.MENU);
	    Thread.sleep(200);
		solo.clickOnMenuItem("Go to");
	    Thread.sleep(500);
	   
	    solo.clickOnMenuItem("Choose Date");
	    Thread.sleep(200);
	    
	    solo.setDatePicker(0, 2014, 4, 10);
	    Thread.sleep(500);
	    
	    Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	    assertNotNull(currentActivity);
	    Button cancel = (Button) currentActivity.findViewById(R.id.chooseday_cancel);
	    solo.scrollDown();
	    Thread.sleep(500);
		solo.clickOnView(cancel);
		Thread.sleep(200);
		currentActivity.finish();
		Thread.sleep(500);
		instrumentation.removeMonitor(monitor);
	    monitor = instrumentation.addMonitor(DayChooseActivity.class.getName(), null, false);
		solo.clickOnMenuItem("Go to");
	    Thread.sleep(200);
	    solo.clickOnMenuItem("Choose Date");
	    Thread.sleep(200);
	    currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	    assertNotNull(currentActivity);
	    Thread.sleep(500);
	    Button confirm = (Button) currentActivity.findViewById(R.id.chooseday_confirm);
		solo.clickOnView(confirm);
		Thread.sleep(500);
		currentActivity.finish();
		Thread.sleep(500);
	}
	
	public void testLongTouch() throws InterruptedException {
		
		
		TouchUtils.tapView(this, calendar);
		Thread.sleep(200);
		
		ListView eventList = (ListView) localendar.findViewById(R.id.events_list);
	    View child1 = eventList.getChildAt(0);
	    assertNotNull(child1);
	    View child2 = eventList.getChildAt(1);
	    assertNotNull(child2);
	    
	    Thread.sleep(500);
		solo.clickLongOnView(child1, 3000);
		Thread.sleep(500);
		solo.clickOnView(child2);
		Thread.sleep(500);
		solo.clickOnView(child2);
		Thread.sleep(500);
		solo.clickOnView(child1);
		Thread.sleep(500);
		solo.clickLongOnView(child2, 3000);
		Thread.sleep(500);
		solo.clickOnView(child1);
		Thread.sleep(500);
		solo.clickOnView(child1);
		Thread.sleep(500);
		solo.clickOnView(child2);
		Thread.sleep(500);
		solo.clickLongOnView(child2, 3000);
		Thread.sleep(500);
		solo.clickOnActionBarItem(R.id.menu_delete);
		Thread.sleep(500);

	}
	
	public void testEventDetail() throws InterruptedException {
		
		 Instrumentation instrumentation = getInstrumentation();
	     Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(EventDetailActivity.class.getName(), null, false);
	     TouchUtils.tapView(this, calendar);
	     ListView eventList = (ListView) localendar.findViewById(R.id.events_list);
	     View child = eventList.getChildAt(0);
	     assertNotNull(child);
	     Thread.sleep(500);
	     solo.clickOnView(child);
	     
	     Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	     assertNotNull(currentActivity);
	     instrumentation.removeMonitor(monitor);
	     monitor = instrumentation.addMonitor(EditEventActivity.class.getName(), null, false);
	     
	     solo.sendKey(Solo.MENU);
		 Thread.sleep(300);
		 solo.clickOnMenuItem("Delete");
		 currentActivity.finish();
		 Thread.sleep(500);
	}
	public void testAddEvent_labelText() throws InterruptedException {
		  Instrumentation instrumentation = getInstrumentation();
	      Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(AddEventActivity.class.getName(), null, false);

	      TouchUtils.tapView(this, add);
	      
	      // Wait for it to start...
	      Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	      assertNotNull(currentActivity);
	      
	      TextView titleTextView = (TextView)currentActivity.findViewById(R.id.addEventTitle);
	      String expectedTitle = "Title";
	      String actualTitle = titleTextView.getText().toString();
	      assertEquals(expectedTitle, actualTitle);
	      
	      TextView descriptionTextView =(TextView) currentActivity.findViewById(R.id.addEventDescription);
	      String expectedDescription = "Description";
	      String actualDescription = descriptionTextView.getText().toString();
	      assertEquals(expectedTitle, actualTitle);
	      currentActivity.finish();
	      Thread.sleep(300);
	}
	public void testAddEvent() throws InterruptedException {
		
		  Instrumentation instrumentation = getInstrumentation();
	      Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(AddEventActivity.class.getName(), null, false);

	      TouchUtils.tapView(this, add);
	      
	      // Wait for it to start...
	      Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	      assertNotNull(currentActivity);
	      
	      ClearableEditText eventTitle = (ClearableEditText) currentActivity.findViewById(R.id.event_title);
	      ClearableEditText eventDescription = (ClearableEditText) currentActivity.findViewById(R.id.event_description);
	      DatePicker eventDate = (DatePicker) currentActivity.findViewById(R.id.date_picker);
	      TimePicker eventTime = (TimePicker) currentActivity.findViewById(R.id.time_picker);
	      EditText eventHour = (EditText) currentActivity.findViewById(R.id.duration_hour);
	      EditText eventMinute = (EditText) currentActivity.findViewById(R.id.duration_minute);
	      ClearableAutoCompleteTextView eventLocation = (ClearableAutoCompleteTextView) currentActivity.findViewById(R.id.event_location);
	      Spinner eventTransportation = (Spinner) currentActivity.findViewById(R.id.event_transportation);
	      Spinner eventRemindTime = (Spinner) currentActivity.findViewById(R.id.remind_time);
	      CheckBox eventCompulsory = (CheckBox) currentActivity.findViewById(R.id.event_compulsory);
	      Button confirmAdd = (Button) currentActivity.findViewById(R.id.confirm_add);
	      Button cancelAdd = (Button) currentActivity.findViewById(R.id.cancel_add);
	      	      
	      assertTrue(View.VISIBLE == eventTitle.getVisibility());
	      
	      solo.clickOnView(eventTitle);
	      Thread.sleep(500);
	      sendKeys("A B C ENTER");
	      Thread.sleep(500);
	      
	      solo.clickOnView(eventDescription);
	      Thread.sleep(500);
	      sendKeys("A B C ENTER");
	      Thread.sleep(500);
	      //solo.setTimePicker(0, 12,10);
	      solo.clickOnView(eventLocation);
	      this.sendKeys("H ENTER");
	      Thread.sleep(500);
	      this.sendKeys("O ENTER");
	      Thread.sleep(500);
	      this.sendKeys("N ENTER");
	      Thread.sleep(500);
	      this.sendKeys("G ENTER");
	      Thread.sleep(500);
	      assertEquals(eventLocation.getAdapter().getItem(1).toString(), "Hong Kong");
	      Thread.sleep(500);
	      solo.clickOnText("Hong Kong");
	      Thread.sleep(500);
	      solo.clickOnView(eventTransportation);
	      Thread.sleep(500);
	      solo.clickOnText("Drive");
	      Thread.sleep(500);
	      solo.clickOnView(eventRemindTime);
	      Thread.sleep(500);
	      solo.clickOnText("5 minutes ahead");
	      Thread.sleep(500);	      
	      //TouchUtils.tapView(this, eventCompulsory);
	      solo.clickOnView(confirmAdd);
	      Thread.sleep(500);
	      solo.clickOnText("Later");
	      solo.clickOnText("Stop");
	      currentActivity.finish();
	}
	public void testAddnewEvent() throws InterruptedException {
		 Instrumentation instrumentation = getInstrumentation();
	      Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(AddEventActivity.class.getName(), null, false);

	      TouchUtils.tapView(this, add);
	      
	      // Wait for it to start...
	      Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	      assertNotNull(currentActivity);
	      
	      ClearableEditText eventTitle = (ClearableEditText) currentActivity.findViewById(R.id.event_title);
	      ClearableEditText eventDescription = (ClearableEditText) currentActivity.findViewById(R.id.event_description);
	      DatePicker eventDate = (DatePicker) currentActivity.findViewById(R.id.date_picker);
	      TimePicker eventTime = (TimePicker) currentActivity.findViewById(R.id.time_picker);
	      EditText eventHour = (EditText) currentActivity.findViewById(R.id.duration_hour);
	      EditText eventMinute = (EditText) currentActivity.findViewById(R.id.duration_minute);
	      ClearableAutoCompleteTextView eventLocation = (ClearableAutoCompleteTextView) currentActivity.findViewById(R.id.event_location);
	      Spinner eventTransportation = (Spinner) currentActivity.findViewById(R.id.event_transportation);
	      Spinner eventRemindTime = (Spinner) currentActivity.findViewById(R.id.remind_time);
	      CheckBox eventCompulsory = (CheckBox) currentActivity.findViewById(R.id.event_compulsory);
	      Button confirmAdd = (Button) currentActivity.findViewById(R.id.confirm_add);
	      Button cancelAdd = (Button) currentActivity.findViewById(R.id.cancel_add);
	      
	      Point size = new Point();
	      currentActivity.getWindowManager().getDefaultDisplay().getSize(size);
	      
	      assertTrue(View.VISIBLE == eventTitle.getVisibility());
	      
	      
	      solo.clickOnView(eventTitle);
	      Thread.sleep(500);
	      sendKeys("H H ENTER");
	      Thread.sleep(500);
	      
	      solo.clickOnView(eventDescription);
	      Thread.sleep(500);
	      sendKeys("H H ENTER");
	      Thread.sleep(500);
	      solo.clickOnView(eventLocation);
	      this.sendKeys("H ENTER");
	      Thread.sleep(500);
	      this.sendKeys("K ENTER");
	      Thread.sleep(500);
	      this.sendKeys("U ENTER");
	      Thread.sleep(500);
	      this.sendKeys("S ENTER");
	      Thread.sleep(500);
	      this.sendKeys("T ENTER");
	      Thread.sleep(500);
	      solo.clickOnText("HKUST, Hong Kong");
	      Thread.sleep(500);
	      solo.clickOnView(eventTransportation);
	      Thread.sleep(500);
	      solo.clickOnText("Drive");
	      Thread.sleep(500);
	      //TouchUtils.tapView(this, eventCompulsory);
	      solo.clickOnView(eventRemindTime);
	      Thread.sleep(500);
	      solo.clickOnText("5 minutes ahead");
	      Thread.sleep(500);	      
	      //TouchUtils.tapView(this, eventCompulsory);
	      solo.clickOnView(confirmAdd);
	      Thread.sleep(500);
	      currentActivity.finish(); 
	   }
	
	
	public void testEditEvent() throws InterruptedException{
		
		Instrumentation ins = getInstrumentation();
		Instrumentation.ActivityMonitor am = ins.addMonitor(EventDetailActivity.class.getName(), null, false);
		
		TouchUtils.tapView(this, calendar);
		Thread.sleep(500);
		ListView eventList = (ListView) localendar.findViewById(R.id.events_list);
		int childCount = eventList.getChildCount()-1;
		View child = eventList.getChildAt(childCount);
	    assertNotNull(child);
	    Thread.sleep(500);
	    solo.clickOnView(child);
	    
	    Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(am, 500);
		assertNotNull(currentActivity);
	    
		ins.removeMonitor(am);
		am = ins.addMonitor(EditEventActivity.class.getName(), null, false);
		
		solo.sendKey(Solo.MENU);
	    solo.clickOnMenuItem("Edit");
	    Thread.sleep(500);
	    
	    
	    currentActivity = getInstrumentation().waitForMonitorWithTimeout(am, 500);
		assertNotNull(currentActivity);
		
	    DatePicker eventDate = (DatePicker) currentActivity.findViewById(R.id.date_picker);
	    TimePicker eventTime = (TimePicker) currentActivity.findViewById(R.id.time_picker);
	    EditText eventHour = (EditText) currentActivity.findViewById(R.id.duration_hour);
	    EditText eventMinute = (EditText) currentActivity.findViewById(R.id.duration_minute);
	    ClearableAutoCompleteTextView eventLocation = (ClearableAutoCompleteTextView) currentActivity.findViewById(R.id.event_location);

	    
	    //Test confirming an edit
	    solo.clickOnEditText(0);
	    solo.clearEditText(0);
	    String test = "def";
	    solo.enterText(0, test);
	    
	    
	    solo.clickOnEditText(1);
	    solo.clearEditText(1);
	    solo.enterText(1, test);
	      
	    //solo.setDatePicker(eventDate, 2014, 11, 31);
	    solo.setTimePicker(eventTime, 11, 59);
	  
	    solo.clearEditText(eventHour);
	    solo.enterText(eventHour, "0");
	    solo.clearEditText(eventMinute);
	    solo.enterText(eventMinute, "1");
	    
	    solo.clearEditText(eventLocation);
	    solo.enterText(eventLocation, "hkust");
	    
	    solo.pressSpinnerItem(0, 2);
	    boolean actual = solo.isSpinnerTextSelected("On foot");
	    assertEquals("On foot is not selected",true, actual);
	    
	    
	    solo.clickOnCheckBox(0);
	    solo.clickOnButton("CONFIRM");
	    
	    Thread.sleep(5000);
	    am = ins.addMonitor(EventDetailActivity.class.getName(), null, false);
	    currentActivity = getInstrumentation().waitForMonitorWithTimeout(am, 500);
	    //Test cancel
	    child = eventList.getChildAt(0);
	    solo.clickOnView(child);
	    
		solo.sendKey(Solo.MENU);
		
		ins.removeMonitor(am);
	    am = ins.addMonitor(EditEventActivity.class.getName(), null, false);
	    currentActivity = getInstrumentation().waitForMonitorWithTimeout(am, 500);
	    solo.clickOnMenuItem("Edit");
	    Thread.sleep(500);
	    solo.clickOnButton("CANCEL");
	    Thread.sleep(5000);
	}
	public void testSearchEventCancel() throws InterruptedException {
		 Instrumentation instrumentation = getInstrumentation();
	     Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(CalendarSearch.class.getName(), null, false);
		  TouchUtils.tapView(this, calendar);
		  Thread.sleep(500);
		  solo.sendKey(Solo.MENU);
		  Thread.sleep(1000);
		  solo.sendKey(Solo.MENU);
		  Thread.sleep(500);
		  solo.clickOnMenuItem("Search Event");
		  Thread.sleep(1500);
		  Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  assertNotNull(currentActivity);
		  Button cancelButton = (Button) currentActivity.findViewById(R.id.btnCancel);
		  
		  solo.clickOnText("Cancel");
		  Thread.sleep(1000);
	}
	public void testSearchEventWithResult() throws InterruptedException {
		 Instrumentation instrumentation = getInstrumentation();
	     Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(CalendarSearch.class.getName(), null, false);
		  TouchUtils.tapView(this, calendar);
		  Thread.sleep(500);
		  solo.sendKey(Solo.MENU);
		  Thread.sleep(500);
		  solo.sendKey(Solo.MENU);
		  Thread.sleep(500);
		  solo.clickOnMenuItem("Search Event");
		  Thread.sleep(1500);
		  Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  assertNotNull(currentActivity);
		  EditText inputText = (EditText) currentActivity.findViewById(R.id.etSearch);
		  Button findButton = (Button) currentActivity.findViewById(R.id.btnFind);
	  
		  
		  Thread.sleep(300);
		  solo.sendKey(KeyEvent.KEYCODE_H);
		  Thread.sleep(500);
		  solo.sendKey(KeyEvent.KEYCODE_ENTER);
		  Thread.sleep(500);
		  //set monitor to search result
		  instrumentation.removeMonitor(monitor);
		  monitor = instrumentation.addMonitor(SearchResult.class.getName(), null, false);
		  Thread.sleep(1000);
		  solo.clickOnText("Confirm");
		  Thread.sleep(1500);
		  currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  ListView listResut = (ListView) currentActivity.findViewById(R.id.result_list); 
		  // set monitor to event detail
		  instrumentation.removeMonitor(monitor);
		  monitor = instrumentation.addMonitor(EventDetailActivity.class.getName(), null, false);
		  View child = listResut.getChildAt(1);
		  assertNotNull(child);
		  Thread.sleep(500);
		  solo.clickOnView(child);
		  
		  currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  assertNotNull(currentActivity);
		  Thread.sleep(1500);
		  
		  instrumentation.removeMonitor(monitor);
		  monitor = instrumentation.addMonitor(Localendar.class.getName(), null, false);
			
		  solo.sendKey(Solo.MENU);
		  solo.clickOnMenuItem("Show on map");	
		  Thread.sleep(3000);
		    
		  currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  assertNotNull(currentActivity);
			
		  Display display = localendar.getWindowManager().getDefaultDisplay();
		  Point size = new Point();
		  display.getSize(size);
		  float x = size.x;
	      float y = size.y;
			
		  instrumentation.removeMonitor(monitor);
		  monitor = instrumentation.addMonitor(EventDetailActivity.class.getName(), null, false);
			
		  solo.clickOnScreen(x/2, 2*y/5);
		  Thread.sleep(3000);
			
		  currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  assertNotNull(currentActivity);
		  currentActivity.finish();
		
	}
	public void testSearchEventWithAllResult() throws InterruptedException {
		 Instrumentation instrumentation = getInstrumentation();
	     Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(CalendarSearch.class.getName(), null, false);
		  TouchUtils.tapView(this, calendar);
		  Thread.sleep(500);
		  solo.sendKey(Solo.MENU);
		  Thread.sleep(500);
		  solo.sendKey(Solo.MENU);
		  Thread.sleep(500);
		  solo.clickOnMenuItem("Search Event");
		  Thread.sleep(1500);
		  Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  assertNotNull(currentActivity);
		  Button findButton = (Button) currentActivity.findViewById(R.id.btnFind);
	      instrumentation.removeMonitor(monitor);
		  monitor = instrumentation.addMonitor(SearchResult.class.getName(), null, false);
		  Thread.sleep(1000);
		  solo.clickOnText("Confirm");
		  Thread.sleep(1500);
		  currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  ListView listResut = (ListView) currentActivity.findViewById(R.id.result_list); 
		  instrumentation.removeMonitor(monitor);
		  monitor = instrumentation.addMonitor(EventDetailActivity.class.getName(), null, false);
		  View child = listResut.getChildAt(0);
		  assertNotNull(child);
		  Thread.sleep(500);
		  solo.clickOnView(child);
		  currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  assertNotNull(currentActivity);
		  Thread.sleep(1500);
		  currentActivity.finish();
	}
	public void testSearchEventNoResult() throws InterruptedException {
		 Instrumentation instrumentation = getInstrumentation();
	     Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(CalendarSearch.class.getName(), null, false);
		  TouchUtils.tapView(this, calendar);
		  Thread.sleep(500);
		  solo.sendKey(Solo.MENU);
		  Thread.sleep(500);
		  solo.sendKey(Solo.MENU);
		  Thread.sleep(500);
		  solo.clickOnMenuItem("Search Event");
		  Thread.sleep(1500);
		  Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
		  assertNotNull(currentActivity);
		  EditText inputText = (EditText) currentActivity.findViewById(R.id.etSearch);
		  Button findButton = (Button) currentActivity.findViewById(R.id.btnFind);
	  
		  
		  Thread.sleep(300);
		  solo.sendKey(KeyEvent.KEYCODE_H);
		  Thread.sleep(500);
		  solo.sendKey(KeyEvent.KEYCODE_H);
		  Thread.sleep(500);
		  solo.sendKey(KeyEvent.KEYCODE_H);
		  Thread.sleep(500);
		  solo.sendKey(KeyEvent.KEYCODE_H);
		  Thread.sleep(500);
		  solo.sendKey(KeyEvent.KEYCODE_H);
		  Thread.sleep(500);
		  solo.sendKey(KeyEvent.KEYCODE_H);
		  Thread.sleep(500);
		  solo.sendKey(KeyEvent.KEYCODE_H);
		  Thread.sleep(500);
		  solo.sendKey(KeyEvent.KEYCODE_ENTER);
		  Thread.sleep(500);
		  Thread.sleep(1000);
		  solo.clickOnText("Confirm");
		  Thread.sleep(5000);
		  
		  currentActivity.finish();
		
	}
}
