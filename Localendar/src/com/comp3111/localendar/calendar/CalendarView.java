package com.comp3111.localendar.calendar;

/*
 * Partial from Chris Gao <chris@exina.net>
 */


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class CalendarView extends ImageView {
    private static int CELL_WIDTH = 0;
    private static int CELL_HEIGHT = 0;
    private static int CELL_MARGIN_TOP = 0;
    private static int CELL_MARGIN_LEFT = 0;
    private static float CELL_TEXT_SIZE = 10;
    
	private Calendar mRightNow = null;
    private DayCell mToday = null;
    private DayCell[][] mCells = new DayCell[6][7];
    MonthDisplayHelper mHelper;
    Drawable mDecoration = null;
    
	public CalendarView(Context context) {
		this(context, null);
	}
	
	public CalendarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDecoration = context.getResources().getDrawable(R.drawable.calendar_today);
		initCalendarView();
	}
	
	@SuppressWarnings("deprecation")
	public String getMonthView() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM, yyyy");
		String date  = sdf.format(new Date(mHelper.getYear() - 1900, mHelper.getMonth(), 1));
		return date;
	}
	private void initCalendarView() {
		mRightNow = Calendar.getInstance();
		// set background
		setImageResource(R.drawable.month_background);
    }
	
	private void initCells() {
	    class _calendar {
	    	public int day;
	    	public boolean thisMonth;
	    	public _calendar(int d, boolean b) {
	    		day = d;
	    		thisMonth = b;
	    	}
	    	public _calendar(int d) {
	    		this(d, false);
	    	}
	    };
	    
	    mHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR), mRightNow.get(Calendar.MONTH));
	    _calendar tmp[][] = new _calendar[6][7];
	    
	    for(int i=0; i<tmp.length; i++) {
	    	int n[] = mHelper.getDigitsForRow(i);
	    	for(int d=0; d<n.length; d++) {
	    		if(mHelper.isWithinCurrentMonth(i,d))
	    			tmp[i][d] = new _calendar(n[d], true);
	    		else
	    			tmp[i][d] = new _calendar(n[d]);
	    		
	    	}
	    }

	    Calendar today = Calendar.getInstance();
	    int thisDay = 0;
	    mToday = null;
	    if(mHelper.getYear()==today.get(Calendar.YEAR) && mHelper.getMonth()==today.get(Calendar.MONTH)) {
	    	thisDay = today.get(Calendar.DAY_OF_MONTH);
	    }
		// build cells
		Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH+CELL_MARGIN_LEFT, CELL_HEIGHT+CELL_MARGIN_TOP);
		for(int week=0; week<mCells.length; week++) {
			for(int day=0; day<mCells[week].length; day++) {
				if(tmp[week][day].thisMonth) {
					if(day==0 || day==6 )
						mCells[week][day] = new RedCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
					else 
						mCells[week][day] = new DayCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
				} else {
					mCells[week][day] = new GrayCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
				}
				
				Bound.offset(CELL_WIDTH, 0); // move to next column 
				
				// get today
				if(tmp[week][day].day==thisDay && tmp[week][day].thisMonth) {
					mToday = mCells[week][day];
					mDecoration.setBounds(mToday.getBound());
				}
			}
			Bound.offset(0, CELL_HEIGHT); // move to next row and first column
			Bound.left = CELL_MARGIN_LEFT;
			Bound.right = CELL_MARGIN_LEFT+CELL_WIDTH;
		}		
	}
	
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		
		int height = bottom - top;
		int width = right - left;
		
		//set up cell size
		if(width < height) {
			CELL_WIDTH = (int) width / 7;
			CELL_MARGIN_TOP = CELL_HEIGHT = CELL_WIDTH;
			CELL_TEXT_SIZE = CELL_WIDTH / 2;
		}
		else {
			CELL_HEIGHT = (int) height / 7;
			CELL_MARGIN_TOP = CELL_WIDTH = CELL_HEIGHT;
			CELL_TEXT_SIZE = CELL_HEIGHT / 2;
		}
		
		initCells();
		super.onLayout(changed, left, top, right, bottom);
	}
	
    public void setTimeInMillis(long milliseconds) {
    	mRightNow.setTimeInMillis(milliseconds);
    	initCells();
    	this.invalidate();
    }
        
    public int getYear() {
    	return mHelper.getYear();
    }
    
    public int getMonth() {
    	return mHelper.getMonth();
    }
    
    public int getDay(int x, int y) {
    	int dayofMonth = 0;
    	try{
    		dayofMonth = mCells[x][y].getDayOfMonth();
    	} catch(Exception e) {
    		dayofMonth = 1;
    	}
    	return dayofMonth;
    }
    
    public void nextMonth() {
    	mHelper.nextMonth();
    	mRightNow.set(getYear(), getMonth(), mHelper.getFirstDayOfMonth());
    	initCells();
    	invalidate();
    }
    
    public void previousMonth() {
    	mHelper.previousMonth();
    	mRightNow.set(getYear(), getMonth(), mHelper.getFirstDayOfMonth());
    	initCells();
    	invalidate();
    }

    
    public Calendar getDate(int x, int y) {
    	int dayofMonth = mCells[x][y].getDayOfMonth();
    	if(mCells[x][y] instanceof GrayCell) {
    		if(dayofMonth < 15)
    			nextMonth();
    		else
    			previousMonth();
    	}
    	
    	mRightNow.set(getYear(), getMonth(), dayofMonth);
    	return mRightNow;
    }

	@Override
	protected void onDraw(Canvas canvas) {
		// draw background
		super.onDraw(canvas);
		
		// draw cells
		for(DayCell[] week : mCells) {
			for(DayCell day : week) {
				day.draw(canvas);			
			}
		}
		
		// draw today
		if(mDecoration!=null && mToday!=null) {
			mDecoration.draw(canvas);
		}
	}
	
	public class GrayCell extends DayCell {
		public GrayCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			mPaint.setColor(Color.LTGRAY);
		}			
	}
	
	private class RedCell extends DayCell {
		public RedCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			mPaint.setColor(0xdddd0000);
		}			
		
	}

}
