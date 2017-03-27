package com.cerone.invitation.activities;

import android.os.Bundle;

import com.cerone.invitation.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventsDetailActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap map;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);
        addToolbarView();
        setFontType(R.id.txt_eventDetails, R.id.txt_eventDescription, R.id.txt_date, R.id.txt_time, R.id.event_date, R.id.event_time,
                R.id.event_address, R.id.btn_accept, R.id.btn_maybe, R.id.btn_decline, R.id.btn_block);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        LatLng location = new LatLng(17.4325, 78.4070);
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.locatin_marker_user)).position(location).title("Kavali")).setVisible(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));
    }
}


