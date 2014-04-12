package com.comp3111.localendar.test;

import com.comp3111.localendar.AboutusActivity;
import com.comp3111.localendar.R;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class AboutusActivityTest extends ActivityInstrumentationTestCase2<AboutusActivity>{
    private AboutusActivity mAboutusActivity;  
    private TextView mTextView;
    private TextView mSecondTextView;
    public AboutusActivityTest() {
		super(AboutusActivity.class);
		
		// TODO Auto-generated constructor stub
	}
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mAboutusActivity = getActivity();
        mTextView = (TextView) mAboutusActivity.findViewById (R.id.introduction);
        mSecondTextView = (TextView) mAboutusActivity.findViewById(R.id.aboutUsTitle);
    }
    public void testPreconditions(){
    	assertNotNull("mAboutusActivity is null",mAboutusActivity);
    	assertNotNull("mTextView is null",mTextView);
    }   
    public void testTextView_labelText() {
        String expected ="We are COMP3111 Group 'Localendar'";
        String actual = mTextView.getText().toString();
        assertEquals(expected, actual);
    }
    public void testSecondTextView_labelText() {
        String expected ="About us";
        String actual = mSecondTextView.getText().toString();
        assertEquals(expected, actual);
    }
}