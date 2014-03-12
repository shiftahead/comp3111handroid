package com.comp3111.localendar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

public class MapActivity extends Activity {
	
	private GoogleMap map;
	private ConnectionDetector mapConnectionDetector;
	private GPSTracker mapGpsTracker;
	//A test Marker
	private Marker testMarker;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		UiSettings localenderMapSettings = map.getUiSettings();
        localenderMapSettings.setZoomControlsEnabled(true);
        localenderMapSettings.setCompassEnabled(true);
        localenderMapSettings.setMyLocationButtonEnabled(true);
        localenderMapSettings.setScrollGesturesEnabled(true);
        localenderMapSettings.setRotateGesturesEnabled(true);
        map.setMyLocationEnabled(true);
	
	//Zoom to my current location
    
    LocationManager locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);;
    
    try {
        Criteria cri= new Criteria();
        String provider = locationmanager.getBestProvider(cri, true);
        Location myLocation = locationmanager.getLastKnownLocation(provider);
        double latitude= myLocation.getLatitude();
        double longtitude = myLocation.getLongitude();
        LatLng ll = new LatLng(latitude, longtitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
    } catch (Exception e){
    	
    }
    
    
    //get my location 
    map.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                    
                       @Override
                          public boolean onMyLocationButtonClick() {
                                // TODO Auto-generated method stub
                                return false;
                          }
                 });
    
    //testMarker created;
    testMarker = map.addMarker(new MarkerOptions()
    .position(new LatLng(22.3375, 114.2630))
    .title("COMP3111H Lecture").snippet("Today\n15:00 - 16:30\nLT-E").draggable(true));
    
    //set Marker called;
    setMarker();
    setInfoWindow();

}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	public GoogleMap getMyGoogleMap(){
		return map;
	}
	
	public void setMarker(){
		 map.setOnMarkerClickListener(new OnMarkerClickListener() {
	        
	        @Override
	        public boolean onMarkerClick(Marker arg0) {
	                // 
	                if(arg0.equals(testMarker)){
	                    arg0.setVisible(false);
	                    return true;
	                }
	                return false;
	        }
	});
	map.setOnMarkerDragListener(new OnMarkerDragListener() {
	         
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
	
	public void setInfoWindow(){
	    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker arg0) {
				// TODO Auto-generated method stub
				if(arg0.equals(testMarker)){
					testMarker.setSnippet("Hello!");
					testMarker.showInfoWindow();
				}
				else{
					testMarker.setSnippet("No");
					testMarker.showInfoWindow();
				}
				
			}
		});
	}
	
	
}
