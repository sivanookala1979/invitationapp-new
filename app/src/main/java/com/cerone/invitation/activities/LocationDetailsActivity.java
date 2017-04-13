package com.cerone.invitation.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.GMapV2Direction;
import com.cerone.invitation.helpers.GPSTracker;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.utills.HTTPUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationDetailsActivity extends BaseActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    Document doc;
    GMapV2Direction googleMapV2Direction;
    LatLng sourcePosition, destPosition;
    Polyline polylin;
    GPSTracker gpsTracker;
    private static final int REQUEST_CODE_PERMISSION = 2;
    Event eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        addToolbarView();

        eventDetails = InvtAppPreferences.getEventDetails();
        googleMapV2Direction = new GMapV2Direction();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }else {
                drawPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void drawPath() {
        gpsTracker = new GPSTracker(getApplicationContext());
        if(eventDetails!=null) {
            if ((gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0)) {
                sourcePosition = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                destPosition = new LatLng(eventDetails.getLatitude(), eventDetails.getLongitude());
                drawMarker(sourcePosition, false);
                drawMarker(destPosition, true);
                InvtAppAsyncTask task = new InvtAppAsyncTask(LocationDetailsActivity.this) {

                    @Override
                    public void process() {
                        doc = googleMapV2Direction.getDocument(sourcePosition,
                                destPosition, GMapV2Direction.MODE_DRIVING);
                    }

                    @Override
                    public void afterPostExecute() {
                        if (polylin != null) {
                            polylin.remove();
                        }
                        if (doc != null) {
                            ArrayList<LatLng> directionPoint = googleMapV2Direction
                                    .getDirection(doc);
                            PolylineOptions rectLine = new PolylineOptions().width(3)
                                    .color(Color.RED);
                            for (int i = 0; i < directionPoint.size(); i++) {
                                rectLine.add(directionPoint.get(i));
                            }
                            polylin = map.addPolyline(rectLine);
                            polylin.setColor(Color.parseColor("#f06824"));
                            polylin.setWidth(5f);
                            map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                @Override
                                public void onMapLoaded() {
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(sourcePosition);
                                    builder.include(destPosition);
                                    LatLngBounds bounds = builder.build();
                                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
                                }
                            });
                        }
                    }
                };
                task.setShowProgress(false);
                task.execute();
            } else {
                if(eventDetails.getLatitude()>0 && eventDetails.getLongitude()>0) {
                    destPosition = new LatLng(eventDetails.getLatitude(), eventDetails.getLongitude());
                    drawMarker(destPosition, true);
                }else {
                    ToastHelper.redToast(getApplicationContext(), "Failed to get location details.");
                }
            }
        }
        }

    private void drawMarker(LatLng point, boolean isDestination) {

        final View customMarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker, null);
        final ImageView pickerImage = (ImageView) customMarker.findViewById(R.id.pickerImage);
        Picasso.with(getApplicationContext())
                .load(eventDetails.getImageUrl()).into(new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {

            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap,
                                       Picasso.LoadedFrom arg1) {
                Log.d("DEBUGMAPS", "on Success Start");

                try {
                    Thread.sleep(100);
                    Bitmap roundedCornerBitmap = HTTPUtils.getRoundedCornerBitmap(bitmap, 96);
                    pickerImage.setImageBitmap(roundedCornerBitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                // TODO Auto-generated method stub

            }
        });
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        // Adding marker on the Google Map
        if (isDestination) {
            BitmapDescriptor fromResource = BitmapDescriptorFactory.fromResource(R.drawable.location_marker_default);
            map.addMarker(markerOptions.icon(fromResource).title(eventDetails.getAddress())).setVisible(true);
        }
        else{
            map.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ShowInviteePositions.createDrawableFromView(this, customMarker))).title(getAddress(gpsTracker.getLatitude(), gpsTracker.getLongitude()))).setVisible(true);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED ) {
                drawPath();
            }
            else{

            }
        }

    }

    public String getAddress(double latitude, double longitude){

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();

        return city+", "+state+", "+zip+".";
    }

}
