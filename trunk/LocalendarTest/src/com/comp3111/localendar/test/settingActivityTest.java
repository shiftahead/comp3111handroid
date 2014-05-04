package com.comp3111.localendar.test;

import com.comp3111.localendar.SettingsActivity;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

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
}
