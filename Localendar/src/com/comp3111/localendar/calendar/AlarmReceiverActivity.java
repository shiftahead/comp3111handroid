package com.comp3111.localendar.calendar;

import java.io.IOException;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3111.localendar.R;
import com.comp3111.localendar.SettingsFragment;
import com.comp3111.localendar.map.MyGoogleMap;

public class AlarmReceiverActivity extends Activity{

    private MediaPlayer mMediaPlayer;
    private long realTimeNeed;
    private Location myCurrentLocation;
	private int numMessages = 0;
	private NotificationManager myNotificationManager; 
	private TextView textView;
	private Vibrator v;

    String eventTitle;
    String eventYear;
    String eventMonth;
    String eventDay;
    String eventHour;
    String eventMinute;
    String eventVenue;
    String eventTransportation;
    long expectTimeNeed;
    boolean vibrate;
    String alarm;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        eventTitle = getIntent().getStringExtra("title");
        eventYear = getIntent().getStringExtra("year");
        eventMonth = getIntent().getStringExtra("month");
        eventDay = getIntent().getStringExtra("day");
        eventHour = getIntent().getStringExtra("hour");
        eventMinute = getIntent().getStringExtra("minute");
        eventVenue = getIntent().getStringExtra("venue");
        eventTransportation = getIntent().getStringExtra("transportation");
        expectTimeNeed = getIntent().getLongExtra("ExpectTimeNeeded", 0);
        
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsFragment.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        vibrate = sharedPreferences.getBoolean(SettingsFragment.VIBRATE, false);
        alarm = sharedPreferences.getString(SettingsFragment.RINGTONE, "default ringtone");
        
        myCurrentLocation = MyGoogleMap.getMyLocation();
        realTimeNeed = MyGoogleMap.travelingTime(Double.toString(myCurrentLocation.getLatitude()) + "," + Double.toString(myCurrentLocation.getLongitude()), eventVenue, eventTransportation, eventYear, eventMonth, eventDay, eventHour, eventMinute);
        realTimeNeed = realTimeNeed * 1000;
        
		Toast.makeText(this, Long.toString(realTimeNeed), Toast.LENGTH_SHORT).show();
				
        if(realTimeNeed > expectTimeNeed){
            super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
	                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	        setContentView(R.layout.alarm);
	        
	        displayNotification();
	        
	        Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
	        stopAlarm.setOnTouchListener(new OnTouchListener() {
	            public boolean onTouch(View arg0, MotionEvent arg1) {
	                mMediaPlayer.stop();
	                v.cancel();
	                finish();
	                return false;
	            }
	        });
	        playSound(this, getAlarmUri());	
	    }
        else{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.about_us);
        	finish();
        	return;
        }
    }

    private void displayNotification() {
		// TODO Auto-generated method stub

        
        textView= (TextView) findViewById(R.id.EventDetailAlarm);
        textView.setText("Time is up for"+eventTitle);

    	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		
		mBuilder.setContentTitle("New alarm");
		mBuilder.setContentText("Time's up for " + eventTitle + " !");
		mBuilder.setTicker("New alarm!");
		mBuilder.setSmallIcon(R.drawable.small_logo);
		
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		
		String[] events = new String[3];
		events[0] = eventTitle;
		events[1] = eventYear + '/' + eventMonth + '/' + eventDay + "  " + eventHour + ":" + eventMinute;
		events[2] = eventVenue;
		
		inboxStyle.setBigContentTitle("Details");
		for (int i = 0; i < events.length; ++i)
			inboxStyle.addLine(events[i]);
		mBuilder.setStyle(inboxStyle);
		
		mBuilder.setNumber(numMessages);
		mBuilder.setAutoCancel(true);
		
		myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	      // pass the Notification object to the system 
	    myNotificationManager.notify(111, mBuilder.build());
	}

	private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
            if (vibrate) {
            long[] pattern = {500,500};
            v.vibrate(pattern, 0);
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification, 
    //Otherwise, ring-tone.
    private Uri getAlarmUri() {
    	Uri alert =Uri.parse(alarm);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}
