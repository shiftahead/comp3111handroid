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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.comp3111.localendar.Localendar;
import com.comp3111.localendar.R;
import com.comp3111.localendar.calendar.AddEventActivity;
import com.comp3111.localendar.calendar.MyCalendar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
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
import com.comp3111.localendar.support.ClearableAutoCompleteTextView;
import com.comp3111.localendar.support.PlacesAutoCompleteAdapter;
import com.comp3111.localendar.calendar.*;

import android.view.View;
public class MyGoogleMap {
	public static MyGoogleMap mapInstance;
	public static GoogleMap localendarMap;
	//private ConnectionDetector mapConnectionDetector;
	//private GPSTracker mapGpsTracker;

	private static ArrayList<Polyline> pathList = null; 
	private static ArrayList<Marker> markerList = null;
	private static ArrayList<String> marker_id = null;
	
	private static DrawingPath path = null;
		    
	public MyGoogleMap(GoogleMap map) {
		mapInstance = this;
		localendarMap = map;
		setMap();
	}
	
	public void setMap(){
		
		//Initial settings
		UiSettings localenderMapSettings = localendarMap.getUiSettings();
        localenderMapSettings.setZoomControlsEnabled(true);
        localenderMapSettings.setCompassEnabled(true);
        localenderMapSettings.setMyLocationButtonEnabled(true);
        localenderMapSettings.setScrollGesturesEnabled(true);
        localenderMapSettings.setRotateGesturesEnabled(true);
        localendarMap.setMyLocationEnabled(true);
        
        pathList = new ArrayList<Polyline>();
    	markerList = new ArrayList<Marker>();
    	marker_id = new ArrayList<String>();
        
        //Zoom to my current location
        LocationManager locationmanager = (LocationManager) Localendar.instance.getSystemService(Context.LOCATION_SERVICE);;
        
        try {
	        Criteria cri= new Criteria();
	        String provider = locationmanager.getBestProvider(cri, true);
	        Location myLocation = locationmanager.getLastKnownLocation(provider);
	        double latitude= myLocation.getLatitude();
	        double longtitude = myLocation.getLongitude();
	        LatLng ll = new LatLng(latitude, longtitude);
	        localendarMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
        } catch (Exception e){
        	
        }
        
        //get my location 
        localendarMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                        
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
        setMapListener();

	}
// move camera to corresponding place
	public void  moveCamera(Place place) {
		LatLng ll = null;
    	ll = new LatLng(place.getLatitude(), place.getLongitude());
    	localendarMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
	}
	
	public void setMapListener(){
		localendarMap.setOnMapLongClickListener(new OnMapLongClickListener() {

	             @Override
	             public void onMapLongClick(LatLng arg0) {
	                 // TODO Auto-generated method stub
	                 Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
	                 Place place;
	                 place = Place.getPlaceFromCoordinate(arg0.latitude, arg0.longitude);
	         		Toast.makeText(Localendar.instance, place.getName(), Toast.LENGTH_SHORT).show();
//	             	addmarker(Place.getPlaceFromAddress(placeSearch), true, placeSearch, "Click to add a event");
	             }
	         });
	}
	
