package com.hyunsungkr.placesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hyunsungkr.placesapp.model.Place;

public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

         place = (Place) getIntent().getSerializableExtra("place");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Double lat = place.getGeometry().getLocation().getLat();
        Double lng = place.getGeometry().getLocation().getLng();

        LatLng center = new LatLng(lat,lng);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,17));

        googleMap.addMarker(new MarkerOptions().position(center).title(place.getName()));





    }
}