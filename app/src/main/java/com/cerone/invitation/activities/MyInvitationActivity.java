/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.Eventstatistics;
import com.example.dataobjects.Invitation;
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
    TextView acceptCount, rejectCount, totalCount,checkedInCount;
    String locationPermission = "";

    public static final String LOCATION = "LOCATION";
    public static final String DISTANCE = "DISTANCE";
    public static final String NOTHING = "NOTHING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_information_layout);
        addToolbarView();
        setFontType(R.id.description,R.id.eventStartDate,R.id.eventStartTime,R.id.eventEndDate,R.id.eventEndTime,R.id.eventLocation,R.id.participantsInfo,
                R.id.editEvent,R.id.shareEvent,R.id.inviteesInfo,R.id.txt_start,R.id.txt_end, R.id.deleteEvent, R.id.acceptInvitation, R.id.mayBe, R.id.rejected);
        invitations = InvtAppPreferences.getInvitation();
        participantsLayout = (LinearLayout) findViewById(R.id.participantsLayout);
        location = (LinearLayout) findViewById(R.id.gpsLocationLayout);
        participantsLayout.setVisibility(View.GONE);
        location.setVisibility(View.VISIBLE);
        invitationSelection = (LinearLayout) findViewById(R.id.invitationSelection);
        invitationSelection.setVisibility(View.VISIBLE);
        findViewById(R.id.invitationShare).setVisibility(View.VISIBLE);
        findViewById(R.id.editEvent).setVisibility(View.GONE);
        findViewById(R.id.shareEvent).setVisibility(View.GONE);
        invitees = (Button) findViewById(R.id.inviteesInfo);
        cancel = (Button) findViewById(R.id.deleteEvent);
        cancel.setText("Cancel");
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
        Log.d("Accept","Accept status "+eventDetails.isAccepted());
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
        acceptCount = (TextView) findViewById(R.id.acceptCount);
        rejectCount = (TextView) findViewById(R.id.rejectCount);
        totalCount = (TextView) findViewById(R.id.totalCount);
        checkedInCount = (TextView) findViewById(R.id.checkedinCount);
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
        participantsInfo.setText("Invitees ");
        if(eventDetails.isAccepted()){
            invitationSelection.setVisibility(View.GONE);
        }else {
            invitationSelection.setVisibility(View.VISIBLE);
        }
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
                showLocationPermissionDialog();
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
                acceptOrRejectInvitation(false, eventDetails, locationPermission);
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

    public void acceptOrRejectInvitation(final boolean status, final Event event, final String locationPermission) {
        final String response = "";
        new InvtAppAsyncTask(this) {

            Eventstatistics response;

            @Override
            public void process() {
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                response = invitationSyncher.getInvitationStatus(event.getEventId(), status, locationPermission);
            }

            @Override
            public void afterPostExecute() {
                findViewById(R.id.invitationSelection).setVisibility(View.VISIBLE);
                if (response.isValid()) {
                    if (status) {
                        activateService();
                    }
                    rejectCount.setText(response.getRejectCount() + "");
                    acceptCount.setText(response.getAcceptCount() + "");
                    totalCount.setText(response.getAcceptCount() + "");
                    checkedInCount.setText("" + response.getCheckedInCount());
                    invitationSelection.setVisibility(View.GONE);
                }
                ToastHelper.blueToast(getApplicationContext(), response.getMessage());
            }
        }.execute();
    }

    public void showLocationPermissionDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        setFontType(R.id.title_accept, R.id.btn_submit, R.id.btn_cancel, R.id.btn_submit, R.id.radio_location, R.id.radio_distance, R.id.radio_nothing);
        Button accept = (Button) dialog.findViewById(R.id.btn_submit);
        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        cancel.setVisibility(View.GONE);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.invt_RadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId){
                    case R.id.radio_location:
                        locationPermission = LOCATION;
                        Log.d("lacation", locationPermission);
                        break;
                    case R.id.radio_distance:
                        locationPermission = DISTANCE;
                        Log.d("lacation", locationPermission);
                        break;
                    case R.id.radio_nothing:
                        locationPermission = NOTHING;
                        Log.d("lacation", locationPermission);
                        break;
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptOrRejectInvitation(true, eventDetails, locationPermission);
                dialog.cancel();
            }
        });
        dialog.show();

    }
}
