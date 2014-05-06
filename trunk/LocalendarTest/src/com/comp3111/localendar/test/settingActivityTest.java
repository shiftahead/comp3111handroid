package com.comp3111.localendar.test;

import com.comp3111.localendar.AboutusActivity;
import com.comp3111.localendar.SettingsActivity;
import com.comp3111.localendar.SigninActivity;
import com.comp3111.localendar.calendar.EventDetailActivity;
import com.robotium.solo.Solo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

public class settingActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity>{
	private SettingsActivity mSettingsActivity;
	
	Solo solo;
	
	public settingActivityTest() {
		super(SettingsActivity.class);
		// TODO Auto-generated constructor stub
	}
    protected void setUp() throws Exception{
        super.setUp();
	    setActivityInitialTouchMode(true);
	    mSettingsActivity = (SettingsActivity) getActivity();
	    solo = new Solo(getInstrumentation(), getActivity());
	}
    public void testSystemSetting() throws InterruptedException{
        solo.clickOnText("Default event duration");
    	Thread.sleep(500);
    	solo.clickOnText("2 hours");
    	Thread.sleep(500);
    	solo.goBackToActivity("SettingsActivity");
    	Thread.sleep(500);
    	
    	solo.clickOnText("Compulsory event");
    	Thread.sleep(500);
    	solo.clickOnText("Compulsory event");
    	Thread.sleep(500);
    	
    	solo.clickOnText("Default transportation");
    	Thread.sleep(500);
    	solo.clickOnText("Drive");
    	Thread.sleep(500);
    	solo.goBackToActivity("SettingsActivity");
    	Thread.sleep(500);
    	
    	solo.clickOnText("Default marker color");
    	Thread.sleep(500);
    	solo.clickOnText("Blue");
    	Thread.sleep(500);
    	solo.goBackToActivity("SettingsActivity");
    	Thread.sleep(500);
    	
    	solo.clickOnText("Vibrate");
    	Thread.sleep(500);
    	solo.clickOnText("Vibrate");
    	Thread.sleep(500);
    	}
    public void testSinginGooglePlus() throws InterruptedException {
    	Instrumentation instrumentation = getInstrumentation();
	    Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(SigninActivity.class.getName(), null, false);
    	solo.clickOnText("Sign in with Google+");
    	Thread.sleep(500);
    	Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	    assertNotNull(currentActivity);
        solo.clickOnActionBarHomeButton();   
	    currentActivity.finish();
    	
	}
//    public void testFacebook() throws InterruptedException {
//    	Instrumentation instrumentation = getInstrumentation();
//	    Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(FacebookLogin.class.getName(), null, false);
//    	solo.clickOnText("Facebook");
//    	Thread.sleep(500);
//    	Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
//	    assertNotNull(currentActivity);
//        solo.clickOnActionBarHomeButton();  
//	    currentActivity.finish();
//    	
//	}
    public void testAboutUS() throws InterruptedException {
    	Instrumentation instrumentation = getInstrumentation();
	    Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(AboutusActivity.class.getName(), null, false);
    	solo.clickOnText("About us");
    	Thread.sleep(500);
    	Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 500);
	    assertNotNull(currentActivity);
        solo.clickOnActionBarHomeButton(); 
	    currentActivity.finish();
    	
	}
}
