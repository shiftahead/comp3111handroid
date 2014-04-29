package com.comp3111.localendar.facebook;

import java.util.Arrays;
import java.util.List;

import com.comp3111.localendar.R;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FacebookFragment extends Fragment {

	
	private static final String TAG = "FacebookFragment";
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.facebook_fragment, container, false);
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.fb_auth_button);
	    Button shareButton = (Button) view.findViewById(R.id.fb_share_button);
	    authButton.setFragment(this); 
	    //authButton.setPublishPermissions(Arrays.asList("publish_actions"));
	    shareButton.setOnClickListener(new View.OnClickListener() {   	
			@Override
			public void onClick(View v) {
				FacebookLogin.publishLoginDialog();
			}
	    });
	    return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		   
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);

	    //Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
	    Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) 
                session = Session.restoreSession(getActivity(), null, callback, savedInstanceState);
            
            else
                session = new Session(getActivity());
            
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(callback));
                //List<String> permissions = session.getPermissions();

    	        
    	            Session.NewPermissionsRequest newPermissionsRequest = new Session
    	                    .NewPermissionsRequest(this, PERMISSIONS);
    	            session.requestNewPublishPermissions(newPermissionsRequest);
    	           
    	        
            }
        }
        
        
	    //uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    //uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	
}
