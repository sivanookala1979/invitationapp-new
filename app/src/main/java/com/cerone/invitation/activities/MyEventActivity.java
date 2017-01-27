/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.*;
import com.example.syncher.EventSyncher;
import com.example.utills.StringUtils;


/**
 * @author Adarsh.T
 * @version 1.0, 25-Feb-2016
 */
public class MyEventActivity extends BaseActivity implements OnClickListener {

    Event eventDetails;
    LinearLayout participantsLayout;
    Button editEvent, shareEvent, deleteEvent;
    TextView totalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_information_layout);
        addToolbarView();
        participantsLayout = (LinearLayout) findViewById(R.id.participantsLayout);
        editEvent = (Button) findViewById(R.id.editEvent);
        shareEvent = (Button) findViewById(R.id.shareEvent);
        deleteEvent = (Button) findViewById(R.id.deleteEvent);
        participantsLayout.setOnClickListener(this);
        editEvent.setOnClickListener(this);
        shareEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
        eventDetails = InvtAppPreferences.getEventDetails();
        loadEventData();
    }

    private void loadEventData() {
        TextView eventName = (TextView) findViewById(R.id.toolbar_title);
        TextView description = (TextView) findViewById(R.id.description);
        TextView eventStartDate = (TextView) findViewById(R.id.eventStartDate);
        TextView eventEndDate = (TextView) findViewById(R.id.eventEndDate);
        TextView eventStartTime = (TextView) findViewById(R.id.eventStartTime);
        TextView eventEndTime = (TextView) findViewById(R.id.eventEndTime);
        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        TextView participantsInfo = (TextView) findViewById(R.id.participantsInfo);
        TextView acceptCount = (TextView) findViewById(R.id.acceptCount);
        TextView rejectCount = (TextView) findViewById(R.id.rejectCount);
        if (!eventDetails.getExtraAddress().isEmpty()) {
            LinearLayout extraAddressLayout = (LinearLayout) findViewById(R.id.extraAddressLayout);
            extraAddressLayout.setVisibility(View.VISIBLE);
            TextView extraAddress = (TextView) findViewById(R.id.extraAddress);
            extraAddress.setText("" + eventDetails.getExtraAddress());
        }
        totalCount = (TextView) findViewById(R.id.totalCount);
        TextView checkedInCount = (TextView) findViewById(R.id.checkedinCount);
        acceptCount.setText("" + eventDetails.getAcceptedCount());
        rejectCount.setText("" + eventDetails.getRejectedCount());
        totalCount.setText("" + eventDetails.getInviteesCount());
        checkedInCount.setText("" + eventDetails.getCheckedInCount());
        eventName.setText(eventDetails.getName());
        description.setText(eventDetails.getDescription());
        eventStartDate.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 1));
        eventEndDate.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 1));
        eventStartTime.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 2));
        eventEndTime.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 2));
        eventLocation.setText(eventDetails.getAddress());
        participantsInfo.setText("invitees ");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.participants:
            case R.id.participantsLayout:
                intent = new Intent(getApplicationContext(), ParticipantsActivity.class);
                startActivity(intent);
                break;
            case R.id.editEvent:
                intent = new Intent(getApplicationContext(), NewEventActivity.class);
                startActivity(intent);
                break;
            case R.id.shareEvent:
                intent = new Intent(getApplicationContext(), ShareEventActivity.class);
                intent.putExtra("newEvent", false);
                startActivity(intent);
                break;
            case R.id.deleteEvent:
                deleteEvent();
                break;
            case R.id.headerBack:
                finish();
                break;
        }
    }

    private void deleteEvent() {
        new InvtAppAsyncTask(this) {

            ServerResponse serverResponse;

            @Override
            public void process() {
                EventSyncher eventSyncher = new EventSyncher();
                serverResponse = eventSyncher.deleteEvent(eventDetails.getEventId());
            }

            @Override
            public void afterPostExecute() {
                Toast.makeText(getApplicationContext(), serverResponse.getStatus(), Toast.LENGTH_LONG).show();
                if (serverResponse.getId()>0) {
                    Intent intent = new Intent(MyEventActivity.this, HomeScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }.execute();
    }
}
