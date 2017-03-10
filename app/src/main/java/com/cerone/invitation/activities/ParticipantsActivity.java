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
import com.example.dataobjects.Event;
import com.example.dataobjects.Invitee;
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
    int groupId;
    String title;

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
        listView.setOnItemClickListener(this);
        groupId = getIntent().getExtras().getInt("groupId");
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
                    Event eventDetails = InvtAppPreferences.getEventDetails();
                    InvitationSyncher invitationSyncher = new InvitationSyncher();
                    participantsList = invitationSyncher.getInvitees(eventDetails.getEventId(), "");
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
            case R.id.call :
                callToUser(invitee.getMobileNumber());
                break;
            case R.id.chat :
                intent = new Intent(ParticipantsActivity.this, IntraChatActivity.class);
                intent.putExtra("UserId", invitee.getInviteeId());
                intent.putExtra("UserImage", "");
                intent.putExtra("UserName", invitee.getInviteeName());
                startActivity(intent);
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
