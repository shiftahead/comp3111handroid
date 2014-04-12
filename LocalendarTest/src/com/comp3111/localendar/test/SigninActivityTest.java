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
	        mTextView1 = (TextView) mSigninActivity.findViewById (R.id.signinTitle);
	        mTextView2 = (TextView) mSigninActivity.findViewById(R.id.SigninHint);
	    }
	 public void testPreconditions(){
	    	assertNotNull("mSigninActivity  is null",mSigninActivity);
	    	assertNotNull("mTextView1 is null",mTextView1);
	    	assertNotNull("mTextView2 is null",mTextView2);
	    }
	 public void testTextView1_labelText() {
	        String expected ="Sign in";
	        String actual = mTextView1.getText().toString();
	        assertEquals(expected, actual);
	    }
	 public void testTextView2_labelText() {
	        String expected ="Sign in to synchronize with Google";
	        String actual = mTextView2.getText().toString();
	        assertEquals(expected, actual);
	    }
}
