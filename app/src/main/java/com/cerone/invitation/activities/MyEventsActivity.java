/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.HomeEventAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.syncher.EventSyncher;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, Dec 24, 2015
 */
public class MyEventsActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    FloatingActionButton fabAdd;
    List<Event> events = new ArrayList<Event>();
    ListView listView;
    HomeEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_layout_new);
        addToolbarView();
        listView = (ListView) findViewById(R.id.events_list);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("My Events");
        findViewById(R.id.toggle_layout).setVisibility(View.INVISIBLE);
        listView.setOnItemClickListener(this);
        fabAdd.setOnClickListener(this);
        getMyEvents();
    }

    public void getMyEvents() {
        if (MobileHelper.hasNetwork(getApplicationContext(), MyEventsActivity.this, " to get your events", null)) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    EventSyncher eventSyncher = new EventSyncher();
                    events = eventSyncher.getMyEvents();
                }

                @Override
                public void afterPostExecute() {
                    adapter = new HomeEventAdapter(getApplicationContext(), R.layout.home_event_item, events, false);
                    listView.setAdapter(adapter);
                    if (events.size() == 0) {
                        ToastHelper.blueToast(getApplicationContext(), "No events found.");
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add :
                InvtAppPreferences.setEventDetails(null);
                startActivity(new Intent(this, NewEventActivity.class));
                break;
            default :
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!(parent.getAdapter() instanceof HomeEventAdapter)) {
//            if (position != 1)
//                onLeftMenuItemClick(parent, view, position, id);
//            else
//                ToastHelper.blueToast(getApplicationContext(), "You are in MY Events.");
        } else {
            InvtAppPreferences.setEventDetails(events.get(position));
            Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
            startActivity(intent);
        }
    }
}
