package com.cerone.invitation.fragement;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.chat.IntraChatActivity;
import com.cerone.invitation.adapter.ParticipantsAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.*;
import com.example.syncher.InvitationSyncher;
import com.example.utills.InvitationAppConstants;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class TabPending extends BaseFragment  {

    ProgressDialog progressDialog;
    List<Invitee> participantsList = new ArrayList<Invitee>();
    ParticipantsAdapter adapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_layout, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        listView = (ListView) view.findViewById(R.id.events_list);
        listView.setOnItemClickListener(this);
        Log.d("Tab", "Tab pending");
        getParticipants();
        return view;
    }

    private void getParticipants() {
        new InvtAppAsyncTask(getActivity()) {

            @Override
            public void process() {
                Event eventDetails = InvtAppPreferences.getEventDetails();
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                participantsList = invitationSyncher.getInvitees(eventDetails.getEventId(), InvitationAppConstants.PENDING);
            }

            @Override
            public void afterPostExecute() {
                if (participantsList.size() > 0) {
                    adapter = new ParticipantsAdapter(getContext(), R.layout.participants_item, participantsList, false);
                    listView.setAdapter(adapter);
                }
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Invitee invitee = participantsList.get(i);
        switch (view.getId()) {
            case R.id.chat:
                Intent intent = new Intent(getContext(), IntraChatActivity.class);
                intent.putExtra("UserId", invitee.getInviteeId());
                intent.putExtra("UserImage", "");
                intent.putExtra("UserName", invitee.getInviteeName());
                startActivity(intent);
                break;
            case R.id.call:
                callToUser(invitee.getMobileNumber());
                break;
        }
    }


}
