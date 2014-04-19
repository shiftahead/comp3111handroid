package com.comp3111.localendar.map;


import static android.provider.BaseColumns._ID;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.calendar.MyCalendar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

//import com.google.maps.android.PolyUtil;
import com.comp3111.localendar.database.*;
import com.comp3111.localendar.calendar.*;

public class MyGoogleMap {
	public static MyGoogleMap mapInstance;
	public static GoogleMap localenderMap;
	//private ConnectionDetector mapConnectionDetector;
	//private GPSTracker mapGpsTracker;

	//A test Marker
	private Marker testMarker;
	//A test polyline
	private static ArrayList<Polyline> line = new ArrayList<Polyline>();
	private static ArrayList<Marker> marker = new ArrayList<Marker>();
	private static ArrayList<String> marker_id = new ArrayList<String>();
		    
	public MyGoogleMap(GoogleMap map) {
		mapInstance = this;
		localenderMap = map;
		setMap();
	}
	
	public void setMap(){
		
		//Initial settings
		UiSettings localenderMapSettings = localenderMap.getUiSettings();
        localenderMapSettings.setZoomControlsEnabled(true);
        localenderMapSettings.setCompassEnabled(true);
        localenderMapSettings.setMyLocationButtonEnabled(true);
        localenderMapSettings.setScrollGesturesEnabled(true);
        localenderMapSettings.setRotateGesturesEnabled(true);
        localenderMap.setMyLocationEnabled(true);
        
        //Zoom to my current location
        LocationManager locationmanager = (LocationManager) Localendar.instance.getSystemService(Context.LOCATION_SERVICE);;
        
        try {
	        Criteria cri= new Criteria();
	        String provider = locationmanager.getBestProvider(cri, true);
	        Location myLocation = locationmanager.getLastKnownLocation(provider);
	        double latitude= myLocation.getLatitude();
	        double longtitude = myLocation.getLongitude();
	        LatLng ll = new LatLng(latitude, longtitude);
	        localenderMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
        } catch (Exception e){
        	
        }
        
        //get my location 
        localenderMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                        
                           @Override
                              public boolean onMyLocationButtonClick() {
                                    // TODO Auto-generated method stub
                                    return false;
                              }
                     });
        
//        drawPath(path(Place.getPlaceFromAddress("Tsim Sha Tsui Station, Tsim Sha Tsui").getLatLng(), Place.getPlaceFromAddress("Central").getLatLng() ) );
        
//        addmarker(Place.getPlaceFromAddress("HKUST (South), Clear Water Bay".replaceAll("\\s+","")), true);
        
        pathing();
        refresh("");
//        drawPath();
//        drawPath(path("China", "Beijing" ) );
        //set Marker called;
        setMarkerListener();
        setInfoWindowListener();

	}
	
	//The function of addmarker and zoom the camera to the added marker if boolean zoomto is set to true;
//	public static boolean addmarker(Place place, boolean zoomto){
	public static boolean addmarker(Place place, boolean zoomto, String title, String time){
		Marker newMarker = null;
		LatLng ll = null;
		
		int dummy = title.indexOf(".");
		String id = title.substring(0, dummy);
		title = title.substring(dummy+1);
		
		if(marker_id.contains(id))
			return true;
				
		try{
			ll = new LatLng(place.getLatitude(), place.getLongitude());
			newMarker = localenderMap.addMarker(new MarkerOptions().position(ll).draggable(true).title(title).snippet(id));
		} catch(Exception e){
			Toast.makeText(Localendar.instance, "The place cannot be shown on the map", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(zoomto == true)
	        localenderMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
		newMarker.showInfoWindow();
		marker.add(newMarker);
		marker_id.add(id);
		return true;
	}
	
	public void setMarkerListener(){	
		localenderMap.setOnMarkerClickListener(new OnMarkerClickListener(){
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
//				arg0.remove();
				return false;
			}
			
		});
		
		
		
		 localenderMap.setOnMarkerDragListener(new OnMarkerDragListener() {
             
             @Override
             public void onMarkerDragStart(Marker arg0) {
                     // 
                     if(arg0.equals(testMarker)){
                             arg0.setVisible(true);
                     }
             }
             
             @Override
             public void onMarkerDragEnd(Marker arg0) {
                     // 
                     if(arg0.equals(testMarker)){
                             arg0.setVisible(true);
                     }
             }
              @Override
             public void onMarkerDrag(Marker arg0) {
                     // 
                     if(arg0.equals(testMarker)){
                             arg0.setVisible(false);
                     }
             }
     });
  }

	public void setInfoWindowListener(){
		
        localenderMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker testMarker) {
				// TODO Auto-generated method stub
				testMarker.setTitle("My InfoWindow is Clicked!");
			}
		});
	}	
	
	
	// have to be arranged according to time
	public static void pathing(){

		ArrayList <String> location = new ArrayList<String>(), year = new ArrayList<String>()
				, month = new ArrayList<String>(), day = new ArrayList<String>()
				, hour = new ArrayList<String>(), minute = new ArrayList<String>()
				, transportation = new ArrayList<String>();		
		Cursor cursor;

		String[] from = {LOCATION, YEAR, MONTH, DAY, HOUR, MINUTE, TRANSPORTATION};
		SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
		cursor = db.query(TABLE_NAME, from, null, null, null, null, null);
				
		while(cursor.moveToNext()){
			location.add(cursor.getString(0));
			year.add(cursor.getString(1));
			month.add(cursor.getString(2));
			day.add(cursor.getString(3));
			hour.add(cursor.getString(4));
			minute.add(cursor.getString(5));
			transportation.add(cursor.getString(6));
		}
		
		if(!location.isEmpty()){
			int dummy = 0;
			while(location.size() > dummy+1){
//				drawPath(path(Place.getPlaceFromAddress(location.get(dummy)), Place.getPlaceFromAddress(location.get(dummy+1)) ) );
				//depricated 
				drawPath(findingPath(location.get(dummy), location.get(dummy+1), transportation.get(dummy+1)
						, year.get(dummy+1), month.get(dummy+1), day.get(dummy+1), hour.get(dummy+1), minute.get(dummy+1)) );
				
				dummy=dummy+1;
			}
		}
		
	}
	
