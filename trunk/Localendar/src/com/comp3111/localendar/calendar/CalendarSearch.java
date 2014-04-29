package com.comp3111.localendar.calendar;

import static android.provider.BaseColumns._ID;
import static com.comp3111.localendar.database.DatabaseConstants.HOUR;
import static com.comp3111.localendar.database.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.database.DatabaseConstants.MINUTE;
import static com.comp3111.localendar.database.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.database.DatabaseConstants.TITLE;
import static com.comp3111.localendar.database.DatabaseConstants.DESCRIPTION;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.EventLogTags.Description;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;

public class CalendarSearch extends Activity{
	private Button findButton;
	private Button cancelButton;
	private EditText searchEditText;
	private Cursor cursor = null;
	
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
	public void searchEvent(String input) {
		String id,location,Description,title;
		ArrayList<String> matchList = new ArrayList<String>();
		String[] from = {_ID, TITLE, DESCRIPTION, LOCATION,};
		SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
		cursor = db.query(TABLE_NAME, from, null, null, null, null, null);
		if(cursor!=null)
			cursor.moveToFirst();
		int i = 0;
		while(cursor.moveToNext()){
			 id = cursor.getString(cursor.getColumnIndex(_ID));
			 location = cursor.getString(cursor.getColumnIndex(LOCATION));
			 Description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
			 title = cursor.getString(cursor.getColumnIndex(TITLE));
			if(ambigiousSearch(input, location) || ambigiousSearch(input, Description) || ambigiousSearch(input, title)){
				i++;
				matchList.add(id);
			}
		}
		if(i!=0){
			Intent intent = new Intent (this,EventDetailActivity.class);
			intent.putExtra("numbers", Integer.toString(i));
            for(int j=0;j<i;j++){
            	String index = matchList.get(j); 
            	intent.putExtra("ID"+Integer.toString(j), index);
            }
			startActivity(intent);
			finish();
		}
		else{
			Toast.makeText(this, "no result", Toast.LENGTH_SHORT).show();
		}
	}
	public Boolean ambigiousSearch(String input, String compare) {
		compare.toLowerCase().contains(input.toLowerCase());
		return false;
	}
}
