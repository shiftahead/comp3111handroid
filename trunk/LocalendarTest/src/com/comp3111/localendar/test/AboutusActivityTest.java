package com.comp3111.localendar.test;

import com.comp3111.localendar.AboutusActivity;
import com.comp3111.localendar.R;
import com.comp3111.localendar.SettingsActivity;
import com.comp3111.localendar.calendar.EventDetailActivity;
import com.robotium.solo.Solo;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class AboutusActivityTest extends ActivityInstrumentationTestCase2<AboutusActivity>{
    private AboutusActivity mAboutusActivity;  
    private TextView link ;
    Solo solo;
    public AboutusActivityTest() {
		super(AboutusActivity.class);
		// TODO Auto-generated constructor stub
	}
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mAboutusActivity = (AboutusActivity) getActivity();
	    solo = new Solo(getInstrumentation(), getActivity());
        link = (TextView) mAboutusActivity.findViewById(R.id.link);
    }
   public void testLink() throws InterruptedException{
	   assertNotNull(link);
	   assertTrue(solo.searchText("https"));
	   solo.clickOnText("https://code.google.com/p/comp3111handroid/");
	   Thread.sleep(3000);
   }
}