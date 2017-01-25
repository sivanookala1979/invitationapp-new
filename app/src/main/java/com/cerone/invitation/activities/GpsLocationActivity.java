/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.cerone.invitation.R;
import com.cerone.invitation.helpers.GPSTracker;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.*;
import com.example.syncher.LocationSyncher;
import com.example.utills.StringUtils;


/**
 * @author CeroneFedora
 * @version 1.0, Dec 25, 2015
 */
public class GpsLocationActivity extends BaseActivity implements OnClickListener {

    TextView latitude, longitude;
    Button postLocation;
    GPSTracker gpsTracker;
    ImageButton headerBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        gpsTracker = new GPSTracker(GpsLocationActivity.this);
        latitude.setText(gpsTracker.getLatitude() + "");
        longitude.setText(gpsTracker.getLongitude() + "");
        postLocation = (Button) findViewById(R.id.postLocationButton);
        postLocation.setOnClickListener(this);
        headerBack = (ImageButton) findViewById(R.id.headerBack);
        headerBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postLocationButton :
                if ((gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0)) {
                    final UserLocation userLocation = new UserLocation();
                    if (latitude.getText() != null && longitude.getText() != null) {
                        userLocation.setLatitude(Double.parseDouble((String) latitude.getText()));
                        userLocation.setLongitude(Double.parseDouble((String) longitude.getText()));
                    }
                    userLocation.setDateTime(StringUtils.getCurrentDate());
                    new InvtAppAsyncTask(this) {

                        ServerResponse serverResponse;

                        @Override
                        public void process() {
                            LocationSyncher locationSyncher = new LocationSyncher();
                            serverResponse = locationSyncher.postUserLocation(userLocation);
                        }

                        @Override
                        public void afterPostExecute() {
                            if (serverResponse.getStatus().equals("Success"))
                                Toast.makeText(getApplicationContext(), serverResponse.getStatus(), Toast.LENGTH_SHORT).show();
                            else
                                ToastHelper.redToast(getApplicationContext(), serverResponse.getStatus());
                            finish();
                        }
                    }.execute();
                } else {
                    ToastHelper.redToast(getApplicationContext(), "Invalid coordinates try again.");
                }
                break;
            case R.id.headerBack :
                finish();
                break;
        }
    }
}
