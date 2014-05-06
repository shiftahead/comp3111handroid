package com.comp3111.localendar.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.comp3111.localendar.Appstart;
import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.R.id;
import com.comp3111.localendar.R.layout;
import com.comp3111.localendar.database.DatabaseHelper;
import com.comp3111.localendar.map.MyGoogleMap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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

	private static long DAY_IN_MILLISECOND = 1000 * 3600 * 24;
	public static int DAY_VIEW = 0;
	public static int MONTH_VIEW = 1;
	public static int viewMode = 0;
	
	public static MyCalendar calendarInstance = null;
	public static DatabaseHelper dbhelper = null;
	private static ListView eventList = null;
	private static CalendarView monthView = null;
	private static View calendarFragment = null;
	private static Cursor cursor = null;
	private static boolean checkMode = false;
	private static float CellHeightInPx = dipToPixels(Localendar.instance, 100);
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		calendarInstance = this;
		calendarFragment = inflater.inflate(R.layout.calendar_fragment, container, false); 
		dbhelper = new DatabaseHelper(this.getActivity());
		eventList = (ListView) calendarFragment.findViewById(R.id.events_list);
		monthView = (CalendarView) calendarFragment.findViewById(R.id.calendar_monthview);
		eventList.setVisibility(View.VISIBLE);
		monthView.setVisibility(View.GONE);
		viewMode = DAY_VIEW;
		calendarFragment.setOnTouchListener(new SwipeListener());
		monthView.setOnTouchListener(new MyOnTouchListener());
		refresh();
        return calendarFragment;
    }  
	
	@SuppressWarnings("deprecation")
	public void refresh() {
		eventList.setAdapter(null);
		
		String[] from = {_ID, TITLE, HOUR, MINUTE, LOCATION};
		String selection = YEAR + " = " + (Localendar.calendar.getTime().getYear() + 1900) + " AND " +
							MONTH + " = "  + (Localendar.calendar.getTime().getMonth() + 1) + " AND " +
							DAY + " = " + Localendar.calendar.getTime().getDate();
		
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		cursor = db.query(TABLE_NAME, from, selection, null, null, null, HOUR + ", " + MINUTE);
		
		int[] to = {R.id.item_id, R.id.item_title, R.id.item_hour, R.id.item_minute, R.id.item_location};
		
		final SimpleCursorAdapter adapter = new SimpleCursorAdapter(calendarInstance.getActivity(), R.layout.event_listitem, cursor, from, to);
        eventList.setAdapter(adapter);
        eventList.setOnTouchListener(new MyOnTouchListener());
        
        // Try to implement a contextual action mode 
        eventList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        eventList.setMultiChoiceModeListener(new MyMultiChoiceModeListener());
	}
	
	public static void setTimeInMillis(long milliseconds) {
		monthView.setTimeInMillis(milliseconds);
	}
	
	static void deleteEvent(String id) {
		SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(TABLE_NAME, _ID + "=" + id, null);
	}	
	
	public static void setViewModeToMonth(boolean setMonth) {
		if(setMonth) {
			if(viewMode != MONTH_VIEW) {
				Animation am1 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.right_out);
				Animation am2 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.left_in);
				eventList.startAnimation(am1);
				eventList.setVisibility(View.GONE);
				monthView.startAnimation(am2);
				monthView.setVisibility(View.VISIBLE);
				viewMode = MONTH_VIEW;
			}
		}
		else {
			if(viewMode != DAY_VIEW) {
				Animation am1 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.left_out);
				Animation am2 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.right_in);
				monthView.setAnimation(am1);
				monthView.setVisibility(View.GONE);
				eventList.startAnimation(am2);
				eventList.setVisibility(View.VISIBLE);
				viewMode = DAY_VIEW;
			}
		}
	}
	
	public String getCurrentMonth() {
		return monthView.getMonthView();
	}

	private class SwipeListener implements View.OnTouchListener {
		
		private float downX, downY, upX, upY;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(viewMode == DAY_VIEW) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = event.getX();
					downY = event.getY();
					return true;
				case MotionEvent.ACTION_UP:
					upX = event.getX();
					upY = event.getY();
					if(upY - downY > 200 && Math.abs(downX-upX) < 200) {
						new Handler().postDelayed(new Runnable(){
							@Override
							public void run(){
								Animation am1 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.down_out);
								eventList.startAnimation(am1);
							}
						}, 0);
						new Handler().postDelayed(new Runnable(){
							@Override
							public void run(){
								Localendar.calendar.setTimeInMillis(Localendar.calendar.getTimeInMillis() - DAY_IN_MILLISECOND);
								Localendar.instance.setCalendarTitle();
								refresh();
								MyGoogleMap.refresh("");
								Animation am2 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.up_in);
								eventList.startAnimation(am2);
							}
						}, 200);
					}
					else if(upY - downY < -200 && Math.abs(downX-upX) < 200) {
						new Handler().postDelayed(new Runnable(){
							@Override
							public void run(){
								Animation am1 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.up_out);
								eventList.startAnimation(am1);
							}
						}, 0);
						new Handler().postDelayed(new Runnable(){
							@Override
							public void run(){
								Localendar.calendar.setTimeInMillis(Localendar.calendar.getTimeInMillis() + DAY_IN_MILLISECOND);
								Localendar.instance.setCalendarTitle();
								refresh();
								MyGoogleMap.refresh("");
								Animation am2 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.down_in);
								eventList.startAnimation(am2);
							}
						}, 200);
					}
					else if(upX - downX > 200 && Math.abs(downY-upY) < 100) {
						setViewModeToMonth(true);
						setTimeInMillis(Localendar.calendar.getTimeInMillis());
						Localendar.instance.setCalendarTitle();
						return true;
					}
					return false;
				}
			}
			else if(viewMode == MONTH_VIEW) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = event.getX();
					downY = event.getY();
					return true;
				case MotionEvent.ACTION_UP:
					upX = event.getX();
					upY = event.getY();
					if(upX - downX < -200 && Math.abs(downY-upY) < 100) {
						setViewModeToMonth(false);
						Localendar.instance.setCalendarTitle();
						return true;
					}
				}
			}
			return false;
		}
		
	}
	private class MyOnTouchListener implements View.OnTouchListener  {
		
		private float downX, downY, upX, upY;
		private long time = 0;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(viewMode == DAY_VIEW) {
				if(!checkMode) {
					switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						downX = event.getX();
						downY = event.getY();
						try {
							eventList.getChildAt((int) (downY/CellHeightInPx)).setBackgroundResource(R.color.gray);
						} catch (Exception e) {
							
						}
						
						time = System.currentTimeMillis();
						return false;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						try {
							eventList.getChildAt((int) (downY/CellHeightInPx)).setBackgroundResource(R.color.light_gray);
						} catch(Exception e) {
							
						}
						upX = event.getX();
						upY = event.getY();
						if(Math.abs(downX-upX) < 10 && Math.abs(downY-upY) < 10 && System.currentTimeMillis() - time < 800) {
							if (cursor.moveToPosition((int) (downY/CellHeightInPx))) {
								
								String eventId = cursor.getString(cursor.getColumnIndex(_ID));
								Intent intent = new Intent (calendarInstance.getActivity(), EventDetailActivity.class);	
								intent.putExtra("ID", eventId);
								startActivity(intent);
								return false;
				    		}
						}
						else if(Math.abs(downX-upX) < 200 && upY-downY > 400) {
							new Handler().postDelayed(new Runnable(){
								@Override
								public void run(){
									Animation am1 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.down_out);
									eventList.startAnimation(am1);
								}
							}, 0);
							new Handler().postDelayed(new Runnable(){
								@Override
								public void run(){
									Localendar.calendar.setTimeInMillis(Localendar.calendar.getTimeInMillis() - DAY_IN_MILLISECOND);
									Localendar.instance.setCalendarTitle();
									refresh();
									MyGoogleMap.refresh("");
									Animation am2 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.up_in);
									eventList.startAnimation(am2);
								}
							}, 200);
						}
						else if(Math.abs(downX-upX) < 200 && upY-downY < -400) {
							new Handler().postDelayed(new Runnable(){
								@Override
								public void run(){
									Animation am1 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.up_out);
									eventList.startAnimation(am1);
								}
							}, 0);
							new Handler().postDelayed(new Runnable(){
								@Override
								public void run(){
									Localendar.calendar.setTimeInMillis(Localendar.calendar.getTimeInMillis() + DAY_IN_MILLISECOND);
									Localendar.instance.setCalendarTitle();
									MyGoogleMap.refresh("");
									refresh();
									Animation am2 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.down_in);
									eventList.startAnimation(am2);
								}
							}, 200);
						}
						else if(upX - downX > 200 && Math.abs(downY-upY) < 100) {
							setViewModeToMonth(true);
							setTimeInMillis(Localendar.calendar.getTimeInMillis());
							Localendar.instance.setCalendarTitle();
							return true;
						}
						return false;
					}
				}
			}
			else if(viewMode == MONTH_VIEW) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = event.getX();
					downY = event.getY();
					return true;
				case MotionEvent.ACTION_UP:
					upX = event.getX();
					upY = event.getY();
					int myViewSize = monthView.getWidth() < monthView.getHeight()?monthView.getWidth():monthView.getHeight();
					int x = (int) downX * 7 / myViewSize;
					int y = (int) downY * 7 / myViewSize;
					if(upY - downY > 200 && Math.abs(downX-upX) < 200) {
						new Handler().postDelayed(new Runnable(){
							@Override
							public void run(){
								Animation am1 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.down_out);
								monthView.startAnimation(am1);
							}
						}, 0);
						
						new Handler().postDelayed(new Runnable(){
							@Override
							public void run(){
								monthView.previousMonth();	
								Localendar.instance.setCalendarTitle();
								Animation am2 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.up_in);
								monthView.startAnimation(am2);
							}
						}, 200);
						return false;
					}
					else if(upY - downY < -200 && Math.abs(downX-upX) < 200) {
						new Handler().postDelayed(new Runnable(){
							@Override
							public void run(){
								Animation am1 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.up_out);
								monthView.startAnimation(am1);
							}
						}, 0);
						
						new Handler().postDelayed(new Runnable(){
							@Override
							public void run(){	
								monthView.nextMonth();
								Localendar.instance.setCalendarTitle();
								Animation am2 = AnimationUtils.loadAnimation(Localendar.instance,R.anim.down_in);
								monthView.startAnimation(am2);
							}
						}, 200);
						return false;
					}
					else if(upX - downX < -200 && Math.abs(downY-upY) < 100) {
						setViewModeToMonth(false);
						Localendar.instance.setCalendarTitle();
						return true;
					}
					else if(y < 1 || y > 6 || x > 6) 
						return true;
					else if(Math.abs(downX-upX)<10 && Math.abs(downY-upY)<10) {
						Localendar.calendar.setTimeInMillis(monthView.getDate(y-1, x).getTimeInMillis());
						setViewModeToMonth(false);
						Localendar.instance.setCalendarTitle();
						refresh();
						return true;
					}
				}
			}
			 
			return false;
		}
		
	}
		
   	 private class MyMultiChoiceModeListener implements MultiChoiceModeListener {
   		 
   		private ArrayList<String> selection = new ArrayList<String>(); //Record the id of selected items
   		private ArrayList<Integer> positions = new ArrayList<Integer>();
   		@Override
   	    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
   	        // Here you can do something when items are selected/de-selected,
   	        // such as update the title in the CAB
       		 mode.setTitle("Select item to delete");  
             setSubtitle(mode);
                if (checked) {
               	 	if (cursor.moveToPosition(position)) {
        				String eventId = cursor.getString(cursor.getColumnIndex(_ID));
        				selection.add(eventId);
        				positions.add((Integer)position);
               	 	}
               	 	eventList.getChildAt(position).setBackgroundResource(R.color.gray);
               	 }
                else {
               	 	if (cursor.moveToPosition(position)) {
	         				String eventId = cursor.getString(cursor.getColumnIndex(_ID));
	         				selection.remove(eventId);
	         				positions.remove((Integer)position);
	                }
               	 	eventList.getChildAt(position).setBackgroundResource(R.color.light_gray);
               	}
   	    }        	 
   	 
   		
   	    private void setSubtitle(ActionMode mode) {
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
   	            	positions = null;
   	                deleteSelectedItems();
           	    	refresh();
   	                mode.finish(); // Action picked, so close the CAB
           	    	MyGoogleMap.refresh("");
   	                return true;
   	            default:
   	                return false;
   	        }
   	    }
           
   	    private void deleteSelectedItems() {				
   	    	for (String id:selection) {
   	    		deleteEvent(id);
       	    	MyGoogleMap.refresh(id);
   	    	}
		}
           
   	    private void cancelSelect() {
   	    	if(positions != null) {
	   	    	for (Integer id:positions) {
	   	    		eventList.getChildAt(id).setBackgroundResource(R.color.light_gray);;
	   	    	}
   	    	}
   	    }
		@Override
   	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
   	        mode.getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
   	        checkMode = true;
   	        return true;
   	    }
           
   	    @Override
   	    public void onDestroyActionMode(ActionMode mode) {
   	    	cancelSelect();
   	    	checkMode = false;
   	    }

   	    @Override
   	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
   	        // Here you can perform updates to the CAB due to
   	        // an invalidate() request
   	        return false;
   	    }
	}
   	 
   	//convert dip to pixel 
   	public static float dipToPixels(Context context, float dipValue) {
   	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
   	    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
   	}

}
