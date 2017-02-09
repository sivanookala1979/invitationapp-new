/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.cerone.invitation.service.MyService;
import com.cerone.invitation.service.NotificationService;
import com.example.dataobjects.*;
import com.example.syncher.BaseSyncher;
import com.example.syncher.UserSyncher;
import com.example.utills.InvitationAppConstants;
import com.example.utills.StringUtils;

import java.util.Calendar;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, 02-Feb-2016
 */
public class CheckInActivity extends BaseActivity implements OnClickListener {

    TextView eventName, eventDate, eventdescription, eventAddress, participantsInfo;
    Button checkIn, snooze, notGoing;
    Event eventInfo;
    MediaPlayer mediaPlayer;
    Vibrator v;
    int eventFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in_layout);
        eventFlag = getIntent().getExtras().getInt("flag");
        InvtAppPreferences.setPref(getApplicationContext());
        BaseSyncher.setAccessToken(InvtAppPreferences.getAccessToken());
        eventName = (TextView) findViewById(R.id.eventName);
        eventDate = (TextView) findViewById(R.id.startDate);
        eventdescription = (TextView) findViewById(R.id.eventDescription);
        eventAddress = (TextView) findViewById(R.id.eventAddress);
        participantsInfo = (TextView) findViewById(R.id.participants);
        checkIn = (Button) findViewById(R.id.checkIn);
        snooze = (Button) findViewById(R.id.snoozeNotification);
        notGoing = (Button) findViewById(R.id.notGoing);
        checkIn.setOnClickListener(this);
        snooze.setOnClickListener(this);
        notGoing.setOnClickListener(this);
        updateData();
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.invitation);
        mediaPlayer.start();
        v.vibrate(10000);
    }

    private void updateData() {
        Event eventInfo = InvtAppPreferences.getServiceDetails().get(eventFlag).getEventInfo();
        eventName.setText("Event Name : " + eventInfo.getName());
        eventDate.setText("Start Date : " + eventInfo.getStartDateTime());
        eventdescription.setText("Description : " + eventInfo.getDescription());
        eventAddress.setText("Address : " + eventInfo.getAddress());
        participantsInfo.setText("Accept: " + eventInfo.getAcceptedCount() + "  Reject : " + eventInfo.getRejectedCount() + " Total: " + eventInfo.getInviteesCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkIn :
                updateEventInfo(InvitationAppConstants.CHECK_IN);
                break;
            case R.id.snoozeNotification :
                snoozeTheNotification();
                break;
            case R.id.notGoing :
                updateEventInfo(InvitationAppConstants.NOT_GOING);
                break;
        }
    }

    private void snoozeTheNotification() {
        ServiceInformation serviceDetails = InvtAppPreferences.getServiceDetails().get(eventFlag);
        serviceDetails.setCheckInNotificationServiceStartTime(StringUtils.getNewDate(serviceDetails.getCheckInNotificationServiceStartTime(), 10));
        Intent myIntent = new Intent(getApplicationContext(), NotificationService.class);
        myIntent.putExtra("flag", eventFlag);
        myIntent.setAction(serviceDetails.getCheckInNotificationServiceStartTime());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), eventFlag, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Calendar cal = Calendar.getInstance();
        cal.setTime(StringUtils.StringToDate(serviceDetails.getCheckInNotificationServiceStartTime()));
        //SAVE UPDATED TIME
        List<ServiceInformation> serviceDetailsList = InvtAppPreferences.getServiceDetails();
        serviceDetailsList.get(eventFlag).setCheckInNotificationServiceStartTime(serviceDetails.getCheckInNotificationServiceStartTime());
        InvtAppPreferences.setServiceDetails(serviceDetailsList);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (60000 * 3), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60000, pendingIntent);
        mediaPlayer.stop();
        finish();
    }

    private void updateEventInfo(final String status) {
        new InvtAppAsyncTask(CheckInActivity.this) {

            ServerResponse updateInviteeCheckInStaus;
            Event eventInfo;
            @Override
            public void process() {
                Event eventInfo = InvtAppPreferences.getServiceDetails().get(eventFlag).getEventInfo();
                UserSyncher userSyncher = new UserSyncher();
                updateInviteeCheckInStaus = userSyncher.updateInviteeCheckInStaus(eventInfo.getEventId(), status);
            }

            @Override
            public void afterPostExecute() {
                ToastHelper.blueToast(getApplicationContext(), updateInviteeCheckInStaus.getStatus());
                MyService service = new MyService();
                service.CancelAlarm(getApplicationContext(),eventFlag);
                NotificationService notificationService = new NotificationService();
                notificationService.CancelAlarm(getApplicationContext(),eventFlag);
                mediaPlayer.stop();
                Log.d("Data", "Main Service closed");
                finish();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        snoozeTheNotification();
        mediaPlayer.stop();
        finish();
    }
}