//	public static void refresh(){
	public static void refresh(String id){
		// refresh Marker
		if(!id.isEmpty()){ //edit or delete events occur
			if(marker_id.contains(id)){
				Cursor cursor;

				String[] from = {_ID, TITLE, LOCATION, HOUR, MINUTE};
				SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
				cursor = db.query(TABLE_NAME, from, null, null, null, null, null);
				Boolean found = false;
				ArrayList<String> locationl = new ArrayList<String>();
							
				while(cursor.moveToNext()){
					locationl.add(cursor.getString(2));
					if(cursor.getString(0).contentEquals(id)){ //edit events
						String title = cursor.getString(1), location = cursor.getString(2)
								, hour = cursor.getString(3), minute = cursor.getString(4);
						
						Marker erasedmarker = marker.get(marker_id.indexOf(id));
						erasedmarker.remove();
						
						addmarker(Place.getPlaceFromAddress(location), true, id + "." + title, new String(hour + ":" + minute));
						
						found = true;
						break;
					}
				}
				if(!found){//delete event
					for(int i=0 ; i<marker_id.size() ; i++){
						if(!locationl.contains(marker_id.get(i))){
							Marker erasedmarker = marker.get(i);
							erasedmarker.remove();
							marker.remove(i);
							marker_id.remove(i);
							break;
						}
					}
				}
			}
		}
		//refresh Polyline (path)
		for(Polyline Line: line){
			Line.remove();
		}
	 	line.clear();
	 	pathing();
	}
		
    private static final String LOG_TAG = "Localendar";
    private static final String OUT_JSON = "/json"; 
	private static final String DIRECTIONS_API_BASE = "http://maps.googleapis.com/maps/api/directions";
	private static final String API_KEY = "AIzaSyC0-Vqt6_XSDsU57zjEnP6YMtB_S5JKqj0";
	
    public static List<LatLng> findingPath(String input1, String input2, String mode, 
    		String arrival_year, String arrival_month, String arrival_day, String arrival_hour, String arrival_minute) {
//    public static List<LatLng> findingPath(String input1, String input2){
    	
    String resultList = new String();
    List<LatLng> resultcoor = new ArrayList<LatLng>();
	
    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {
        StringBuilder sb = new StringBuilder(DIRECTIONS_API_BASE + OUT_JSON);
        sb.append("?origin="+ input1.replaceAll("\\s+",""));
        sb.append("&destination="+input2.replaceAll("\\s+",""));
        sb.append("&sensor=false");
        
        if(!mode.contentEquals("Drive")){  //default is drive
        	sb.append("&mode=");
        	if(mode.contentEquals("On foot"))
        		sb.append("walking");
        	if(mode.contentEquals("Public transportation")){
        		sb.append("&arrival_time="+timeCalculation(arrival_year, arrival_month, arrival_day, arrival_hour, arrival_minute));
        		sb.append("transit");
        	}
        }
        
        
//      sb.append("&mode=");
//      switch(mode)
//      sb.append((switch())) dirving, walking, transit
      
//		sb.append("&departure_time="); / sb.append("&arrival_time=");
//calculateTime(){
        
//      <string-array name="transportation">
//      <item>Drive</item>
//      <item>Public transportation</item>
//      <item>On foot</item>
//</string-array>

        URL url = new URL(sb.toString());
        conn = (HttpURLConnection) url.openConnection();
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        
        // Load the results into a StringBuilder
        int read;
        char[] buff = new char[1024];
        while ((read = in.read(buff)) != -1) {
            jsonResults.append(buff, 0, read);
        }
    } catch (MalformedURLException e) {
        Log.e(LOG_TAG, "Error processing Places API URL", e);
        return resultcoor;
    } catch (IOException e) {
        Log.e(LOG_TAG, "Error connecting to Places API", e);
        return resultcoor;
    } finally {
        if (conn != null) {
            conn.disconnect();
        }
    }

    try {
        JSONObject jsonObj = new JSONObject(jsonResults.toString());
        JSONArray routesJsonArray = jsonObj.getJSONArray("routes");
        if(!routesJsonArray.isNull(0)){
        	JSONObject routes = routesJsonArray.optJSONObject(0);
        	JSONObject overviewPolylines = routes.optJSONObject("overview_polyline");
        	resultList = overviewPolylines.optString("points");
        }
    } catch (JSONException e) {
        Log.e(LOG_TAG, "Cannot process JSON results", e);
    }
    
  	resultcoor = decode(resultList);
    
	return resultcoor;
	
}
    
