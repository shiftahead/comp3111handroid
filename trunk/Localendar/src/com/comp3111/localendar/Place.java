package com.comp3111.localendar;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;

public class Place {
	private final String name;
	private final double latitude;
	private final double longitude;
	
	public Place(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getName() {
		return name;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	//Input a string and get the place's Latitude and Longitude
    public static Place getPlaceFromAddress(String addressName) {
    	Geocoder geoCoder = new Geocoder(Localendar.instance);
    	Place myPlace = null;
    	
    	try {
            List<Address> addresses = geoCoder.getFromLocationName(addressName, 1); 
            if (addresses.size() >  0) {
            	double addressLatitude = addresses.get(0).getLatitude(); 
            	double addressLongitude = addresses.get(0).getLongitude();
            	myPlace = new Place(addressName, addressLatitude, addressLongitude);
            }

        } catch (IOException e) { 
        e.printStackTrace(); }
    	
    	return myPlace;
    }
}
