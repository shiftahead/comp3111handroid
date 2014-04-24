package com.comp3111.localendar.test;

import android.test.ActivityInstrumentationTestCase2;

import com.comp3111.localendar.Appstart;


public class AppstartTest extends ActivityInstrumentationTestCase2<Appstart> {

	private Appstart app;
	public AppstartTest(Class<Appstart> activityClass) {
		super(activityClass);
	}
	public void testNull() {
		assertNotNull("Appstart  is null", app);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
