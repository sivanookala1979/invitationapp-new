package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
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
import com.example.dataobjects.*;
import com.example.syncher.EventSyncher;
import com.example.syncher.InvitationSyncher;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class InvitationActivity extends BaseActivity implements OnItemClickListener {

    List<Event> events = new ArrayList<Event>();
    List<Invitation> myInvitations = new ArrayList<Invitation>();
    ListView listView;
    HomeEventAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_layout_new);
        addToolbarView();
        InvtAppPreferences.setScreenRefreshStatus(false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("My Invitations");
        listView = (ListView) findViewById(R.id.events_list);
        findViewById(R.id.fab_add).setVisibility(View.INVISIBLE);
        findViewById(R.id.toggle_layout).setVisibility(View.INVISIBLE);
        listView.setOnItemClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }

            private void refreshContent() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMyEvents();
                    }
                }, 1000);
            }
        });
        getMyEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(InvtAppPreferences.isScreenRefresh()){
            getMyEvents();
            InvtAppPreferences.setScreenRefreshStatus(false);
        }
    }

    public void getMyEvents() {
        if (MobileHelper.hasNetwork(getApplicationContext(), InvitationActivity.this, " to get your invitations", null)) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    InvitationSyncher invitationSyncher = new InvitationSyncher();
                    myInvitations = invitationSyncher.getMyInvitations();
                    if (myInvitations.size() > 0) {
                        EventSyncher eventSyncher = new EventSyncher();
                        String eventIds = StringUtils.getEventIds(myInvitations);
                        if (eventIds.length() > 0) {
                            events = eventSyncher.getMyEventsByIds(eventIds);
                        }
                    }
                }

                @Override
                public void afterPostExecute() {
                    if (events.size() > 0) {
                        adapter = new HomeEventAdapter(getApplicationContext(), R.layout.home_event_item, events, false);
                        listView.setAdapter(adapter);
                    } else {
                        ToastHelper.blueToast(getApplicationContext(), "No invitations found.");
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }.execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!(parent.getAdapter() instanceof HomeEventAdapter)) {
            if (position != 2) {
                //onLeftMenuItemClick(parent, view, position, id);
            }else
                ToastHelper.blueToast(getApplicationContext(), "You are in my invitations.");
        } else {
            Event event = events.get(position);
            event.setInvitation(true);
            InvtAppPreferences.setEventDetails(event);
            InvtAppPreferences.setInvitation(myInvitations.get(position));
            Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
            startActivity(intent);
        }
    }
}