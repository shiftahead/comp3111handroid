package com.comp3111.localendar;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
