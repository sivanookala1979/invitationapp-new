package com.cerone.invitation.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.*;
import com.example.syncher.LocationSyncher;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShowInviteePositions extends FragmentActivity implements OnClickListener {

    GoogleMap googleMap;
    Button doneButton;
    List<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_layout);
        doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(this);
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else { // Google Play Services are available
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            // Getting GoogleMap object from the fragment
//            googleMap = fm.getMap();
            // Enabling MyLocation Layer of Google Map
//            googleMap.setMyLocationEnabled(true);
            mMarkersHashMap = new HashMap<Marker, MyMarker>();
            // Getting stored zoom level if exists else return 0
            // If locations are already saved
        }
        //TODO NEED TO HANDEL MAP
//        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                marker.showInfoWindow();
//                return true;
//            }
//        });
        loadAndShowLocationData();
    }

    private void loadAndShowLocationData() {
        new InvtAppAsyncTask(this) {

            String response;

            @Override
            public void process() {
                Event eventDetails = InvtAppPreferences.getEventDetails();
                LocationSyncher locations = new LocationSyncher();
                mMyMarkersArray = locations.checkInviteesLocation(eventDetails.getEventId());
            }

            @Override
            public void afterPostExecute() {
                if (mMyMarkersArray.size() > 0) {
                    plotMarkers(mMyMarkersArray);
                } else {
                    ToastHelper.redToast(getApplicationContext(), "Participant locations not found.");
                }
            }
        }.execute();
    }

    private void plotMarkers(List<MyMarker> markers) {
        if (markers.size() > 0) {
            for (MyMarker myMarker : markers) {
                LatLng latLng = new LatLng(myMarker.getLatitude(), myMarker.getLongitude());
                MarkerOptions markerOption = new MarkerOptions().position(latLng);
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon));
                Marker currentMarker = googleMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done :
                finish();
                break;
        }
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
            MyMarker myMarker = mMarkersHashMap.get(marker);
            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);
            TextView markerLabel = (TextView) v.findViewById(R.id.name);
            TextView markernumber = (TextView) v.findViewById(R.id.number);
            markerIcon.setImageResource(R.drawable.new_logo);
            markerLabel.setText(myMarker.getInviteeName());
            markernumber.setText(myMarker.getContactNumber());
            return v;
        }
    }
}