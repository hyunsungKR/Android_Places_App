package com.hyunsungkr.placesapp.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Geometry implements Serializable {
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
