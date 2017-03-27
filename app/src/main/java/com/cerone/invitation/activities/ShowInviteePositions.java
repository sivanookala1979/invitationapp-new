package com.cerone.invitation.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.test.mock.MockPackageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.MyMarker;
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
import com.squareup.picasso.Picasso;

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
                //mMyMarkersArray = locations.checkInviteesLocation(eventDetails.getEventId());
                MyMarker m1 = new MyMarker("Adarsh", "9949257729", "http://invtapp.cerone-software.com//assets/image/99/original/bangalore.jpg?1489729555", 14.9132, 79.9930, 5);
                MyMarker m2 = new MyMarker("Chakri", "9000451544", "http://invtapp.cerone-software.com//assets/image/98/original/hyderabad.jpg?1489729540", 17.3850, 78.4867, 10);
                MyMarker m3 = new MyMarker("Srikanth", "8008446701", "http://invtapp.cerone-software.com//assets/image/102/original/kolkata.jpg?1489746712", 13.0827, 80.2707, 15);
                MyMarker m4 = new MyMarker("Hareesh", "9290004545", "http://invtapp.cerone-software.com//assets/image/100/original/mumbai.jpg?1489729570", 10.8505, 76.2711, 20);
                mMyMarkersArray.add(m1);
                mMyMarkersArray.add(m2);
                mMyMarkersArray.add(m3);
                mMyMarkersArray.add(m4);
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
        Log.d("size", markers.size()+"");
        if (markers.size() > 0) {
            for (MyMarker myMarker : markers) {
                final View customMarker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.custom_marker, null);
                final ImageView pickerImage = (ImageView) customMarker.findViewById(R.id.pickerImage);
                Picasso.with(getApplicationContext()).load(myMarker.getImage()).into(pickerImage);
                LatLng latLng = new LatLng(myMarker.getLatitude(), myMarker.getLongitude());
                MarkerOptions markerOption = new MarkerOptions().position(latLng);
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(ShowInviteePositions.createDrawableFromView(this, customMarker)));
                Marker currentMarker = googleMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
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
            final View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.infowindow_layout, null);
            MyMarker myMarker = mMarkersHashMap.get(marker);
            TextView distance = (TextView) v.findViewById(R.id.distance);
            distance.setText(myMarker.getDistance()+"KM");
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

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewPager.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        view.draw(canvas);
        return bitmap;
    }

}