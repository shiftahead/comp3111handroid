package com.comp3111.localendar;

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
	/* User Defined variable
	 *  1. localendarMap
	 *  2. Marker
	 */
	private GoogleMap localenderMap;
	private ConnectionDetector mapConnectionDetector;
	private GPSTracker mapGpsTracker;

	//A test Marker
	private Marker testMarker;
	
	public void setMap(GoogleMap map, ConnectionDetector iDetector, GPSTracker gDetector){
		localenderMap = map;
		mapConnectionDetector = iDetector;
		mapGpsTracker = gDetector;
				
		UiSettings localenderMapSettings = localenderMap.getUiSettings();
        localenderMapSettings.setZoomControlsEnabled(true);
        localenderMapSettings.setCompassEnabled(true);
        localenderMapSettings.setMyLocationButtonEnabled(true);
        localenderMapSettings.setScrollGesturesEnabled(true);
        localenderMapSettings.setRotateGesturesEnabled(true);
        localenderMap.setMyLocationEnabled(true);
        
        //get my location 
        localenderMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                        
                           @Override
                              public boolean onMyLocationButtonClick() {
                                    // TODO Auto-generated method stub
                                    return false;
                              }
                     });
        
        //testMarker created;
        testMarker = localenderMap.addMarker(new MarkerOptions()
        .position(new LatLng(22.3375, 114.2630))
        .title("COMP3111H Lecture").snippet("Today\n15:00 - 16:30\nLT-E").draggable(true));
        
        //set Marker called;
        setMarker();
        setInfoWindow();

	}
	
	public GoogleMap getMyGoogleMap(){
		return localenderMap;
	}
	
	public void setMarker(){
		 localenderMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            
            @Override
            public boolean onMarkerClick(Marker arg0) {
                    // 
                    if(arg0.equals(testMarker) && mapConnectionDetector.isConnectingToInternet() && mapGpsTracker.canGetLocation()){
                        arg0.setVisible(false);
                        return true;
                    }
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

	public void setInfoWindow(){
        localenderMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
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
