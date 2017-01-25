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

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.*;
import com.example.syncher.InvitationSyncher;
import com.example.utills.StringUtils;


/**
 * @author peekay
 * @version 1.0, 02-Mar-2016
 */
public class MyInvitationActivity extends BaseActivity implements OnClickListener {

    Event eventDetails;
    LinearLayout participantsLayout, location, invitationSelection;
    Button accept, maybe, reject, invitees, cancel;
    Invitation invitations = new Invitation();
    TextView acceptCount, rejectCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_information_layout);
        invitations = InvtAppPreferences.getInvitation();
        participantsLayout = (LinearLayout) findViewById(R.id.participantsLayout);
        location = (LinearLayout) findViewById(R.id.gpsLocationLayout);
        location.setVisibility(View.VISIBLE);
        participantsLayout.setVisibility(View.GONE);
        invitationSelection = (LinearLayout) findViewById(R.id.invitationSelection);
        invitationSelection.setVisibility(View.VISIBLE);
        findViewById(R.id.invitationShare).setVisibility(View.VISIBLE);
        findViewById(R.id.editEvent).setVisibility(View.GONE);
        findViewById(R.id.shareEvent).setVisibility(View.GONE);
        invitees = (Button) findViewById(R.id.inviteesInfo);
        cancel = (Button) findViewById(R.id.deleteEvent);
        cancel.setText("CANCEL");
        invitees.setVisibility(View.VISIBLE);
        accept = (Button) findViewById(R.id.acceptInvitation);
        maybe = (Button) findViewById(R.id.mayBe);
        reject = (Button) findViewById(R.id.rejected);
        participantsLayout.setOnClickListener(this);
        location.setOnClickListener(this);
        invitees.setOnClickListener(this);
        cancel.setOnClickListener(this);
        accept.setOnClickListener(this);
        maybe.setOnClickListener(this);
        reject.setOnClickListener(this);
        eventDetails = InvtAppPreferences.getEventDetails();
        loadEventData();
    }

    private void loadEventData() {
        TextView eventName = (TextView) findViewById(R.id.headerTitle);
        TextView description = (TextView) findViewById(R.id.description);
        TextView eventStartDate = (TextView) findViewById(R.id.eventStartDate);
        TextView eventEndDate = (TextView) findViewById(R.id.eventEndDate);
        TextView eventStartTime = (TextView) findViewById(R.id.eventStartTime);
        TextView eventEndTime = (TextView) findViewById(R.id.eventEndTime);
        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        TextView participantsInfo = (TextView) findViewById(R.id.participantsInfo);
        acceptCount = (TextView) findViewById(R.id.acceptCount);
        rejectCount = (TextView) findViewById(R.id.rejectCount);
        TextView totalCount = (TextView) findViewById(R.id.totalCount);
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
            case R.id.participants :
            case R.id.participantsLayout :
                intent = new Intent(getApplicationContext(), ParticipantsActivity.class);
                startActivity(intent);
                break;
            case R.id.acceptInvitation :
                acceptOrRejectInvitation(true, eventDetails);
                break;
            case R.id.gpsLocationIcon :
            case R.id.gpsLocationLayout :
                intent = new Intent(getApplicationContext(), ShowInviteePositions.class);
                startActivity(intent);
                break;
            case R.id.mayBe :
                invitationSelection.setVisibility(View.GONE);
                break;
            case R.id.rejected :
                acceptOrRejectInvitation(false, eventDetails);
                break;
            case R.id.inviteesInfo :
                intent = new Intent(getApplicationContext(), InvitieesTabActivity.class);
                startActivity(intent);
                break;
            case R.id.deleteEvent :
            case R.id.headerBack :
                finish();
                break;
        }
    }

    public void acceptOrRejectInvitation(final boolean status, final Event event) {
        final String response = "";
        new InvtAppAsyncTask(this) {

            String response;

            @Override
            public void process() {
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                response = invitationSyncher.getInvitationStatus(event.getEventId(), status);
            }

            @Override
            public void afterPostExecute() {
                findViewById(R.id.invitationSelection).setVisibility(View.VISIBLE);
                if (response != null && !response.equals("Invalid response")) {
                    if (status) {
                        activateService();
                        acceptCount.setText((eventDetails.getAcceptedCount() + 1) + "");
                    } else {
                        rejectCount.setText((eventDetails.getRejectedCount() + 1) + "");
                    }
                    invitationSelection.setVisibility(View.GONE);
                }
                ToastHelper.blueToast(getApplicationContext(), response);
            }
        }.execute();
    }
}
