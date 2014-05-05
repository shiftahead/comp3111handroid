package com.comp3111.localendar.calendar;

import static android.provider.BaseColumns._ID;
import static com.comp3111.localendar.database.DatabaseConstants.YEAR;
import static com.comp3111.localendar.database.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.database.DatabaseConstants.MONTH;
import static com.comp3111.localendar.database.DatabaseConstants.DAY;
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
import android.view.WindowManager;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setTitle("   Event Search");
        findButton = (Button) findViewById(R.id.btnFind);
        findButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String input = null;
				input=searchEditText.getText().toString();
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
		String id,location,Description,title,year,month,day,date;
		ArrayList<String> matchList = new ArrayList<String>();
		ArrayList<String> matchListTitle = new ArrayList<String>();
		ArrayList<String> matchListDate = new ArrayList<String>();
		String[] from = {_ID, TITLE, DESCRIPTION, LOCATION,YEAR,MONTH,DAY};
		SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
		cursor = db.query(TABLE_NAME, from, null, null, null, null, null);
		if(cursor!=null && cursor.getCount()>0)
			cursor.moveToFirst();
		else{
			Toast.makeText(this, "The calendar is empty", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		int i = 0;
		do{
			 id = cursor.getString(cursor.getColumnIndex(_ID));
			 location = cursor.getString(cursor.getColumnIndex(LOCATION));
			 Description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
			 title = cursor.getString(cursor.getColumnIndex(TITLE));
			 year= cursor.getString(cursor.getColumnIndex(YEAR));
			 month= cursor.getString(cursor.getColumnIndex(MONTH));
			 day= cursor.getString(cursor.getColumnIndex(DAY));
			 date=year+"/"+month+"/"+day;
			// Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
			if(input==null || ambigiousSearch(input, location) || ambigiousSearch(input, Description) || ambigiousSearch(input, title)){
				i++;
				matchList.add(id);
				matchListTitle.add(title);
				matchListDate.add(date);
			}
		}while(cursor.moveToNext());
		if(i!=0){
			Intent intent = new Intent (this,SearchResult.class);
			intent.putExtra("numbers", Integer.toString(i));
            for(int j=0;j<i;j++){
            	String index = matchList.get(j); 
            	intent.putExtra("ID"+Integer.toString(j), index);
            	String event = matchListTitle.get(j); 
            	intent.putExtra("TITLE"+Integer.toString(j), event);
            	String eventDate = matchListDate.get(j); 
            	intent.putExtra("DATE"+Integer.toString(j), eventDate);
            }
			startActivity(intent);
			finish();
		}
		else{
			Toast.makeText(this, "no result", Toast.LENGTH_SHORT).show();
			searchEditText.setText(" ");
		}
	}
	public Boolean ambigiousSearch(String input, String compare) {
		if(compare.toLowerCase().contains(input.toLowerCase()))
			return true;
		return false;
	}
}