// to be disposed
//    public static List<LatLng> path(Place input1, Place input2) {
//    	
//    String resultList = new String();
//    List<LatLng> resultcoor = new ArrayList<LatLng>();
//	
//    HttpURLConnection conn = null;
//    StringBuilder jsonResults = new StringBuilder();
//    try {
//        StringBuilder sb = new StringBuilder(DIRECTIONS_API_BASE + OUT_JSON);
//        sb.append("?origin="+String.valueOf(input1.getLatitude())+","+String.valueOf(input1.getLongitude()));
//        sb.append("&destination="+String.valueOf(input2.getLatitude())+","+String.valueOf(input2.getLongitude()));
//        sb.append("&sensor=false");
//        
//
//        URL url = new URL(sb.toString());
//        conn = (HttpURLConnection) url.openConnection();
//        InputStreamReader in = new InputStreamReader(conn.getInputStream());
//
//        // Load the results into a StringBuilder
//        int read;
//        char[] buff = new char[1024];
//        while ((read = in.read(buff)) != -1) {
//            jsonResults.append(buff, 0, read);
//        }
//    } catch (MalformedURLException e) {
//        Log.e(LOG_TAG, "Error processing Places API URL", e);
//        return resultcoor;
//    } catch (IOException e) {
//        Log.e(LOG_TAG, "Error connecting to Places API", e);
//        return resultcoor;
//    } finally {
//        if (conn != null) {
//            conn.disconnect();
//        }
//    }
//
//    try {
//        JSONObject jsonObj = new JSONObject(jsonResults.toString());
//        JSONArray routesJsonArray = jsonObj.optJSONArray("routes");
//        JSONObject routes = routesJsonArray.optJSONObject(0);
//        JSONObject overviewPolylines = routes.optJSONObject("overview_polyline");
//        resultList = overviewPolylines.optString("points");
//        
//    } catch (JSONException e) {
//        Log.e(LOG_TAG, "Cannot process JSON results", e);
//    }
//    
//   	resultcoor = decode(resultList);
//    
//	return resultcoor;
//	
//    }
    
    private static String timeCalculation(String syear, String smonth, String sday, String shour, String sminute){
    	
    	Calendar time = new GregorianCalendar(Integer.parseInt(syear), Integer.parseInt(smonth), 
    								Integer.parseInt(sday), Integer.parseInt(shour), Integer.parseInt(sminute));
    	    	
    	return String.valueOf(time.getTimeInMillis());
    }
    
	
    //Note that this functions is an extract from PolyUtil, an open-source library
    //the URL is https://github.com/googlemaps/android-maps-utils
    public static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }
   
  //for testing purpose
//	public static void drawPath(){
//		List<LatLng> lat = new ArrayList<LatLng>(); 
//		lat.add(new LatLng(22.3375, 114.2630));		
//		lat.add(new LatLng(22.4515, 114.0081));
//		lat.add(new LatLng(22.3184, 114.1699));
//
//		line.add(localenderMap.addPolyline(new PolylineOptions()
//	    .addAll(lat)
//	         .width(15)
//	    .geodesic(true)));
//	}
	
	public static void drawPath(List<LatLng> list){	
//		if(!list.isEmpty())
		line.add(localenderMap.addPolyline(new PolylineOptions()
	    .addAll(list)
	         .width(5)
	    .geodesic(true)));
	}
    
	

}
