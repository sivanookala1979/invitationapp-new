/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.ParticipantsAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.*;
import com.example.syncher.InvitationSyncher;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, Jan 2, 2016
 */
public class ParticipantsActivity extends BaseActivity implements OnClickListener {

    ListView listView;
    ImageButton headerBack;
    ParticipantsAdapter adapter;
    List<Invitee> participantsList = new ArrayList<Invitee>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);
        TextView title = (TextView) findViewById(R.id.headerTitle);
        title.setText("Invitees");
        listView = (ListView) findViewById(R.id.events_list);
        headerBack = (ImageButton) findViewById(R.id.headerBack);
        headerBack.setOnClickListener(this);
        getParticipants();
    }

    private void getParticipants() {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                Event eventDetails = InvtAppPreferences.getEventDetails();
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                participantsList = invitationSyncher.getInvitees(eventDetails.getEventId(), "");
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
}
