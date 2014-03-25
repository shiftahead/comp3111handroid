package com.comp3111.localendar;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

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


public class MyGoogleMap {
	private GoogleMap localenderMap;
	//private ConnectionDetector mapConnectionDetector;
	//private GPSTracker mapGpsTracker;
	private Context mContext;
	
	//A test Marker
	private Marker testMarker;
	//A test polyline
	private Polyline line;
		    
	public MyGoogleMap(Context c) {
		mContext = c;
	}
	
	public void setMap(GoogleMap map){
		localenderMap = map;
		
		//Initial settings
		UiSettings localenderMapSettings = localenderMap.getUiSettings();
        localenderMapSettings.setZoomControlsEnabled(true);
        localenderMapSettings.setCompassEnabled(true);
        localenderMapSettings.setMyLocationButtonEnabled(true);
        localenderMapSettings.setScrollGesturesEnabled(true);
        localenderMapSettings.setRotateGesturesEnabled(true);
        localenderMap.setMyLocationEnabled(true);
        
        //Zoom to my current location
        LocationManager locationmanager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);;
        
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
        
//        drawline();
        drawline(path("China", "Beijing"));
        
        
        //set Marker called;
        setMarkerListener();
        setInfoWindowListener();

	}
	
	public GoogleMap getMyGoogleMap(){
		return localenderMap;
	}
	
	//The function of addmarker and zoom the camera to the added marker if boolean zoomto is set to true;
	public void addmarker(double lag, double lon, boolean zoomto){
		Marker m = localenderMap.addMarker(new MarkerOptions()
        .position(new LatLng(lag, lon)).draggable(true));
		
		if(zoomto == true){
			LatLng ll = new LatLng(lag, lon);
	        localenderMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));	
		}
	}
	
	public void setMarkerListener(){	 
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
			public void onInfoWindowClick(Marker arg0) {
				// TODO Auto-generated method stub
				arg0.setTitle("My InfoWindow is Clicked!");
			}
		});
	}
	
	public void drawline(){
		List<LatLng> lat = new ArrayList<LatLng>(); 
		lat.add(new LatLng(22.3375, 114.2630));		
		lat.add(new LatLng(22.4515, 114.0081));
		lat.add(new LatLng(22.3184, 114.1699));
		
		ArrayList<LatLng> array= new ArrayList<LatLng> (lat);
		
////		ShowPath path1=new ShowPath();
//		List<LatLng> list = path("Brooklyn", "Queens");
		
		
		line = localenderMap.addPolyline(new PolylineOptions()
	    .addAll(lat)
	         .width(65)
	    .geodesic(true));
	}
	
	public void drawline(List<LatLng> list){
		List<LatLng> lat = new ArrayList<LatLng>(); 
		lat.add(new LatLng(22.3375, 114.2630));		
		lat.add(new LatLng(22.4515, 114.0081));
		lat.add(new LatLng(22.3184, 114.1699));
		
		ArrayList<LatLng> array= new ArrayList<LatLng> (lat);
		
////		ShowPath path1=new ShowPath();
//		List<LatLng> list = path("Brooklyn", "Queens");
		
		
		line = localenderMap.addPolyline(new PolylineOptions()
	    .addAll(list)
	         .width(15)
	    .geodesic(true));
	}
	
    private static final String LOG_TAG = "Localendar";
    private static final String OUT_JSON = "/json";
//    private static final String API_KEY = "AIzaSyDMyI5sg3IptPBQm31tEmFn5mPDTKSw-Ag";
    
	private static final String DIRECTIONS_API_BASE = "http://maps.googleapis.com/maps/api/directions";
	private static final String API_KEY = "AIzaSyC0-Vqt6_XSDsU57zjEnP6YMtB_S5JKqj0";
	
	

    public List<LatLng> path(String input1, String input2) {
    	
    String resultList = new String();
    List<LatLng> resultcoor = new ArrayList<LatLng>();
	
    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {
        StringBuilder sb = new StringBuilder(DIRECTIONS_API_BASE + OUT_JSON);
        sb.append("?origin="+input1);
        sb.append("&destination="+input2);
        sb.append("&sensor=false");
        

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
        // Create a JSON object hierarchy from the results
        JSONObject jsonObj = new JSONObject(jsonResults.toString());
        
//\
//        if(routeArray.size() > 0) {
//            JSONObject routes = routeArray.getJSONObject(0);
//            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
//            encodedString = overviewPolylines.getString("points");
//
//            List<LatLng> list = decodePoly(encodedString);
//        }
//        
        
        JSONArray routesJsonArray = jsonObj.getJSONArray("routes");
        JSONObject routes = routesJsonArray.getJSONObject(0);
        JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
        resultList = overviewPolylines.getString("points");

     
        // 
//        resultList = jsonObj.getString("routes").getString("overview_polyline").getString("points");
        
//        resultList = new ArrayList<String>(predsJsonArray.length());
//        for (int i = 0; i < predsJsonArray.length(); i++) {
//            resultList.add(predsJsonArray.getJSONObject(i).getString("points"));
//        }
    } catch (JSONException e) {
        Log.e(LOG_TAG, "Cannot process JSON results", e);
    }

    resultcoor = decode(resultList);
    
	return resultcoor;
	
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
    
	
	
}
