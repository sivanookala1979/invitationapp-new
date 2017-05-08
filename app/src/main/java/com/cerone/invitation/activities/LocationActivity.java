package com.cerone.invitation.activities;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.GPSTracker;
import com.cerone.invitation.helpers.InstantAutoComplete;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.UserLocation;
import com.example.syncher.LocationSyncher;
import com.example.utills.StringUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class LocationActivity extends BaseActivity implements OnClickListener,OnMapReadyCallback {

    GoogleMap googleMap;
    Button doneButton;
    TextView addressText;
    InstantAutoComplete locationFilter;
    List<UserLocation> locationSHistory = new ArrayList<UserLocation>();
    List<String> oldLocations = new ArrayList<String>();
    List<String> locaionInfo = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;
    UserLocation location;
    private static final int REQUEST_CODE_PERMISSION = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_layout);
        addToolbarView();
        setFontType(R.id.done, R.id.locationSearch);
        doneButton = (Button) findViewById(R.id.done);
        addressText = (TextView) findViewById(R.id.addressDetails);
        locationFilter = (InstantAutoComplete) findViewById(R.id.locationSearch);
        locationFilter.setVisibility(View.VISIBLE);
        addressText.setVisibility(View.VISIBLE);
        doneButton.setOnClickListener(this);
        locationSHistory = InvtAppPreferences.getRecentAddressDetails();
        //Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
//        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else { // Google Play Services are available
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.invitee_map);
            fm.getMapAsync(this);
        }

        locationFilterSetUp();
        checkUserPermissions();
    }

    public UserLocation getHistoryAddres(String filterData) {
        UserLocation mylocation = null;
        for (UserLocation iterable_element : locationSHistory) {
            if (iterable_element.getAddress().equalsIgnoreCase(filterData)) {
                mylocation = iterable_element;
            }
        }
        return mylocation;
    }

    private void locationFilterSetUp() {
        for (UserLocation userLocation : locationSHistory) {
            oldLocations.add(userLocation.getAddress());
        }
        myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view, oldLocations);
        locationFilter.setAdapter(myAdapter);
        locationFilter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new InvtAppAsyncTask(LocationActivity.this) {

                    @Override
                    public void process() {
                        String filterData = locationFilter.getText().toString();
                        UserLocation historyAddres = getHistoryAddres(filterData);
                        if (historyAddres != null) {
                            location = historyAddres;
                        } else {
                            LocationSyncher locationSyncher = new LocationSyncher();
                            location = locationSyncher.getLocationDetailsByName(filterData);
                        }
                    }

                    @Override
                    public void afterPostExecute() {
                        if (location != null) {
                            InvtAppPreferences.setEventLocationDetails(location);
                          //  googleMap.clear();
                          //  LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                          //  drawMarker(point, location.getAddress());
                        }
                    }
                }.execute();
            }
        });
        locationFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                new AsyncTask<String, Void, String>() {

                    String data;

                    @Override
                    protected String doInBackground(String... params) {
                        locaionInfo.clear();
                        data = locationFilter.getText().toString();
                        if (data.length() >= 3) {
                            LocationSyncher locationSyncher = new LocationSyncher();
                            locaionInfo = locationSyncher.getLocations(data);
                        } else {
                            if (data.length() == 0)
                                locaionInfo.addAll(oldLocations);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view, locaionInfo);
                        locationFilter.setAdapter(myAdapter);
                    };
                }.execute();
            }
        });
    }

    private void drawMarker(LatLng point, String title) {
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting latitude and longitude for the marker
        markerOptions.position(point).title(title);
        // Adding marker on the Google Map
        addressText.setText(title);
        googleMap.addMarker(markerOptions).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done :
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                location = new UserLocation();
                /** Storing the zoom level to the shared preferences */
                InvtAppPreferences.setZoomPosition(Float.toString(googleMap.getCameraPosition().zoom));
                List<Address> addresses;
                Geocoder geocoder1 = new Geocoder(getBaseContext(), Locale.getDefault());
                String completeAddress = "";
                try {
                    addresses = geocoder1.getFromLocation(point.latitude, point.longitude, 1);
                    if(addresses.size()>0) {
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        completeAddress = StringUtils.getValidString(address) + StringUtils.getValidString(city) + StringUtils.getValidString(state) + StringUtils.getValidString(country) + StringUtils.getValidString(postalCode);
                        Toast.makeText(getBaseContext(), "Address " + completeAddress, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                location.setLatitude(point.latitude);
                location.setLongitude(point.longitude);
                location.setAddress(completeAddress);
                InvtAppPreferences.setEventLocationDetails(location);
                googleMap.clear();
                drawMarker(point, completeAddress);
            }
        });
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }else {
                UserLocation eventLocationDetails = InvtAppPreferences.getEventLocationDetails();
                // If locations are already saved
                if (eventLocationDetails.getAddress() != null && eventLocationDetails.getAddress().length() > 0) {
                    LatLng latLng = new LatLng(eventLocationDetails.getLatitude(), eventLocationDetails.getLongitude());
                    drawMarker(latLng, eventLocationDetails.getAddress());
                } else {
                    GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 10));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}