package com.comp3111.localendar.calendar;

import com.comp3111.localendar.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SearchResult extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.search_result);
	}
}
