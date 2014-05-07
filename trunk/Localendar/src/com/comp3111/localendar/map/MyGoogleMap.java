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
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

//import com.google.maps.android.PolyUtil;
import com.comp3111.localendar.database.*;
import com.comp3111.localendar.facebook.FacebookLogin;
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
	
	private static float DEFAULTMARKERCOLOR = BitmapDescriptorFactory.HUE_RED;
	
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
        Location myLocation = getMyLocation();
		LatLng ll = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
    	localendarMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
    	
    	
        //get my location 
        localendarMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                        
                           @Override
                              public boolean onMyLocationButtonClick() {
                                    // TODO Auto-generated method stub
                                    return false;
                              }
                     });
       
        pathing();
        refresh("");
       
        //set Marker called;
        setMarkerListener();
        setInfoWindowListener();
        setMapListener();

	}

	public static Location getMyLocation() {
		LocationManager locationmanager = (LocationManager) Localendar.instance.getSystemService(Context.LOCATION_SERVICE);;
        
        try {
	        Criteria cri= new Criteria();
	        String provider = locationmanager.getBestProvider(cri, true);
	        Location myLocation = locationmanager.getLastKnownLocation(provider);
	        return myLocation;
        } catch (Exception e){
        	
        }
		return null;
	}
// move camera to corresponding place
	public void  moveCamera(Place place) {
		LatLng ll = null;
    	ll = new LatLng(place.getLatitude(), place.getLongitude());
    	localendarMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
	}
	
	public void setMapListener(){
		
		localendarMap.setOnMapLongClickListener(new OnMapLongClickListener() {
// Location returned from coordinate is not accurate enough to implement addmarker function. 
	             @Override
	             public void onMapLongClick(LatLng arg0) {
	                 // TODO Auto-generated method stub
//	                 Log.d("arg0", arg0.latitude + "-" + arg0.longitude);
//	                 Place place;
//	                 place = Place.getPlaceFromCoordinate(arg0.latitude, arg0.longitude);
//	         		Toast.makeText(Localendar.instance, place.getName(), Toast.LENGTH_SHORT).show();
//	             	addmarker(Place.getPlaceFromAddress(placeSearch), true, placeSearch, "Click to add a event");
	             }
	         });
		 

	}
	
	//The function of addmarker and zoom the camera to the added marker if boolean zoomto is set to true;
	public static boolean addmarker(Place place, boolean draggable, String id, String title, String time){
		Marker newMarker = null;
		LatLng ll = null;
		
		if(!id.isEmpty() && marker_id.contains(id)){
			Toast.makeText(Localendar.instance, "The marker has already be added on the map", Toast.LENGTH_SHORT).show();
			return true;
		}
		try{
			ll = new LatLng(place.getLatitude(), place.getLongitude());
			newMarker = localendarMap.addMarker(new MarkerOptions().position(ll).draggable(draggable).title(title).snippet(time).
					icon((BitmapDescriptorFactory.defaultMarker(DEFAULTMARKERCOLOR))));
		} catch(Exception e){
			Toast.makeText(Localendar.instance, "The place cannot be shown on the map", Toast.LENGTH_SHORT).show();
			return false;
		}
		localendarMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
		newMarker.showInfoWindow();
		if(!id.isEmpty())   //event exists for that marker
			markerList.add(newMarker);
			marker_id.add(id);
		return true;
	}
	
	public static Marker addmarker(Place place, String title, String time){
		Marker newMarker = null;
		LatLng ll = null;
		
		try{
			ll = new LatLng(place.getLatitude(), place.getLongitude());
			newMarker = localendarMap.addMarker(new MarkerOptions().position(ll).draggable(false).title(title).snippet(time).
					icon((BitmapDescriptorFactory.defaultMarker(DEFAULTMARKERCOLOR))));
		} catch(Exception e){
			Toast.makeText(Localendar.instance, "The place cannot be shown on the map", Toast.LENGTH_SHORT).show();
			return null;
		}
		localendarMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
		newMarker.showInfoWindow();

		return newMarker;
	}
	
	public void setMarkerListener(){	
		localendarMap.setOnMarkerClickListener(new OnMarkerClickListener(){
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				//for testing
//				Toast.makeText(Localendar.instance, String.valueOf(time), Toast.LENGTH_SHORT).show();
				return false;
			}
			
		});

		
		 localendarMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			 LatLng origMarkerPosition;
			 
             @Override
             public void onMarkerDragStart(Marker arg0) {
            	 
            	 
            	String location = null;
            	if(markerList.contains(arg0)){
            		Cursor cursor;
            		String[] from = {_ID, LOCATION};
            		SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
            		cursor = db.query(TABLE_NAME, from, "_ID="+marker_id.get(markerList.indexOf(arg0)), null, null, null, null);
            		while(cursor.moveToNext()){
            			location = cursor.getString(1);
            		}
            	}
            	else
            		location = arg0.getTitle();
            	
            	Place plocation = Place.getPlaceFromAddress(location);
     			origMarkerPosition = new LatLng(plocation.getLatitude(), plocation.getLongitude());

//     			origMarkerPosition = arg0.getPosition();
            	 Localendar.instance.mainRadio.setVisibility(View.GONE);
            	 Localendar.instance.deleteMarker.setVisibility(View.VISIBLE);
            	 Localendar.instance.facebookShare.setVisibility(View.VISIBLE);
            	 arg0.setAlpha((float) 0.6);
             }
             
             @Override
            public void onMarkerDrag(Marker arg0) {
            	 final ImageView trashBin = Localendar.instance.deleteMarker;
            	 final ImageView facebookShare = Localendar.instance.facebookShare;
            	 Point markerScreenPosition = localendarMap.getProjection().toScreenLocation(arg0.getPosition());

            	 switch(overlap(markerScreenPosition, trashBin)){
           	 		case 0:
           	 			trashBin.setImageResource(R.drawable.ic_action_discard);
           	 			facebookShare.setImageResource(R.drawable.ic_action_share);
           	 			break;
           	 		case 1:
           	 			trashBin.setImageResource(R.drawable.ic_action_discard_pressed);
           	 			facebookShare.setImageResource(R.drawable.ic_action_share);
           	 			break;
           	 		case -1:
           	 			facebookShare.setImageResource(R.drawable.ic_action_share_pressed);
           	 			trashBin.setImageResource(R.drawable.ic_action_discard);
           	 			break;
            	 }
         	    
            }
             
             @Override
             public void onMarkerDragEnd(Marker arg0) {
            	 arg0.setAlpha((float) 1);
            	 Localendar.instance.mainRadio.setVisibility(View.VISIBLE);
            	 Localendar.instance.deleteMarker.setVisibility(View.GONE);
            	 Localendar.instance.facebookShare.setVisibility(View.GONE);
            	 final ImageView trashBin = Localendar.instance.deleteMarker;
            	 final ImageView facebookShare = Localendar.instance.facebookShare;
            	 Point markerScreenPosition = localendarMap.getProjection().toScreenLocation(arg0.getPosition());
            	 
            	 switch(overlap(markerScreenPosition, trashBin)){
            	 	case 0:
            	 		arg0.setPosition(origMarkerPosition);
            	 		break;
            	 	case 1:
            	 		arg0.remove();
            	   		marker_id.remove(markerList.indexOf(arg0));
            	   		markerList.remove(markerList.indexOf(arg0));
            	   		break;
            	 	case -1:
            	 		arg0.setPosition(origMarkerPosition);
         				Cursor cursor;
         				String[] from = {_ID, LOCATION, MONTH, DAY, HOUR, MINUTE, DESCRIPTION};
         				SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
         				cursor = db.query(TABLE_NAME, from, "_ID="+marker_id.get(markerList.indexOf(arg0)), null, null, null, null);
         				String location = null, month = null, day = null, hour = null, minute = null, description = null;
         				while(cursor.moveToNext()){
         					location = cursor.getString(1);		month = cursor.getString(2);		day = cursor.getString(3);
         					hour = cursor.getString(4);		minute = cursor.getString(5);		description = cursor.getString(6);
         				}                      
            	 		
            	    	Intent intent = new Intent(Localendar.instance, FacebookLogin.class);
            	    	intent.putExtra("TITLE", arg0.getTitle());
            	    	intent.putExtra("DESCRIPTION", description);
            	    	intent.putExtra("TIME", month + "/" + day + " " + hour + ":" + minute);
            	    	intent.putExtra("LOCATION", location);
            	    	Localendar.instance.startActivity(intent);
            	 		break;
            	 }
            	 
             }
             
             int overlap(Point mkrScnPosition, ImageView trashBin){  //1 trash bin, -1 share, 0 not overlap
         	 	int[] imgCoords = new int[2];
         	 	trashBin.getLocationOnScreen(imgCoords);
         	 	boolean overlapX = mkrScnPosition.x > imgCoords[0] ;
         	    boolean overlapY = mkrScnPosition.y > imgCoords[1] - 3*trashBin.getHeight();
         	    if(overlapX && overlapY)
         	    	return 1;
         	    if(!overlapX&&overlapY)
         	    	return -1;
         	    return 0;
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
					intent.putExtra("location", arg0.getTitle());
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
	
	public static void setMarkerColor(String color){
		if(color.contentEquals("Blue"))
			for(Marker marker: markerList){
				marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			}
		if(color.contentEquals("Green"))
			for(Marker marker: markerList){
				marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			}
		if(color.contentEquals("Red"))
			for(Marker marker: markerList){
				marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			}
	}
	
	public static void setDefaultMarkerColor(String color){
		if(color.contentEquals("Blue"))
			DEFAULTMARKERCOLOR = BitmapDescriptorFactory.HUE_BLUE;
		if(color.contentEquals("Green"))
			DEFAULTMARKERCOLOR = BitmapDescriptorFactory.HUE_GREEN;
		if(color.contentEquals("Red"))
			DEFAULTMARKERCOLOR = BitmapDescriptorFactory.HUE_RED;
	}
	
	public static String getDefaultMarkerColor(){
		if(DEFAULTMARKERCOLOR == BitmapDescriptorFactory.HUE_BLUE)
			return "Blue";
		if(DEFAULTMARKERCOLOR == BitmapDescriptorFactory.HUE_GREEN)
			return "HUE_GREEN";
		if(DEFAULTMARKERCOLOR == BitmapDescriptorFactory.HUE_RED)
			return "RED";
		return "";
	}
	
	
	// have to be arranged according to time
	public static void pathing(){

		ArrayList <String> location = new ArrayList<String>(), year = new ArrayList<String>()
				, month = new ArrayList<String>(), day = new ArrayList<String>()
				, hour = new ArrayList<String>(), minute = new ArrayList<String>()
				, transportation = new ArrayList<String>();		
		Cursor cursor;
		
//		Calendar today = Calendar.getInstance();
		
		String[] from = {LOCATION, YEAR, MONTH, DAY, HOUR, MINUTE, TRANSPORTATION};
		SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
		String selection = YEAR + " = " + (Localendar.calendar.getTime().getYear() + 1900) + " AND " +
				MONTH + " = "  + (Localendar.calendar.getTime().getMonth() + 1) + " AND " +
				DAY + " = " + Localendar.calendar.getTime().getDate();;
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
			while(location.size() > dummy+1){
				new DrawingPath().execute(URLformation(location.get(dummy), location.get(dummy+1), transportation.get(dummy+1)
								, year.get(dummy+1), month.get(dummy+1), day.get(dummy+1), hour.get(dummy+1), minute.get(dummy+1)));
				
				dummy=dummy+1;
			}
			
		}
		
	}
	
	public static void refresh(String id){
		// refresh Marker
		if(!id.isEmpty()){ //edit or delete events occur
			if(marker_id.contains(id)){
				Cursor cursor;

				String[] from = {_ID, TITLE, LOCATION, HOUR, MINUTE};
				SQLiteDatabase db = MyCalendar.dbhelper.getReadableDatabase();
				String selection = _ID + " = " + id;
				cursor = db.query(TABLE_NAME, from, selection, null, null, null, null);
				Boolean found = false;

				while(cursor.moveToNext()){ //edit event
					String title = cursor.getString(1), location = cursor.getString(2)
								, hour = cursor.getString(3), minute = cursor.getString(4);
					
					Marker erasedmarker = markerList.get(marker_id.indexOf(id));
					erasedmarker.remove();
					addmarker(Place.getPlaceFromAddress(location), true, id, title, new String(hour + ":" + minute));
						
					found = true;
				}
				
				if(!found){//delete event
					int index = marker_id.indexOf(id);
					markerList.get(index).remove();
					marker_id.remove(id);
					markerList.remove(index);
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
        		sb.append("transit");
        		sb.append("&arrival_time="+timeCalculation(arrival_year, arrival_month, arrival_day, arrival_hour, arrival_minute));
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
	
    private static String timeCalculation(String syear, String smonth, String sday, String shour, String sminute){
    	
    	Calendar time = new GregorianCalendar(Integer.parseInt(syear), Integer.parseInt(smonth), 
    								Integer.parseInt(sday), Integer.parseInt(shour), Integer.parseInt(sminute));
    	long ltime = 0;
    	
    	try {
			ltime = time.getTimeInMillis()/1000;
		} catch (IllegalArgumentException e) {
		}  
    	    	
    	return String.valueOf(ltime);
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
   	
	public static void drawPath(List<LatLng> list){	
		pathList.add(localendarMap.addPolyline(new PolylineOptions()
	    .addAll(list)
	         .width(5)
	    .geodesic(true)));
	}
	private static long time = 0;
	
	// locaiton1 for orign, locaiton 2 for destincation
	public static long travelingTime(String location1, String location2, String transportation
			, String year, String month, String day, String hour, String minute){ 
		
		return locationReminding(URLformation(location1, location2, transportation
				, year, month, day, hour, minute));
	}
    	
	private static int locationReminding(String urlString){
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		
	try{
        URL url = new URL(urlString);
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
        return -1;
    } catch (IOException e) {
        Log.e(LOG_TAG, "Error connecting to Places API", e);
        return -1;
    } finally {
        if (conn != null) {
            conn.disconnect();
        }
    }
		String obejct = jsonResults.toString();

        String value = new String("0");
        try {
            JSONObject jsonObj = new JSONObject(obejct);
            JSONArray routesJsonArray = jsonObj.getJSONArray("routes");
            if(!routesJsonArray.isNull(0)){
            	JSONObject routes = routesJsonArray.optJSONObject(0);
            	JSONArray legs = routes.optJSONArray("legs");
            	JSONObject legsobj = legs.optJSONObject(0);
            	JSONObject duration = legsobj.optJSONObject("duration");
            	value = duration.optString("value");
            }
            
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
      		return Integer.valueOf(value);
	
	}
	
	public static void showPath(){
		for(Polyline Line: pathList){
			Line.setVisible(true);
		}
	}
	

	public static void hidePath(){
		for(Polyline Line: pathList){
			Line.setVisible(false);
		}
	}
	
	public static int pathVisibility(){  // 0 no path, 1 show path, -1 hide path 
		if(pathList.isEmpty())
			return 0;
		if(pathList.get(0).isVisible())
			return 1;
		else
			return -1;
		
	}
	

}
