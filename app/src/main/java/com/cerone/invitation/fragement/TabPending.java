package com.cerone.invitation.fragement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.ParticipantsAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.*;
import com.example.syncher.InvitationSyncher;
import com.example.utills.InvitationAppConstants;

import java.util.ArrayList;
import java.util.List;


public class TabPending extends Fragment {

    ProgressDialog progressDialog;
    List<Invitee> participantsList = new ArrayList<Invitee>();
    ParticipantsAdapter adapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_layout, container, false);
        view.findViewById(R.id.action_header).setVisibility(View.GONE);
        listView = (ListView) view.findViewById(R.id.events_list);
        //ToastHelper.blueToast(getContext(), "Tab pending");
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
}
