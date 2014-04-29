package com.comp3111.localendar.calendar;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.comp3111.localendar.R;

public class SearchResult extends Activity{
	private ArrayList<String> eventID = new ArrayList<String>();
	private ArrayList<String> eventTitle = new ArrayList<String>();
	private String number;
	private ListView listView;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.search_result);
        listView = (ListView) findViewById(R.id.result_list);
        Intent intent = getIntent();
        number=intent.getExtras().getString("numbers"); 
        int i = Integer.parseInt(number);
        for(int j = 0 ; j<i;j++){
        	String id = intent.getExtras().getString("ID"+Integer.toString(j));  
        	String title = intent.getExtras().getString("TITLE"+Integer.toString(j));  
        	eventID.add(id);
        	eventTitle.add(title);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,eventTitle);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String itemString = (String)parent.getItemAtPosition(position);
				int i = eventTitle.indexOf(itemString);
				String event_id = eventID.get(i);
				Intent intent = new Intent (SearchResult.this, EventDetailActivity.class);	
				intent.putExtra("ID", event_id);
				startActivity(intent);
				finish();
			}

          });
	}
}
