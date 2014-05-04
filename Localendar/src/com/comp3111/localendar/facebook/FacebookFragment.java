package com.comp3111.localendar.facebook;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.comp3111.localendar.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FacebookFragment extends Fragment {

	
	private static final String TAG = "FacebookFragment";
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static String userID = "";
	private static String profileName = "";
	private TextView userName;
	private ImageView userPhoto;
	
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
	    userName = (TextView) view.findViewById(R.id.facebook_username);
	    userPhoto = (ImageView) view.findViewById(R.id.facebook_photo);
	    authButton.setFragment(this); 
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

	    Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) 
                session = Session.restoreSession(getActivity(), null, callback, savedInstanceState);
            else
                session = new Session(getActivity());
            
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(callback));
                Session.NewPermissionsRequest newPermissionsRequest = new Session
    	                    .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
            }
        }
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
	    
	    if (session != null && session.isOpened()) {
	        // If the session is open, make an API call to get user data
	        // and define a new callback to handle the response
	        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	                
	            	if (user != null) {
	            		userID = user.getId();//user id
	                    profileName = user.getName();//user's profile name
	                    userName.setText("Logged in as: " + profileName);
	                    userPhoto.setImageBitmap(getFacebookProfilePicture(userID));
	            	}   
	            }  
	        }); 
	        Request.executeBatchAsync(request);
	    }
	    else {
	    	userName.setText("");
	    	userPhoto.setImageBitmap(null);
	    }
	}
	
	public static Bitmap getFacebookProfilePicture(String id){
	    URL imageURL = null;
	    Bitmap bitmap = null;
		try {
			imageURL = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
			bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return bitmap;
	}
}
