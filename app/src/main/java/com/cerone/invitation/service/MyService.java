/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.service;

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

import java.util.Calendar;
import java.util.List;


/**
 * @author adarsh
 * @version 1.0, Dec 29, 2015
 */
public class MyService extends BroadcastReceiver {

    public static int totalCount = 0;
    GPSTracker gpsTracker;
    List<ServiceInformation> serviceInformationList;



    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        final int flagInfo = intent.getExtras().getInt("flag");
        InvtAppPreferences.setPref(context);
        serviceInformationList = InvtAppPreferences.getServiceDetails();
        if(serviceInformationList.size()>0) {
            final ServiceInformation serviceInfo = serviceInformationList.get(flagInfo);
            Log.d("Data", "flag info " + flagInfo + " Service activated " + StringUtils.getCurrentDate() + " " + action + " service start time " + serviceInfo.getEventStartTime() + " end time " + serviceInfo.getEnventEndTime());
            if (!StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(action)) {//SOMEACTION.equals(action)
                if (serviceInfo.getServieEndTime() != null) {
                    if (StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(serviceInfo.getServieEndTime())) {
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
                                    Log.d("Data_notification", "Notification service before");
                                    ServiceInformation service = serviceInfo;//InvtAppPreferences.getServiceDetails();
                                    if (!service.isShowNotification() && !StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(service.getCheckInNotificationServiceStartTime())) {
                                        Log.d("Data_notification", "Notification service after");
                                        Intent myIntent = new Intent(context, NotificationService.class);
                                        myIntent.putExtra("flag", flagInfo);
                                        myIntent.setAction(service.getCheckInNotificationServiceStartTime());
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                        alarmManager.cancel(pendingIntent);
                                        List<ServiceInformation> serviceDetails = InvtAppPreferences.getServiceDetails();
                                        serviceDetails.get(flagInfo).setShowNotification(true);
                                        InvtAppPreferences.setServiceDetails(serviceDetails);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(StringUtils.StringToDate(service.getCheckInNotificationServiceStartTime()));
                                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60000, pendingIntent);
                                    }
                                }
                            }.execute();
                        } else {
                            Log.d("Data", "Invalid location data :(");
                        }
                    } else {
                        Log.d("Data", "you are done :)");
                        CancelAlarm(context, flagInfo);
                    }
                } else {
                    Log.d("Data", "Invalid date found :(");
                }
            } else {
                Log.d("Data", "Action not matched :(");
            }
        }
    }

    public void CancelAlarm(Context context,int flag) {
        Intent intent = new Intent(context, MyService.class);
        intent.setAction(InvtAppPreferences.getServiceDetails().get(flag).getServiceStartTime());
        PendingIntent sender = PendingIntent.getBroadcast(context, flag, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}