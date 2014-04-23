package com.comp3111.localendar.test;

import static com.comp3111.localendar.database.DatabaseConstants.COMPULSORY;
import static com.comp3111.localendar.database.DatabaseConstants.DAY;
import static com.comp3111.localendar.database.DatabaseConstants.DESCRIPTION;
import static com.comp3111.localendar.database.DatabaseConstants.DURATION_HOUR;
import static com.comp3111.localendar.database.DatabaseConstants.DURATION_MINUTE;
import static com.comp3111.localendar.database.DatabaseConstants.HOUR;
import static com.comp3111.localendar.database.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.database.DatabaseConstants.MINUTE;
import static com.comp3111.localendar.database.DatabaseConstants.MONTH;
import static com.comp3111.localendar.database.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.database.DatabaseConstants.TITLE;
import static com.comp3111.localendar.database.DatabaseConstants.TRANSPORTATION;
import static com.comp3111.localendar.database.DatabaseConstants.YEAR;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;

import com.comp3111.localendar.*;
import com.comp3111.localendar.R;
import com.comp3111.localendar.support.*;
import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.calendar.AddEventActivity;
import com.comp3111.localendar.calendar.MyCalendar;
import com.comp3111.localendar.database.DatabaseHelper;
import com.comp3111.localendar.map.MyGoogleMap;
import com.comp3111.localendar.map.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

//sendkey
//keyevent
//keycode

public class MyGoogleMapTest extends ActivityInstrumentationTestCase2<Localendar> {

	public MyGoogleMap mapInstance;
	public GoogleMap localenderMap;
	public Localendar localendar;
	public ArrayList<Polyline> line = new ArrayList<Polyline>();
	
//	@Override
	
	public MyGoogleMapTest() {
		super(Localendar.class);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		localendar = getActivity();
		localenderMap = ((MapFragment) localendar.getFragmentManager().findFragmentById(R.id.map)).getMap();

}

//	public void testMyGoogleMap() {
////		assertNotNull(mapInstance);
//		assertNotNull(localenderMap);
//	}
//	
//	public void testPathing() {
//		MyGoogleMap.pathing();
//		assertNotNull(line);
//	}
//	

    public void testpath(){
//    	assertNull(MyGoogleMap.path("123", "3123"));
    	List<LatLng> Instance = new ArrayList<LatLng>();
    	List<LatLng> Instance1 = new ArrayList<LatLng>(); 
//    	assertNotSame(Instance, MyGoogleMap.path("123", "3123"));
    	//assertTrue(MyGoogleMap.path("123", "3123").isEmpty());
    }
    
	public void testaddmarker(){

		Handler nHandler = new Handler();

    nHandler.post(new Runnable() {
        public void run()
        {
    		double lat = 22.4030, longl = 114.2449;
    		LatLng latlng = new LatLng(lat, longl);
//    		assertNotNull(mapInstance.addmarker(new Place("MaOnShan", lat, longl),true));

        }
    });
//		assertNotNull(Instance.addMarker(new MarkerOptions().position(latlng).draggable(true)));
	}
//	
//	public void testdrawPath(){
//		MyGoogleMap.drawPath();
//	}
//	
}
