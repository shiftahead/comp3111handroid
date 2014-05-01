package com.comp3111.localendar.test;

import com.comp3111.localendar.R;
import com.comp3111.localendar.SigninActivity;
import com.comp3111.localendar.calendar.EventDetailActivity;
import com.google.android.gms.common.SignInButton;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SigninActivityTest extends ActivityInstrumentationTestCase2<SigninActivity>{
    
	private SigninActivity mSigninActivity;
	private SignInButton signinButton;
	private Button signoutButton;
	private ListView mListView;
	public SigninActivityTest() {
		super(SigninActivity.class);
		// TODO Auto-generated constructor stub
	}
	 protected void setUp() throws Exception {
	        super.setUp();
	        mSigninActivity = getActivity();
	        signinButton = (SignInButton) mSigninActivity.findViewById(R.id.sign_in_button);
	        signoutButton = (Button) mSigninActivity.findViewById(R.id.sign_out_button);
	        mListView = (ListView) mSigninActivity.findViewById(R.id.circles_list);
	    }
	 public void testSignInAndOut() throws InterruptedException {
		 TouchUtils.tapView(this, signoutButton);
		 Thread.sleep(1000);
		 TouchUtils.tapView(this, signinButton);
		 Thread.sleep(5000);
		 View child = mListView.getChildAt(0);
	     assertNotNull(child);
		 TouchUtils.tapView(this, signoutButton);
	}
}
