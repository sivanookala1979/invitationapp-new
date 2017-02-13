package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.UserProfile;
import com.cerone.invitation.adapter.HomeEventAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.cerone.invitation.service.MyService;
import com.cerone.invitation.service.NotificationService;
import com.example.dataobjects.Event;
import com.example.dataobjects.ServiceInformation;
import com.example.syncher.EventSyncher;

import java.util.ArrayList;
import java.util.List;


public class HomeScreenActivity extends BaseActivity implements OnClickListener, OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    ListView myEvents;
    HomeEventAdapter adapter;
    List<Event> myEventsList = new ArrayList<Event>();
    int ownerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_menu_activity);
        myEvents = (ListView) findViewById(R.id.events_list);
        ImageView imageView = (ImageView) findViewById(R.id.toolbarEvent);
        imageView.setOnClickListener(this);
        closePreviousServices();
        Log.d("Token", InvtAppPreferences.getAccessToken());
        ownerId = InvtAppPreferences.getOwnerId();
        Log.d("Owner id", ownerId + "");
        myEvents.setOnItemClickListener(this);
        createLeftMenu();
        loadEvents();

    }

    private void closePreviousServices() {
        List<ServiceInformation> serviceDetailsList = InvtAppPreferences.getServiceDetails();
        for(int i= 0;i<serviceDetailsList.size();i++){
            MyService service = new MyService();
            service.CancelAlarm(getApplicationContext(),i);
            NotificationService notificationService = new NotificationService();
            notificationService.CancelAlarm(getApplicationContext(),i);
        }
        InvtAppPreferences.setServiceDetails(new ArrayList<ServiceInformation>());
    }


    private void createLeftMenu() {
        addToolbarView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            case R.id.toolbarEvent:
                InvtAppPreferences.setEventDetails(null);
                startActivity(new Intent(this, NewEventActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

    @Override
    public void onBackPressed() {
        doExit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.home :
                intent = new Intent(this, HomeScreenActivity.class);
                break;
            case R.id.profile :
                intent = new Intent(this, UserProfile.class);
                break;
            case R.id.myEvents :
                intent = new Intent(this, MyEventsActivity.class);
                break;
            case R.id.myInvitations  :
                intent = new Intent(this, InvitationActivity.class);
                break;
            case R.id.nav_add_group :
                intent = new Intent(this, NewGroupActivity.class);
                break;
            case R.id.settings :
                ToastHelper.blueToast(getApplicationContext(), "Need to implement settings.");
                break;
            case R.id.logout :
                InvtAppPreferences.setLoggedIn(false);
                InvtAppPreferences.reset();
                Intent logoutIntent = new Intent(HomeScreenActivity.this, SignInActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                break;
        }
        if (R.id.logout != id && intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left_out_animation, R.anim.right_to_left_in_animation);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}