package com.cerone.invitation.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cerone.invitation.R.id.map;


public class ShowInviteePositions extends BaseActivity implements OnClickListener,OnMapReadyCallback {

    GoogleMap googleMap;
    Button doneButton;
    List<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;
    private static final int REQUEST_CODE_PERMISSION = 2;
    Event eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_layout);
        addToolbarView();
        doneButton = (Button) findViewById(R.id.done);
        TextView headerTitle = (TextView)findViewById(R.id.toolbar_title);
        headerTitle.setText("Participants Locations");
        findViewById(R.id.selectionDetailsLayout).setVisibility(View.GONE);
        doneButton.setOnClickListener(this);
        eventDetails = InvtAppPreferences.getEventDetails();

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
            fm.getMapAsync(this);
            mMarkersHashMap = new HashMap<Marker, MyMarker>();
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
    }

    private void loadAndShowLocationData() {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                LocationSyncher locations = new LocationSyncher();
                mMyMarkersArray = locations.checkInviteesLocation(eventDetails.getEventId());
            }

            @Override
            public void afterPostExecute() {
                plotMarkers();
            }
        }.execute();
    }

    private void plotMarkers() {
        if(googleMap != null) {
            if (mMyMarkersArray.size() > 0) {
                plotMarkers(mMyMarkersArray);
            } else {
                ToastHelper.redToast(getApplicationContext(), "Participant locations not found.");
            }
        }
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
            TextView time = (TextView) v.findViewById(R.id.time);
            Log.d("Location","Marker info"+myMarker.getDateTime());
            markerIcon.setImageResource(R.drawable.new_logo);
            markerLabel.setText(myMarker.getInviteeName());
            markernumber.setText(myMarker.getContactNumber());
            return v;
        }
    }
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        loadAndShowLocationData();
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}