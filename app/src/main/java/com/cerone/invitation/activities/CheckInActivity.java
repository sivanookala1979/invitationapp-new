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
import com.cerone.invitation.service.NotificationService;
import com.example.dataobjects.*;
import com.example.syncher.BaseSyncher;
import com.example.syncher.UserSyncher;
import com.example.utills.InvitationAppConstants;
import com.example.utills.StringUtils;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in_layout);
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
        mediaPlayer = MediaPlayer.create(this, R.drawable.invitation);
        mediaPlayer.start();
        v.vibrate(10000);
    }

    private void updateData() {
        Event eventInfo = InvtAppPreferences.getServiceDetails().getEventInfo();
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
        ServiceInformation serviceDetails = InvtAppPreferences.getServiceDetails();
        serviceDetails.setCheckInNotificationServiceStartTime(StringUtils.getNewDate(serviceDetails.getCheckInNotificationServiceStartTime(), 10));
        Intent myIntent = new Intent(getApplicationContext(), NotificationService.class);
        myIntent.setAction(serviceDetails.getCheckInNotificationServiceStartTime());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        InvtAppPreferences.setServiceDetails(serviceDetails);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (60000 * 3), pendingIntent);
        mediaPlayer.stop();
        finish();
    }

    private void updateEventInfo(final String status) {
        new InvtAppAsyncTask(CheckInActivity.this) {

            ServerResponse updateInviteeCheckInStaus;

            @Override
            public void process() {
                Event eventInfo = InvtAppPreferences.getServiceDetails().getEventInfo();
                UserSyncher userSyncher = new UserSyncher();
                updateInviteeCheckInStaus = userSyncher.updateInviteeCheckInStaus(eventInfo.getEventId(), status);
            }

            @Override
            public void afterPostExecute() {
                ToastHelper.blueToast(getApplicationContext(), updateInviteeCheckInStaus.getStatus());
                MyService service = new MyService();
                service.CancelAlarm(getApplicationContext());
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
