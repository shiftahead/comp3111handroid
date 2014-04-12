package com.comp3111.localendar.calendar;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.comp3111.localendar.R;
import com.comp3111.localendar.R.id;
import com.comp3111.localendar.R.layout;
import com.comp3111.localendar.database.DatabaseHelper;
import com.comp3111.localendar.map.MyGoogleMap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import static android.provider.BaseColumns._ID;
import static com.comp3111.localendar.database.DatabaseConstants.DAY;
import static com.comp3111.localendar.database.DatabaseConstants.HOUR;
import static com.comp3111.localendar.database.DatabaseConstants.MINUTE;
import static com.comp3111.localendar.database.DatabaseConstants.MONTH;
import static com.comp3111.localendar.database.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.database.DatabaseConstants.TITLE;
import static com.comp3111.localendar.database.DatabaseConstants.YEAR;
import static com.comp3111.localendar.database.DatabaseConstants.LOCATION;

public class MyCalendar extends Fragment {

	public static MyCalendar calendarInstance = null;
	public static DatabaseHelper dbhelper;
	private ListView eventList;
	private View view;
	static Cursor cursor;
	private ArrayList<Integer> selection = new ArrayList<Integer>(); //Record the id of selected items
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		calendarInstance = this;
		view = inflater.inflate(R.layout.day_fragment, container, false); 
		dbhelper = new DatabaseHelper(this.getActivity());
		eventList = (ListView) view.findViewById(R.id.events_list);
		
		refresh();
        return view;
    }  
	
	void refresh() {
		eventList.setAdapter(null);
		
		String[] from = {_ID, TITLE, HOUR, MINUTE, LOCATION};
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		cursor = db.query(TABLE_NAME, from, null, null, null, null, null);
		
		int[] to = {R.id.item_id, R.id.item_title, R.id.item_hour, R.id.item_minute, R.id.item_location};
		@SuppressWarnings("deprecation")
		final SimpleCursorAdapter adapter = new SimpleCursorAdapter(calendarInstance.getActivity(), R.layout.event_listitem, cursor, from, to);
        eventList.setAdapter(adapter);
        eventList.setOnItemClickListener(new MyOnItemClickListener());
        
        /* Try to implement a contextual action mode */
        eventList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        eventList.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
        	 @Override
        	    public void onItemCheckedStateChanged(ActionMode mode, int position,
        	                                          long id, boolean checked) {
        	        // Here you can do something when items are selected/de-selected,
        	        // such as update the title in the CAB
	        		 mode.setTitle("Select item to delete");  
	                 setSubtitle(mode);  
	                 if (checked)
	                	 selection.add((int) id);
	                	 

        	    }        	 
        	 

        	    private void setSubtitle(ActionMode mode) {
				// TODO Auto-generated method stub
				final int checkedCount = eventList.getCheckedItemCount();
				switch (checkedCount) {
					case 0:
						mode.setSubtitle(null);
						break;
					case 1:
						mode.setSubtitle("Selected 1 item");
						break;
					default:
						mode.setSubtitle("Selected " + checkedCount + " items");
        				break;
				}
			}

				@Override
        	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        	        // Respond to clicks on the actions in the CAB
        	        switch (item.getItemId()) {
        	            case R.id.menu_delete:
        	                deleteSelectedItems();
                	    	MyCalendar.calendarInstance.refresh();
        	                mode.finish(); // Action picked, so close the CAB
                	    	MyGoogleMap.refresh();
        	                return true;
        	            default:
        	                return false;
        	        }
        	    }
                
        	    private void deleteSelectedItems() {
					// TODO Auto-generated method stub
        	    	for (int id:selection) {
        	    		String sid = Integer.toString(id);
        	    		MyCalendar.deleteEvent(sid);
        	    	}
				}
                
				@Override
        	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        	        // Inflate the menu for the CAB
        	        mode.getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
        	        return true;
        	    }
                
        	    @Override
        	    public void onDestroyActionMode(ActionMode mode) {
        	        // Here you can make any necessary updates to the activity when
        	        // the CAB is removed. By default, selected items are deselected/unchecked.
        	    }

        	    @Override
        	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        	        // Here you can perform updates to the CAB due to
        	        // an invalidate() request
        	        return false;
        	    }
		});
	}
	
	static void deleteEvent(String id) {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(TABLE_NAME, _ID + "=" + id, null);
	}	
	
	public class MyOnItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (cursor.moveToPosition(position)) {
				String eventId = cursor.getString(cursor.getColumnIndex(_ID));
				Intent intent = new Intent (calendarInstance.getActivity(), EventDetailActivity.class);	
				intent.putExtra("ID", eventId);
				startActivity(intent);
    		}
		}
		
		
    	
	}

}
