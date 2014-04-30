package com.comp3111.localendar.calendar;

import java.io.IOException;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.R.*;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3111.localendar.map.MyGoogleMap;
import com.comp3111.localendar.map.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class AlarmReceiverActivity extends Activity{

	private Float distance; // the tolerate final distance between user and destination
	private Place destination; // the destination place
	private float[] actual_distance;
    private MediaPlayer mMediaPlayer;
	private int numMessages = 0;
	private NotificationManager myNotificationManager; 
	private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.alarm);
        

		Toast.makeText(this, Double.toString(MyGoogleMap.mapInstance.getMyLocation().getLatitude()), Toast.LENGTH_SHORT).show();
        displayNotification();
        
        Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
        stopAlarm.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mMediaPlayer.stop();
                finish();
                return false;
            }
        });
        playSound(this, getAlarmUri());
        
    }

    private void displayNotification() {
		// TODO Auto-generated method stub
        String eventTitle = getIntent().getStringExtra("title");
        String eventYear = getIntent().getStringExtra("year");
        String eventMonth = getIntent().getStringExtra("month");
        String eventDay = getIntent().getStringExtra("day");
        String eventHour = getIntent().getStringExtra("hour");
        String eventMinute = getIntent().getStringExtra("minute");
        String eventVenue = getIntent().getStringExtra("venue");
        
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
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification, 
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
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
