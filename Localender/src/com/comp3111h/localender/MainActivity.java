package com.comp3111h.localender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

	
	private GoogleMap localenderMap;
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localenderMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        localenderMap.setPadding(0, 0, 0, 100);
        
        //Add a marker for testing, do not add it in the onCreate method!
        localenderMap.addMarker(new MarkerOptions()
        .position(new LatLng(0, 0))
        .title("Hello world"));            
    }
    
    
}
