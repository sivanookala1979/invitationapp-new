/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.cerone.invitation.helpers.GPSTracker;
import com.cerone.invitation.helpers.InvtAppPreferences;

import com.example.dataobjects.*;
import com.example.syncher.BaseSyncher;
import com.example.syncher.LocationSyncher;
import com.example.utills.StringUtils;


/**
 * @author adarsh
 * @version 1.0, Dec 29, 2015
 */
public class MyService extends BroadcastReceiver {

    public static int totalCount = 0;
    GPSTracker gpsTracker;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        InvtAppPreferences.setPref(context);
        ServiceInformation serviceInfo = InvtAppPreferences.getServiceDetails();
        Log.d("Data", "Service activated " + StringUtils.getCurrentDate() + " " + action + " service start time " + serviceInfo.getEventStartTime() + " end time " + serviceInfo.getEnventEndTime());
        if (!StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(action)) {//SOMEACTION.equals(action)
            Log.d("Data", "Some action is mached " + totalCount);
            if (serviceInfo.getServieEndTime() != null) {
                if (StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(serviceInfo.getServieEndTime())) {
                    Log.d("Data", "Data received count " + totalCount);
                    gpsTracker = new GPSTracker(context);
                    if (true || (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0)) {
                        new AsyncTask<String, Void, String>() {

                            @Override
                            protected String doInBackground(String... params) {
                                LocationSyncher locationSyncher = new LocationSyncher();
                                UserLocation userLocation = new UserLocation();
                                userLocation.setLatitude(gpsTracker.getLatitude());
                                userLocation.setLongitude(gpsTracker.getLongitude());
                                userLocation.setDateTime(StringUtils.getCurrentDate());
                                BaseSyncher.setAccessToken(InvtAppPreferences.getAccessToken());
                                Log.d("Data", "locations count " + userLocation.getLatitude() + " " + userLocation.getLongitude() + " " + userLocation.getDateTime());
                                ServerResponse locations = locationSyncher.postUserLocation(userLocation);
                                Log.d("Data", "locations count " + locations.getStatus());
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String result) {
                                totalCount++;
                                Log.d("Data_notification", "Notification service before");
                                ServiceInformation service = InvtAppPreferences.getServiceDetails();
                                if (!service.isShowNotification() && !StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(service.getCheckInNotificationServiceStartTime())) {
                                    Log.d("Data_notification", "Notification service after");
                                    //TODO NEED TO CHECK AND UNCOMMENT THE CODE
//                                    Intent myIntent = new Intent(context, NotificationService.class);
//                                    myIntent.setAction(service.getCheckInNotificationServiceStartTime());
//                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                                    alarmManager.cancel(pendingIntent);
//                                    service.setShowNotification(true);
//                                    InvtAppPreferences.setServiceDetails(service);
//                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);
                                }
                            }
                        }.execute();
                    } else {
                        Log.d("Data", "Invalid location data :(");
                    }
                } else {
                    Log.d("Data", "you are done :)");
                    totalCount = 0;
                    CancelAlarm(context);
                }
            } else {
                Log.d("Data", "Invalid date found :(");
            }
        } else {
            Log.d("Data", "Action not matched :(");
        }
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, MyService.class);
        intent.setAction(InvtAppPreferences.getServiceDetails().getServiceStartTime());
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}