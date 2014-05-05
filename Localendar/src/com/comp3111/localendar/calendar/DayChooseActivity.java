package com.comp3111.localendar.calendar;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.map.MyGoogleMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

public class DayChooseActivity extends Activity implements OnClickListener {

	private Button confirm, cancel;
	private DatePicker picker;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.choose_day_dialog);
        setTitle("   Choose Date");
        confirm = (Button) findViewById(R.id.chooseday_confirm);
        cancel = (Button) findViewById(R.id.chooseday_cancel);
        picker = (DatePicker) findViewById(R.id.chooseday_picker);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.chooseday_cancel:
			this.finish();
			break;
		case R.id.chooseday_confirm:
			int day = picker.getDayOfMonth();
			int month = picker.getMonth();
			int year = picker.getYear();
			Localendar.calendar.set(year, month, day);
			MyCalendar.calendarInstance.refresh();
			MyGoogleMap.refresh("");
			MyCalendar.setTimeInMillis(Localendar.calendar.getTimeInMillis());
			MyCalendar.setViewModeToMonth(false);
			Localendar.instance.setCalendarTitle();
			this.finish();
			break;
		default:
			break;
		}
	}
}
