/*
 * (c) Copyright 2001-2016 COMIT AG
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

import com.cerone.invitation.activities.CheckInActivity;
import com.cerone.invitation.helpers.GPSTracker;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.*;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.ServiceInformation;
import com.example.syncher.UserSyncher;
import com.example.utills.StringUtils;


/**
 * @author Adarsh.T
 * @version 1.0, 02-Feb-2016
 */
public class NotificationService extends BroadcastReceiver {

    public static int totalCount = 0;
    GPSTracker gpsTracker;
    Event eventInfo;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("Data_notification", "Notification service");
        InvtAppPreferences.setPref(context);
        ServiceInformation serviceDetails = InvtAppPreferences.getServiceDetails();
        eventInfo = serviceDetails.getEventInfo();
        if (!StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(serviceDetails.getEventStartTime())) {
            new AsyncTask<String, Void, String>() {

                ServerResponse distanceFromEvent;

                @Override
                protected String doInBackground(String... params) {
                    UserSyncher syncher = new UserSyncher();
                    distanceFromEvent = syncher.getDistanceFromEvent(eventInfo.getEventId());
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    if (distanceFromEvent != null && distanceFromEvent.getDistance() != null) {
                        String distance = distanceFromEvent.getDistance();
                        double distanceInfo = Double.parseDouble(distance);
                        if (distanceInfo < 1) {
                            if (eventInfo.isManualCheckIn()) {
                                Intent newIntent = new Intent(context, CheckInActivity.class);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(newIntent);
                            } else {
                                CancelAlarm(context);
                            }
                            CancelAlarm(context);
                        }
                        //                        Intent newIntent = new Intent(context, CheckInActivity.class);
                        //                        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //                        context.startActivity(newIntent);
                        //                        CancelAlarm(context);
                    }
                }
            }.execute();
        }
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(InvtAppPreferences.getServiceDetails().getCheckInNotificationServiceStartTime());
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.d("Data_notification", "Notification service closed");
    }
}