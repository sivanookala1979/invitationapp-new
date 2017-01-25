package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.HomeEventAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.*;
import com.example.syncher.EventSyncher;

import java.util.ArrayList;
import java.util.List;


public class HomeScreenActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    ListView myEvents;
    HomeEventAdapter adapter;
    List<Event> myEventsList = new ArrayList<Event>();
    int ownerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_layout_new);
        myEvents = (ListView) findViewById(R.id.events_list);
        ImageView imageView = (ImageView) findViewById(R.id.toolbarEvent);
        imageView.setOnClickListener(this);
        InvtAppPreferences.setServiceDetails(new ServiceInformation());
        Log.d("Token", InvtAppPreferences.getAccessToken());
        ownerId = Integer.parseInt(InvtAppPreferences.getOwnerId());
        Log.d("Owner id", ownerId + "");
        myEvents.setOnItemClickListener(this);
        createLeftMenu(this);
        loadEvents();
    }

    private void loadEvents() {
        if (MobileHelper.hasNetwork(getApplicationContext(), HomeScreenActivity.this, " to get events", null)) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    EventSyncher eventSyncher = new EventSyncher();
                    myEventsList = eventSyncher.getAllEvents();
                }

                @Override
                public void afterPostExecute() {
                    adapter = new HomeEventAdapter(getApplicationContext(), R.layout.home_event_item, myEventsList, true);
                    myEvents.setAdapter(adapter);
                    if (myEventsList.size() > 0) {
                        if (!InvtAppPreferences.isServiceRefresh()) {
                            InvtAppPreferences.setServiceRefresh(true);
                            activateService();
                        }
                    } else {
                        ToastHelper.blueToast(getApplicationContext(), "No events found.");
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.toolbarEvent :
                InvtAppPreferences.setEventDetails(null);
                startActivity(new Intent(this, NewEventActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!(parent.getAdapter() instanceof HomeEventAdapter)) {
            if (position != 0)
                onLeftMenuItemClick(parent, view, position, id);
            else
                ToastHelper.blueToast(getApplicationContext(), "You are in Home.");
        } else {
            if (ownerId > 0) {
                Event event = myEventsList.get(position);
                InvtAppPreferences.setEventDetails(event);
                Intent intent;
                if (event.getOwnerId() == ownerId) {
                    intent = new Intent(getApplicationContext(), MyEventActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MyInvitationActivity.class);
                }
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        doExit();
    }
}