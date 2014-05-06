package com.comp3111.localendar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        ActionBar actionBar = getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);
	        actionBar.setTitle("Settings");
	        // Display the fragment as the main content.
	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new SettingsFragment())
	                .commit();
	    }
	 
	 public boolean onOptionsItemSelected(MenuItem item)
     {       
         onBackPressed();
         overridePendingTransition(R.anim.left_in, R.anim.right_out);
         
 		switch (item.getItemId()) {
 		case R.id.add_shortcut:
			addShortcut();
 		}
         return true;
     }
	 
	 public boolean onCreateOptionsMenu(Menu menu) {
		    // Inflate the menu items for use in the action bar
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.settings_menu, menu);
		    return super.onCreateOptionsMenu(menu);
	 }
	 
	 @Override
	 public void startActivity(Intent intent) {
		 super.startActivity(intent);
		 overridePendingTransition(R.anim.right_in,R.anim.left_out);	 
	 }
	 
	 
	 
	 private void addShortcut() {
			Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
	        String title = getResources().getString(R.string.app_name); 
	        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.localendar_logo);
	        Intent intent = new Intent(getApplicationContext(), Appstart.class); 
	        intent.setAction(Intent.ACTION_MAIN);
	        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title); 
	        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
	        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);  
	        shortcutIntent.putExtra("duplicate", false); 
	        getApplicationContext().sendBroadcast(shortcutIntent);
	        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	        editor.putBoolean("isShortcutCreated", true);
	        editor.commit();
	        Toast.makeText(this, "Shortcut added", Toast.LENGTH_SHORT).show();
		}
}
