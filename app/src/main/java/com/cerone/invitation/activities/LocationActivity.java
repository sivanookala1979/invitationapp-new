package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InstantAutoComplete;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.UserLocation;
import com.example.syncher.LocationSyncher;

import java.util.ArrayList;
import java.util.List;


public class LocationActivity extends FragmentActivity implements OnClickListener {

    //GoogleMap googleMap;
    Button doneButton;
    TextView addressText;
    InstantAutoComplete locationFilter;
    List<UserLocation> locationSHistory = new ArrayList<UserLocation>();
    List<String> oldLocations = new ArrayList<String>();
    List<String> locaionInfo = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;
    boolean loadCoordinates;
    UserLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map_layout);
        doneButton = (Button) findViewById(R.id.done);
        addressText = (TextView) findViewById(R.id.addressDetails);
        locationFilter = (InstantAutoComplete) findViewById(R.id.locationSearch);
        locationFilter.setVisibility(View.VISIBLE);
        addressText.setVisibility(View.VISIBLE);
        doneButton.setOnClickListener(this);
        locationSHistory = InvtAppPreferences.getRecentAddressDetails();
        //Getting Google Play availability status
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
//        // Showing status
//        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
//            int requestCode = 10;
//            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
//            dialog.show();
//        } else { // Google Play Services are available
//            // Getting reference to the SupportMapFragment of activity_main.xml
//            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//            // Getting GoogleMap object from the fragment
//            googleMap = fm.getMap();
//            // Enabling MyLocation Layer of Google Map
//            googleMap.setMyLocationEnabled(true);
//            // Getting stored zoom level if exists else return 0
//            String zoom = InvtAppPreferences.getZoomPosition();
//            UserLocation eventLocationDetails = InvtAppPreferences.getEventLocationDetails();
//            // If locations are already saved
//            if (eventLocationDetails.getAddress() != null && eventLocationDetails.getAddress().length() > 0) {
//                LatLng latLng = new LatLng(eventLocationDetails.getLatitude(), eventLocationDetails.getLongitude());
//                drawMarker(latLng, eventLocationDetails.getAddress());
//            } else {
//                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 10));
//            }
//        }
//        googleMap.setOnMapClickListener(new OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng point) {
//                location = new UserLocation();
//                /** Storing the zoom level to the shared preferences */
//                InvtAppPreferences.setZoomPosition(Float.toString(googleMap.getCameraPosition().zoom));
//                List<Address> addresses;
//                Geocoder geocoder1 = new Geocoder(getBaseContext(), Locale.getDefault());
//                String completeAddress = "";
//                try {
//                    addresses = geocoder1.getFromLocation(point.latitude, point.longitude, 1);
//                    String address = addresses.get(0).getAddressLine(0);
//                    String city = addresses.get(0).getLocality();
//                    String state = addresses.get(0).getAdminArea();
//                    String country = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
//                    completeAddress = StringUtils.getValidString(address) + StringUtils.getValidString(city) + StringUtils.getValidString(state) + StringUtils.getValidString(country) + StringUtils.getValidString(postalCode);
//                    Toast.makeText(getBaseContext(), "Address " + completeAddress, Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                location.setLatitude(point.latitude);
//                location.setLongitude(point.longitude);
//                location.setAddress(completeAddress);
//                InvtAppPreferences.setEventLocationDetails(location);
//                googleMap.clear();
//                drawMarker(point, completeAddress);
//            }
//        });
//        locationFilterSetUp();
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
//        locationFilter.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                new AsyncTask<String, Void, String>() {
//
//                    String data;
//
//                    @Override
//                    protected String doInBackground(String... params) {
//                        locaionInfo.clear();
//                        data = locationFilter.getText().toString();
//                        if (data.length() >= 3) {
//                            LocationSyncher locationSyncher = new LocationSyncher();
//                            locaionInfo = locationSyncher.getLocations(data);
//                        } else {
//                            if (data.length() == 0)
//                                locaionInfo.addAll(oldLocations);
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(String result) {
//                        myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_view, locaionInfo);
//                        locationFilter.setAdapter(myAdapter);
//                    };
//                }.execute();
//            }
//        });
    }

//    private void drawMarker(LatLng point, String title) {
//        // Creating an instance of MarkerOptions
//        MarkerOptions markerOptions = new MarkerOptions();
//        // Setting latitude and longitude for the marker
//        markerOptions.position(point).title(title);
//        // Adding marker on the Google Map
//        addressText.setText(title);
//        googleMap.addMarker(markerOptions).showInfoWindow();
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
//        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
//    }

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
}