	//The function of addmarker and zoom the camera to the added marker if boolean zoomto is set to true;
//	public static boolean addmarker(Place place, boolean zoomto){
	public static boolean addmarker(Place place, boolean zoomto, String title, String time){
		Marker newMarker = null;
		LatLng ll = null;
		
		int dummy = title.indexOf(".");
		String id = new String(); 
		if(dummy>0){
			id = title.substring(0, dummy);
			title = title.substring(dummy+1);
		}
		
		if(!id.isEmpty() && marker_id.contains(id)){
			Toast.makeText(Localendar.instance, "The marker has already be added on the map", Toast.LENGTH_SHORT).show();
			return true;
		}
		try{
			ll = new LatLng(place.getLatitude(), place.getLongitude());
			newMarker = localendarMap.addMarker(new MarkerOptions().position(ll).draggable(true).title(title).snippet(time));
		} catch(Exception e){
			Toast.makeText(Localendar.instance, "The place cannot be shown on the map", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(zoomto == true)
	        localendarMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
		newMarker.showInfoWindow();
		if(!id.isEmpty())   //event exists for that marker
			markerList.add(newMarker);
			marker_id.add(id);
		return true;
	}
	
	public void setMarkerListener(){	
		localendarMap.setOnMarkerClickListener(new OnMarkerClickListener(){
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
//				arg0.remove();
				//for testing
				time = travelingTime("Tin Shui Wai", "Yuen Long", "Drive", "", "", "", "", "");
				Toast.makeText(Localendar.instance, String.valueOf(time), Toast.LENGTH_SHORT).show();
				return false;
			}
			
		});

		
		 localendarMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			 
//        	 final ImageView deleteMarker = (Localendar.instance).deleteMarker;
//        	 final RadioGroup radio = (Localendar.instance).mainRadio;
			 
			 LatLng origMarkerPosition;
			 
             @Override
             public void onMarkerDragStart(Marker arg0) {
            	 origMarkerPosition = arg0.getPosition();
            	 Localendar.instance.mainRadio.setVisibility(View.GONE);
            	 Localendar.instance.deleteMarker.setVisibility(View.VISIBLE);
            	 
             }
             
             @Override
            public void onMarkerDrag(Marker arg0) {
            	 final ImageView trashBin = Localendar.instance.deleteMarker;
            	 Point markerScreenPosition = localendarMap.getProjection().toScreenLocation(arg0.getPosition());
         	    if (overlap(markerScreenPosition, trashBin)) {
         	        trashBin.setImageResource(R.drawable.cancel_button_pressed);
        	    } else
        	    	trashBin.setImageResource(R.drawable.cancel_button_normal);

            }
             
             @Override
             public void onMarkerDragEnd(Marker arg0) {
            	 Localendar.instance.mainRadio.setVisibility(View.VISIBLE);
            	 Localendar.instance.deleteMarker.setVisibility(View.GONE);
            	 
            	 final ImageView trashBin = Localendar.instance.deleteMarker;
            	 Point markerScreenPosition = localendarMap.getProjection().toScreenLocation(arg0.getPosition());
            	 
            	 if (overlap(markerScreenPosition, trashBin)) {
            	   	if(markerList.contains(arg0)){
            	   		marker_id.remove(markerList.indexOf(arg0));
            	   		markerList.remove(markerList.indexOf(arg0));
            	   	}
            	     arg0.remove();
            	 } else
            		 arg0.setPosition(origMarkerPosition);
         			
             }
             
             Boolean overlap(Point mkrScnPosition, ImageView trashBin){
            	 	int[] imgCoords = new int[2];
            	 	trashBin.getLocationOnScreen(imgCoords);
//            	    Log.e(TAG, " ****** Img x:" + imgCoords[0] + " y:" + imgCoords[1] + "    Point x:" + point.x + "  y:" + point.y + " Width:" + imgview.getWidth() + " Height:" + imgview.getHeight());
//            	    boolean overlapX = mkrScnPosition.x < imgCoords[0] + imgview.getWidth() && point.x > imgCoords[0] - imgview.getWidth();
            	    boolean overlapY = mkrScnPosition.y > imgCoords[1] - 2*trashBin.getHeight();
//            	    return overlapX && overlapY;
//            	    Toast.makeText(Localendar.instance, mkrScnPosition.toString() + "VS " + String.valueOf(imgCoords[1]) +"," + trashBin.getHeight() , Toast.LENGTH_SHORT).show();
            	    return overlapY;
//            	 return true;
             }

     });
  }

	public void setInfoWindowListener(){
		
        localendarMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker arg0) {
				if(!markerList.contains(arg0)){
					Toast.makeText(Localendar.instance, "Adding Events!", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Localendar.instance, AddEventActivity.class);
					Localendar.instance.startActivity(intent);
					Localendar.instance.overridePendingTransition(R.anim.left_in, R.anim.right_out);
				}
				else{
					Toast.makeText(Localendar.instance, "Jumping to details page", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Localendar.instance, EventDetailActivity.class);
					intent.putExtra("ID", marker_id.get(markerList.indexOf(arg0)));
					Localendar.instance.startActivity(intent);
				}
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
		
		Calendar today = Calendar.getInstance();
		
		String[] from = {LOCATION, YEAR, MONTH, DAY, HOUR, MINUTE, TRANSPORTATION};
		SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
		String selection = YEAR + " = " + (Localendar.calendar.getTime().getYear() + 1900) + " AND " +
				MONTH + " = "  + (Localendar.calendar.getTime().getMonth() + 1) + " AND " +
				DAY + " = " + Localendar.calendar.getTime().getDate();
//		cursor = db.query(TABLE_NAME, from, null, null, null, null, null);
		cursor = db.query(TABLE_NAME, from, selection, null, null, null, HOUR + ", " + MINUTE);
		

				
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
			List<String> url= new ArrayList<String>();
			while(location.size() > dummy+1){
//				drawPath(path(Place.getPlaceFromAddress(location.get(dummy)), Place.getPlaceFromAddress(location.get(dummy+1)) ) );
				//depricated 
//				url.add(URLformation(location.get(dummy), location.get(dummy+1), transportation.get(dummy+1)
//						, year.get(dummy+1), month.get(dummy+1), day.get(dummy+1), hour.get(dummy+1), minute.get(dummy+1)));
//				drawPath(findingPath(location.get(dummy), location.get(dummy+1), transportation.get(dummy+1)
//								, year.get(dummy+1), month.get(dummy+1), day.get(dummy+1), hour.get(dummy+1), minute.get(dummy+1)) );
				
				new DrawingPath().execute(URLformation(location.get(dummy), location.get(dummy+1), transportation.get(dummy+1)
								, year.get(dummy+1), month.get(dummy+1), day.get(dummy+1), hour.get(dummy+1), minute.get(dummy+1)));
				
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
						
						Marker erasedmarker = markerList.get(marker_id.indexOf(id));
						erasedmarker.remove();
						
						addmarker(Place.getPlaceFromAddress(location), true, id + "." + title, new String(hour + ":" + minute));
						
						found = true;
						break;
					}
				}
				if(!found){//delete event
					for(int i=0 ; i<marker_id.size() ; i++){
						if(!locationl.contains(marker_id.get(i))){
							Marker erasedmarker = markerList.get(i);
							erasedmarker.remove();
							markerList.remove(i);
							marker_id.remove(i);
							break;
						}
					}
				}
			}
		}
		//refresh Polyline (path)
		for(Polyline Line: pathList){
			Line.remove();
		}
	 	pathList.clear();
	 	pathing();
	}
	
	static String URLformation(String input1, String input2, String mode, 
    	String arrival_year, String arrival_month, String arrival_day, String arrival_hour, String arrival_minute){
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
        return sb.toString();
	}
	
	public static class DrawingPath extends AsyncTask<String, Void, String>{
		@Override
	    protected String doInBackground(String... urlString) {
	        // TODO Auto-generated method stub

			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();
			
		try{
	        URL url = new URL(urlString[0]);
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
	        return "";
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return "";
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
			
			return jsonResults.toString();
	    }
	 
	    @Override
	    protected void onPreExecute() {
	    }
	 
	    @Override
	    protected void onProgressUpdate(Void... values) {
	        // TODO Auto-generated method stub
	        super.onProgressUpdate();
	        
			Toast.makeText(Localendar.instance, "Updating Map...", Toast.LENGTH_SHORT).show();
	    }
	   
	    @Override
	    protected void onPostExecute(String jsonResults) {
	        // TODO Auto-generated method stub
	        super.onPostExecute(jsonResults);
	        
	        String resultList = new String();
	        List<LatLng> resultcoor = new ArrayList<LatLng>();
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
	      	drawPath(resultcoor);
	      	
//			Toast.makeText(Localendar.instance, "Map Updated!", Toast.LENGTH_SHORT).show();
	    }
	 
	    @Override
	    protected void onCancelled() {
	    }
	}
		
    private static final String LOG_TAG = "Localendar";
    private static final String OUT_JSON = "/json"; 
	private static final String DIRECTIONS_API_BASE = "http://maps.googleapis.com/maps/api/directions";
	
    public static List<LatLng> findingPath(String input1, String input2, String mode, 
    		String arrival_year, String arrival_month, String arrival_day, String arrival_hour, String arrival_minute) {
//    public static List<LatLng> findingPath(String input1, String input2){
    	
    String resultList = new String();
    List<LatLng> resultcoor = new ArrayList<LatLng>();
	
    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {
    	//forming URL
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
    //getting encoded path
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
   //decode the path
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
		pathList.add(localendarMap.addPolyline(new PolylineOptions()
	    .addAll(list)
	         .width(5)
	    .geodesic(true)));
	}
	private static long time;
	
	// locaiton1 for orign, locaiton 2 for destincation
	public static long travelingTime(String location1, String location2, String transportation
			, String year, String month, String day, String hour, String minute){ 
		new LocaitonReminder().execute(URLformation(location1, location2, transportation
				, year, month, day, hour, minute));
		return time;
	}
    
	public static class LocaitonReminder extends AsyncTask<String, Void, String>{
		@Override
	    protected String doInBackground(String... urlString) {
	        // TODO Auto-generated method stub

			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();
			
		try{
	        URL url = new URL(urlString[0]);
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
	        return "";
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return "";
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
			return jsonResults.toString();
	    }
	 
	    @Override
	    protected void onPreExecute() {
	    }
	 
	    @Override
	    protected void onProgressUpdate(Void... values) {
	    }
	   
	    @Override
	    protected void onPostExecute(String jsonResults) {
	        // TODO Auto-generated method stub
	        super.onPostExecute(jsonResults);
	        
	        String value = new String();
	        List<LatLng> resultcoor = new ArrayList<LatLng>();
	        try {
	            JSONObject jsonObj = new JSONObject(jsonResults.toString());
	            JSONArray routesJsonArray = jsonObj.getJSONArray("routes");
	            if(!routesJsonArray.isNull(0)){
	            	JSONObject routes = routesJsonArray.optJSONObject(0);
	            	JSONArray legs = routes.optJSONArray("legs");
	            	JSONObject legsobj = legs.optJSONObject(0);
	            	JSONObject duration = legsobj.optJSONObject("duration");
	            	value = duration.optString("value");
	            }
//	            
//	            JSONObject jsonObj = new JSONObject(jsonResults.toString());
//	            JSONArray routesJsonArray = jsonObj.getJSONArray("routes");
//	            if(!routesJsonArray.isNull(0)){
//	            	JSONObject routes = routesJsonArray.optJSONObject(0);
//	            	JSONObject overviewPolylines = routes.optJSONObject("overview_polyline");
//	            	resultList = overviewPolylines.optString("points");
//	            }
	            
	            
	        } catch (JSONException e) {
	            Log.e(LOG_TAG, "Cannot process JSON results", e);
	        }
	      		time = Integer.valueOf(value);
//			Toast.makeText(Localendar.instance, time, Toast.LENGTH_SHORT).show();
	    }
	 
	    @Override
	    protected void onCancelled() {
	    }
	}
	

}
