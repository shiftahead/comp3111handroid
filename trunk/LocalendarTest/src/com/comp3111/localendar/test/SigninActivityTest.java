package com.comp3111.localendar.test;

import com.comp3111.localendar.R;
import com.comp3111.localendar.SigninActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class SigninActivityTest extends ActivityInstrumentationTestCase2<SigninActivity>{
    
	private SigninActivity mSigninActivity;
	private TextView mTextView1;
	private TextView mTextView2;
	public SigninActivityTest() {
		super(SigninActivity.class);
		// TODO Auto-generated constructor stub
	}
	 protected void setUp() throws Exception {
	        super.setUp();
	        mSigninActivity = getActivity();
	    }
}
