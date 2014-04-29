package com.comp3111.localendar.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.comp3111.localendar.R;

public class CalendarSearch extends Activity{
	private Button findButton;
	private Button cancelButton;
	private EditText searchEditText;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.search_event);
        
        findButton = (Button) findViewById(R.id.btnFind);
        findButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String input = searchEditText.getText().toString();
				searchEvent(input);
			}
		});
        cancelButton = (Button) findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        searchEditText = (EditText) findViewById(R.id.etSearch);
      }
	public String searchEvent(String input) {
		return input;
	}
}
