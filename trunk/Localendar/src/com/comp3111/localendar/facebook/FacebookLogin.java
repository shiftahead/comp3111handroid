package com.comp3111.localendar.facebook;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.comp3111.localendar.R;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


public class FacebookLogin extends FragmentActivity {
	
	public static FacebookLogin facebookInstance;
	private FacebookFragment facebookFragment;
	private String title, description, location, time;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    facebookInstance = this;
	    ActionBar actionBar = getActionBar();
	    actionBar.setTitle("Facebook");
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.comp3111.localendar", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {
        	e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {

        }
        */
        
	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	    	facebookFragment = new FacebookFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, facebookFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	    	facebookFragment = (FacebookFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	    Bundle b = getIntent().getExtras();
        if (b != null) {
        	title = b.getString("TITLE");
        	description = b.getString("DESCRIPTION");
        	location = b.getString("LOCATION");
        	time = b.getString("TIME");
        	publishEvent();
        }
	}
	
	  @Override
      public boolean onOptionsItemSelected(MenuItem menuItem)
      {       
          onBackPressed();
          overridePendingTransition(R.anim.left_in, R.anim.right_out);
          return true;
      }
	  
	  static void publishLoginDialog() {
		  try{
			  Bundle params = new Bundle();
			  params.putString("name", "Localendar");
			  params.putString("caption", "I am using Localendar");
			  params.putString("description", "A wonderful location based calendar");
			  params.putString("link", "https://code.google.com/p/comp3111handroid/");
			  params.putString("picture", "https://comp3111handroid.googlecode.com/svn/trunk/Localendar/res/drawable-hdpi/small_logo_color.png");
			  WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(facebookInstance, Session.getActiveSession(),params))
					  .setOnCompleteListener(new OnCompleteListener() {

				            @Override
				            public void onComplete(Bundle values, FacebookException error) {
				                if (error == null) {
				                    // When the story is posted, echo the success
				                    // and the post Id.
				                    final String postId = values.getString("post_id");
				                    if (postId != null) {
				                        Toast.makeText(facebookInstance,
				                            "Share succeeded",
				                            Toast.LENGTH_SHORT).show();
				                    } else {
				                        // User clicked the Cancel button
				                        Toast.makeText(facebookInstance, 
				                            "Publish cancelled", 
				                            Toast.LENGTH_SHORT).show();
				                    }
				                } else if (error instanceof FacebookOperationCanceledException) {
				                    // User clicked the "x" button
				                    Toast.makeText(facebookInstance, 
				                        "Publish cancelled", 
				                        Toast.LENGTH_SHORT).show();
				                } else {
				                    // Generic, ex: network error
				                    Toast.makeText(facebookInstance, 
				                        "Error posting story", 
				                        Toast.LENGTH_SHORT).show();
				                }
				            }

				        })
				        .build();
			  feedDialog.show();
		  } catch(Exception e) {
			  e.printStackTrace();
			  Toast.makeText(facebookInstance,"Share fail. You may not login successfully.", Toast.LENGTH_SHORT).show();
		  }
	  }
	  
	  private void publishEvent() {
		  try{
			  Bundle params = new Bundle();
			  params.putString("name", "My Localendar Event");
			  params.putString("caption", title);
//			  params.putString("description", "Time: "+time+". "+ "\\n" + "Location: "+location+". ");
			  if(!description.isEmpty())
				  params.putString("description", "Time: " + time +". "+ "Location: "+location+ ". "  +"Description: " + description);
			  else 
				  params.putString("description", "Time: " + time+". " + "Location: "+location+ ". ");
			  params.putString("link", "https://code.google.com/p/comp3111handroid/");
			  params.putString("picture", "https://comp3111handroid.googlecode.com/svn/trunk/Localendar/res/drawable-hdpi/small_logo_color.png");
			  WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(facebookInstance, Session.getActiveSession(),params))
					  .setOnCompleteListener(new OnCompleteListener() {

				            @Override
				            public void onComplete(Bundle values, FacebookException error) {
				                if (error == null) {
				                    // When the story is posted, echo the success
				                    // and the post Id.
				                    final String postId = values.getString("post_id");
				                    if (postId != null) {
				                        Toast.makeText(facebookInstance,
				                            "Share succeeded",
				                            Toast.LENGTH_SHORT).show();
				                    } else {
				                        // User clicked the Cancel button
				                        Toast.makeText(facebookInstance, 
				                            "Publish cancelled", 
				                            Toast.LENGTH_SHORT).show();
				                    }
				                } else if (error instanceof FacebookOperationCanceledException) {
				                    // User clicked the "x" button
				                    Toast.makeText(facebookInstance, 
				                        "Publish cancelled", 
				                        Toast.LENGTH_SHORT).show();
				                } else {
				                    // Generic, ex: network error
				                    Toast.makeText(facebookInstance, 
				                        "Error posting story", 
				                        Toast.LENGTH_SHORT).show();
				                }
				                facebookInstance.finish();
				            }

				        })
				        .build();
			  feedDialog.show();
		  } catch(Exception e) {
			  e.printStackTrace();
			  Toast.makeText(facebookInstance,"Share fail. Please go back and try again.", Toast.LENGTH_SHORT).show();
		  }
	  }
}