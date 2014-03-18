package com.comp3111.localendar;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

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

public class MyGoogleMap {
	private GoogleMap localenderMap;
	//private ConnectionDetector mapConnectionDetector;
	//private GPSTracker mapGpsTracker;
	private Context mContext;
	
	//A test Marker
	private Marker testMarker;
		    
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
	
}
