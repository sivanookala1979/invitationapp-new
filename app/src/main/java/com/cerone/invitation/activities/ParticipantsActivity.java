/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.ProfileInfo;
import com.cerone.invitation.R;
import com.cerone.invitation.activities.chat.IntraChatActivity;
import com.cerone.invitation.adapter.ParticipantsAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.Invitee;
import com.example.dataobjects.ServerResponse;
import com.example.syncher.GroupSyncher;
import com.example.syncher.InvitationSyncher;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, Jan 2, 2016
 */
public class ParticipantsActivity extends BaseActivity implements OnClickListener,AdapterView.OnItemClickListener {

    ListView listView;
    ParticipantsAdapter adapter;
    List<Invitee> participantsList = new ArrayList<Invitee>();
    int groupId, eventId;
    String title;
    boolean isAdmin;
    boolean allInvitees;
    ServerResponse serverResponse;
    Event selectedEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);
        addToolbarView();
        title = getIntent().getExtras().getString("title");
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(this.title+"");
        ImageView image = (ImageView) findViewById(R.id.toolbarEvent);
        image.setVisibility(View.INVISIBLE);
        listView = (ListView) findViewById(R.id.events_list);
        listView.setEmptyView(findViewById( R.id.empty_list_view));
        listView.setOnItemClickListener(this);
        groupId = getIntent().getExtras().getInt("groupId");
        eventId = getIntent().getExtras().getInt("eventId");
        allInvitees = getIntent().getExtras().getBoolean("allInvitees");
        selectedEvent = InvtAppPreferences.getEventDetails();
        if(selectedEvent!=null && eventId==selectedEvent.getEventId()){
            if(selectedEvent.isAccepted()||(selectedEvent.getOwnerId()==InvtAppPreferences.getOwnerId())) {
                isAdmin = selectedEvent.isAdmin();
            }
        }
        getParticipants();
    }

    private void getParticipants() {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                if(groupId > 0){
                    GroupSyncher groupSyncher = new GroupSyncher();
                    participantsList = groupSyncher.getGroupMembers(groupId);
                }else {
                    if(allInvitees){
                        InvitationSyncher syncher = new InvitationSyncher();
                        participantsList = syncher.getInvitees(eventId, "");
                    }else{
                        participantsList = InvtAppPreferences.getInvitees();
                    }
                }
            }

            @Override
            public void afterPostExecute() {
                if (participantsList.size() > 0) {
                    adapter = new ParticipantsAdapter(getApplicationContext(), R.layout.participants_item, participantsList, true);
                    listView.setAdapter(adapter);
                }
            }
        }.execute();
    }


    private void makeInviteeAdmin(final int eventId, final int userId) {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                if(eventId > 0 & userId > 0){
                    InvitationSyncher invitationSyncher = new InvitationSyncher();
                    serverResponse = invitationSyncher.makeInviteeAdmin(eventId, userId);
                }else{
                    ToastHelper.blueToast(getApplicationContext(), "No inputs");
                }
            }

            @Override
            public void afterPostExecute() {
                if (serverResponse!=null) {
                    ToastHelper.blueToast(getApplicationContext(), serverResponse.getStatus());
                }
            }
        }.execute();
    }

    private void blockInvitee(final int eventId, final int userId) {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                if(eventId > 0 & userId > 0){
                    InvitationSyncher invitationSyncher = new InvitationSyncher();
                    serverResponse = invitationSyncher.blockInvitee(eventId, userId);
                }else{
                    ToastHelper.blueToast(getApplicationContext(), "No inputs");
                }
            }

            @Override
            public void afterPostExecute() {
                if (serverResponse!=null) {
                    ToastHelper.blueToast(getApplicationContext(), serverResponse.getStatus());
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerBack :
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;
        Invitee invitee = participantsList.get(i);
        switch (view.getId()) {
            case R.id.call:
                callToUser(invitee.getMobileNumber());
                break;
            case R.id.chat:
                intent = new Intent(ParticipantsActivity.this, IntraChatActivity.class);
                intent.putExtra("UserId", invitee.getInviteeId());
                intent.putExtra("UserId", invitee.getInviteeId());
                intent.putExtra("UserImage", "");
                intent.putExtra("UserName", invitee.getInviteeName());
                startActivity(intent);
                break;
            case R.id.admin:
                if(isAdmin&!invitee.isAdmin()){
                    makeInviteeAdmin(eventId, invitee.getInviteeId());
                    invitee.setAdmin(true);
                    participantsList.set(i, invitee);
                    adapter.updateAdapter(participantsList);
                }
                break;
            case R.id.block:
                if(isAdmin&!invitee.isBlocked()) {
                    blockInvitee(eventId, invitee.getInviteeId());
                    invitee.setBlocked(true);
                    participantsList.set(i, invitee);
                    adapter.updateAdapter(participantsList);
                }
                break;
            default:
                intent = new Intent(ParticipantsActivity.this, ProfileInfo.class);
                intent.putExtra("UserName", invitee.getInviteeName());
                intent.putExtra("UserMobile", invitee.getMobileNumber());
                intent.putExtra("UserImage", invitee.getImage());
                intent.putExtra("UserEmail", invitee.getEmail());
                startActivity(intent);
                break;

        }
    }
    public void callToUser(String mobileNumber) {
        try {
            Intent my_callIntent = new Intent(Intent.ACTION_CALL);
            my_callIntent.setData(Uri.parse("tel:" + mobileNumber));
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(my_callIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
