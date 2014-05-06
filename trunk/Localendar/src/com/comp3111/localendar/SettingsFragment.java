package com.comp3111.localendar;


import com.comp3111.localendar.map.MyGoogleMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.TextUtils;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, OnPreferenceChangeListener {
	
	
	public static final String DURATION = "duration_key";
	public static final String IS_COMPULSORY = "is_compulsory_key";
	public static final String TRANSPORTATION = "transportation_key";
	public static final String MARKER = "marker_color_key";
	public static final String RINGTONE = "ring_tone_key";
	public static final String VIBRATE = "vibrate_key";
	public static final String SHARED_PREFS_NAME = "com.comp3311.localendar_settings";
	
	ListPreference mDuration;
	CheckBoxPreference mCompulsory;
	ListPreference mTransportation;
	ListPreference mMarker;
	CheckBoxPreference mVibrate;
	RingtonePreference mRingtone;
	
	
	public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME , Context.MODE_PRIVATE);
    }
	
	
	/** Set the default shared preferences in the proper context */
    public static void setDefaultValues(Context context) {
        PreferenceManager.setDefaultValues(context, SHARED_PREFS_NAME, Context.MODE_PRIVATE,
                R.xml.preferences, false);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Activity activity = getActivity();
        // Make sure to always use the same preferences file regardless of the package name
        // we're running under
        final PreferenceManager preferenceManager = getPreferenceManager();
        final SharedPreferences sharedPreferences = getSharedPreferences(activity);
        preferenceManager.setSharedPreferencesName(SHARED_PREFS_NAME);
        
		addPreferencesFromResource(R.xml.preferences);
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        mVibrate = (CheckBoxPreference) preferenceScreen.findPreference(VIBRATE);
        mRingtone = (RingtonePreference) preferenceScreen.findPreference(RINGTONE);
        String ringToneUri = getRingTonePreference(activity);
        // Set the ringToneUri to the backup-able shared pref only so that
        // the Ringtone dialog will open up with the correct value.
        final Editor editor = preferenceScreen.getEditor();
        editor.putString(SettingsFragment.RINGTONE, ringToneUri).apply();
        String ringtoneDisplayString = getRingtoneTitleFromUri(activity, ringToneUri);
        mRingtone.setSummary(ringtoneDisplayString == null ? "" : ringtoneDisplayString);
        mDuration = (ListPreference) preferenceScreen.findPreference(DURATION);
        mCompulsory = (CheckBoxPreference) preferenceScreen.findPreference(IS_COMPULSORY);
        mTransportation = (ListPreference) preferenceScreen.findPreference(TRANSPORTATION);
        mMarker = (ListPreference) preferenceScreen.findPreference(MARKER);
        mDuration.setSummary(mDuration.getEntry());
        mTransportation.setSummary(mTransportation.getEntry());
        mMarker.setSummary(mMarker.getEntry());

        if (mMarker.getEntry().equals("Blue"))
        	MyGoogleMap.setDefaultMarkerColor("Blue");
        else if (mMarker.getEntry().equals("Red"))
        	MyGoogleMap.setDefaultMarkerColor("Red");
        else if (mMarker.getEntry().equals("Green"))
        	MyGoogleMap.setDefaultMarkerColor("Green");
	}
	
    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        setPreferenceListeners(this);
    }
    
    /**
     * Sets up all the preference change listeners to use the specified
     * listener.
     */
    private void setPreferenceListeners(OnPreferenceChangeListener listener) {
        mDuration.setOnPreferenceChangeListener(listener);
        mTransportation.setOnPreferenceChangeListener(listener);
        mMarker.setOnPreferenceChangeListener(listener);
        mRingtone.setOnPreferenceChangeListener(listener);
        mVibrate.setOnPreferenceChangeListener(listener);
        mCompulsory.setOnPreferenceChangeListener(listener);
    }
    
    @Override
    public void onStop() {
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        setPreferenceListeners(null);
        super.onStop();
    }
	
	 public static String getRingTonePreference(Context context) {
	        SharedPreferences prefs = context.getSharedPreferences(
	                SettingsFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
	        String ringtone = prefs.getString(SettingsFragment.RINGTONE, null);
            setRingTonePreference(context, ringtone);
	        return ringtone;
	    }

	 public static void setRingTonePreference(Context context, String value) {
	        SharedPreferences prefs = context.getSharedPreferences(
	                SettingsFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
	        prefs.edit().putString(SettingsFragment.RINGTONE, value).apply();
	    }

	@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        
    }
	
	/*public void onSharedPreferenceChanged(SharedPreferences sharedPreference, String key) {
	 if (key.equals(DURATION)) {
		 Preference duration = findPreference(key);
		 duration.setSummary(sharedPreference.getString(key, " "));
	 }
}*/
	
	@Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final Activity activity = getActivity();
        SharedPreferences prefs = activity.getSharedPreferences(
                SettingsFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        if (preference == mDuration) {
        	mDuration.setValue((String) newValue);
        	mDuration.setSummary(mDuration.getEntry());
        	prefs.edit().putString(SettingsFragment.DURATION, (String) newValue).apply();
        } else if (preference == mTransportation) {
        	mTransportation.setValue((String) newValue);
        	mTransportation.setSummary(mTransportation.getEntry());
        	prefs.edit().putString(SettingsFragment.TRANSPORTATION, (String) newValue).apply();
        } 
          else if (preference == mMarker) {
        	mMarker.setValue((String) newValue);
        	mMarker.setSummary(mMarker.getEntry());
        	
            
            if (mMarker.getEntry().equals("Blue"))
            	MyGoogleMap.setDefaultMarkerColor("Blue");
            else if (mMarker.getEntry().equals("Red"))
            	MyGoogleMap.setDefaultMarkerColor("Red");
            else if (mMarker.getEntry().equals("Green"))
            	MyGoogleMap.setDefaultMarkerColor("Green");
        	
        	prefs.edit().putString(SettingsFragment.MARKER, (String) newValue).apply();
        }
          else if (preference == mRingtone) {
            if (newValue instanceof String) {
                setRingTonePreference(activity, (String) newValue);
                String ringtone = getRingtoneTitleFromUri(activity, (String) newValue);
                mRingtone.setSummary(ringtone == null ? "" : ringtone);
            }
            return true;
        }
          else if (preference == mVibrate) {
        	  mVibrate.setChecked((Boolean) newValue);
        	  return true;
          }
          else if (preference == mCompulsory) {
        	  mCompulsory.setChecked((Boolean) newValue);
        	  return true;
          }
          else {
            return true;
        }
        return false;
    }
	
    public String getRingtoneTitleFromUri(Context context, String uri) {
        if (TextUtils.isEmpty(uri)) {
            return null;
        }
        Ringtone ring = RingtoneManager.getRingtone(getActivity(), Uri.parse(uri));
        if (ring != null) {
            return ring.getTitle(context);
        }
        return null;
    }

	
	public static SharedPreferences getSharedPreferences(Context context, String prefsName) {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
    }
}
