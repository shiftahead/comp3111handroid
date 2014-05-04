package com.comp3111.localendar.calendar;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.comp3111.localendar.R;

public class SearchResult extends Activity implements View.OnClickListener {
	private ArrayList<String> eventID = new ArrayList<String>();
	private ArrayList<String> eventTitle = new ArrayList<String>();
	private ArrayList<String> eventDate = new ArrayList<String>();
	private ArrayList<String> showView = new ArrayList<String>();
	private String number;
	private ListView listView;
	private Button exitButton;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.search_result);
        setTitle("   Search Result");
        listView = (ListView) findViewById(R.id.result_list);
        exitButton = (Button) findViewById(R.id.result_exit);
        exitButton.setOnClickListener(this);
        Intent intent = getIntent();
        number=intent.getExtras().getString("numbers"); 
        int i = Integer.parseInt(number);
        for(int j = 0 ; j<i;j++){
        	String id = intent.getExtras().getString("ID"+Integer.toString(j));  
        	String title = intent.getExtras().getString("TITLE"+Integer.toString(j));  
        	String time = intent.getExtras().getString("DATE"+Integer.toString(j));
        	eventID.add(id);
        	eventTitle.add(title);
        	eventDate.add(time);
        }
        
        int max1=0,max2=0;
        for(int k=0;k<i;k++){
        	if(eventTitle.get(k).length()>max1) max1=eventTitle.get(k).length();
        	if(eventDate.get(k).length()>max2) max2=eventDate.get(k).length();
        }
        for(int k=0;k<i;k++){
        	int space = max1+max2-eventTitle.get(k).length()-eventDate.get(k).length();
        	String spareString = createSpace(space);
        	String finaString = eventTitle.get(k)+spareString+eventDate.get(k);
        	showView.add(finaString);
        }
        
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,showView);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String itemString = (String)parent.getItemAtPosition(position);
				int i = showView.indexOf(itemString);
				String event_id = eventID.get(i);
				Intent intent = new Intent (SearchResult.this, EventDetailActivity.class);	
				intent.putExtra("ID", event_id);
				startActivity(intent);
				finish();
			}

          });
	}
	public String createSpace(int i) {
		String number="     ";
		while(i>0){
			i--;
			number+=" ";
		}
		
		return number;
	}
	@Override
	public void onClick(View v) {
		this.finish();
		
	}
}
