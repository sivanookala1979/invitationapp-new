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

import com.cerone.invitation.helpers.GPSTracker;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.Event;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.ServiceInformation;
import com.example.syncher.UserSyncher;
import com.example.utills.StringUtils;

import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, 02-Feb-2016
 */
public class NotificationService extends BroadcastReceiver {

    public static int totalCount = 0;
    GPSTracker gpsTracker;
    Event eventInfo;
    List<ServiceInformation> serviceInformationList;


    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("Data_notification", "Notification service");
        final int flag = intent.getExtras().getInt("flag");
        InvtAppPreferences.setPref(context);
        serviceInformationList = InvtAppPreferences.getServiceDetails();
        if(serviceInformationList.size()>0) {
            final ServiceInformation serviceDetails = serviceInformationList.get(flag);
            eventInfo = serviceDetails.getEventInfo();
            if (!StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(serviceDetails.getEventStartTime())) {
                Log.d("Service", "End time" + serviceDetails.getServieEndTime());
                if (StringUtils.isGivenDateGreaterThanOrEqualToCurrentDate(serviceDetails.getServieEndTime())) {
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
                            if ((distanceFromEvent != null && distanceFromEvent.getDistance() != null) || (eventInfo.getLatitude() == 0.0 && eventInfo.getLongitude() == 0.0)) {
                                String distance = distanceFromEvent.getDistance();
                                double distanceInfo = Double.parseDouble(distance);
                                if ((distanceInfo < 1) || (eventInfo.getLatitude() == 0.0 && eventInfo.getLongitude() == 0.0)) {
//                            if (eventInfo.isManualCheckIn()) {
//                                Intent newIntent = new Intent(context, CheckInActivity.class);
//                                newIntent.putExtra("flag",flag);
//                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(newIntent);
//                            }
                                    CancelAlarm(context, flag);
                                    MyService myService = new MyService();
                                    myService.CancelAlarm(context, flag);
                                }
                            }
                        }
                    }.execute();
                } else {
                    CancelAlarm(context, flag);
                    MyService myService = new MyService();
                    myService.CancelAlarm(context, flag);
                }
            }
        }
    }

    public void CancelAlarm(Context context, int flag) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(InvtAppPreferences.getServiceDetails().get(flag).getCheckInNotificationServiceStartTime());
        PendingIntent sender = PendingIntent.getBroadcast(context, flag, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.d("Data_notification", "Notification service closed");
    }
}
