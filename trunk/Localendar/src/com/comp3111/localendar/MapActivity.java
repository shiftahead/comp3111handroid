package com.comp3111.localendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

        
        private GoogleMap localenderMap;
        
    //This is just adding a marker for testing, do not add it until an Event is created!
        private Marker testMarker;
        
    @SuppressLint("NewApi")
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localenderMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        // config UI setting
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
        //This pieces of codes are testing the OnMarkerClickListener's availability
        /*
        localenderMap.setOnMarkerClickListener(new OnMarkerClickListener() {
                        
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
        */
        
        //This pieces of codes are testing the OnMarkerDragListener's availability
        /*
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
        */
        
        //This pieces of codes are testing the OnInfoWindowClickListener's availability
        /*
        localenderMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
                        
                        @Override
                        public void onInfoWindowClick(Marker arg0) {
                                // TODO Auto-generated method stub
                                if(arg0.equals(testMarker)){
                                        arg0.setSnippet("Display on the event List?");
                                        arg0.showInfoWindow();
                                }                       
                        }
                });
                */
        
        
        
        //Do not add it in the onCreate method!
        testMarker = localenderMap.addMarker(new MarkerOptions()
        .position(new LatLng(22.3375, 114.2630))
        .title("COMP3111H Lecture").snippet("Today\n15:00 - 16:30\nLT-E").draggable(true));
        
    }    
}
