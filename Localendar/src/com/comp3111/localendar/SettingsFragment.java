package com.comp3111.localendar;


import android.os.Bundle;

import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment/* implements OnSharedPreferenceChangeListener */{

	
	public static final String DURATION = "duration_key";
	public static final String IS_COMPULSORY = "is_compulsory_key";
	public static final String TRANSPORTATION = "transportation_key";
	public static final String RINGTONE = "ring_tone_key";
	public static final String VIBRATE = "vibrate_key";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	 /*public void onSharedPreferenceChanged(SharedPreferences sharedPreference, String key) {
		 if (key.equals(DURATION)) {
			 Preference duration = findPreference(key);
			 duration.setSummary(sharedPreference.getString(key, " "));
		 }
	 }*/
}
