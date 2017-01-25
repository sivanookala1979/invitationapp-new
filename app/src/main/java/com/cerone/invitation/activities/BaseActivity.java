package com.cerone.invitation.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.CustomDrawerAdapter;
import com.cerone.invitation.adapter.ImageDialogAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.syncher.BaseSyncher;
import com.example.syncher.EventSyncher;
import com.example.syncher.InvitationSyncher;
import com.example.utills.InvitationAppConstants;
import com.example.utills.StringUtils;
import com.example.dataobjects.*;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    public static final int GOOGLE_MAPS_REQUEST = 1;
    InvtAppAsyncTask invtAppAsyncTask;
    List<Event> events = new ArrayList<Event>();
    ListView leftMenuView;
    public DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    List<DrawerItem> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#304ffe")));
    }

    public void createLeftMenu(OnItemClickListener itemClickListener) {
        leftMenuView = (ListView) findViewById(R.id.left_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_layout_icon);
        dataList = new ArrayList<DrawerItem>();
        dataList.add(new DrawerItem("Home", R.drawable.home));
        dataList.add(new DrawerItem("My Events", R.drawable.event));
        dataList.add(new DrawerItem("My Invitations", R.drawable.invitation));
        dataList.add(new DrawerItem("Create Group", R.drawable.add_group));
        dataList.add(new DrawerItem("Settings", R.drawable.settings));
        dataList.add(new DrawerItem("Log Out", R.drawable.log_out));
        leftMenuView.setAdapter(new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList));
        leftMenuView.setOnItemClickListener(itemClickListener);
        mDrawerToggle = createDrawerToggle();
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    private ActionBarDrawerToggle createDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, R.drawable.drawer_layout_icon, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
    }

    public void onLeftMenuItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position) {
            case 0 :
                intent = new Intent(this, HomeScreenActivity.class);
                break;
            case 1 :
                intent = new Intent(this, MyEventsActivity.class);
                break;
            case 2 :
                intent = new Intent(this, InvitationActivity.class);
                break;
            case 3 :
                intent = new Intent(this, NewGroupActivity.class);
                break;
            case 4 :
                ToastHelper.blueToast(getApplicationContext(), "Need to implement settings.");
                break;
            case 5 :
                InvtAppPreferences.setLoggedIn(false);
                InvtAppPreferences.reset();
                Intent logoutIntent = new Intent(BaseActivity.this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                break;
        }
        if (position != 5 && intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left_out_animation, R.anim.right_to_left_in_animation);
        }
        drawerLayout.closeDrawer(leftMenuView);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            int id = item.getItemId();
            //            if (id == R.id.action_settings) {
            //                InvtAppPreferences.setLoggedIn(false);
            //                InvtAppPreferences.reset();
            //                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            //                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //                startActivity(intent);
            //            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void executingTask(InvtAppAsyncTask invtAppAsyncTask) {
        this.invtAppAsyncTask = invtAppAsyncTask;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (invtAppAsyncTask != null) {
            invtAppAsyncTask.cancel(true);
        }
    }

    public void visibleLayout(View... view) {
        for (View widget : view) {
            widget.setVisibility(View.VISIBLE);
        }
    }

    public void setLoginDetails(ServerResponse serverResponse) {
        InvtAppPreferences.setLoggedIn(true);
        InvtAppPreferences.setOwnerId(serverResponse.getId());
        BaseSyncher.setAccessToken(serverResponse.getToken());
        InvtAppPreferences.setAccessToken(serverResponse.getToken());
    }

    public String getCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";//"91";
        //                TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //                CountryID = manager.getSimCountryIso().toUpperCase();
        //                String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        //                for (String element : rl) {
        //                    String[] g = element.split(",");
        //                    if (g[1].trim().equals(CountryID.trim())) {
        //                        CountryZipCode = g[0];
        //                        break;
        //                    }
        //                }
        return CountryZipCode;
    }

    public void activateService() {
        Log.d("Data", "Service started at " + InvtAppPreferences.getAccessToken() + " " + InvtAppPreferences.isLoggedIn());
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                List<Invitation> myInvitations = invitationSyncher.getMyInvitations();
                if (myInvitations.size() > 0) {
                    EventSyncher eventSyncher = new EventSyncher();
                    String eventIds = StringUtils.getAcceptedEventIds(myInvitations);
                    if (eventIds.length() > 0) {
                        events = eventSyncher.getMyEventsByIds(eventIds);
                    }
                    events = StringUtils.getFutureEvents(events);
                    StringUtils.sortEventsBadsedonStartDate(events);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (events.size() > 0) {
                    Event event = events.get(0);
                    Log.d("Data Event", "Name " + event.getName() + "Event start date " + event.getStartDateTime() + " end date " + event.getEndDateTime());
                    ServiceInformation information = new ServiceInformation();
                    information.setCheckInNotificationServiceStartTime(StringUtils.getNewDate(event.getStartDateTime(), -30));
                    information.setServiceStartTime(StringUtils.getNewDate(event.getStartDateTime(), -60));
                    information.setServieEndTime(StringUtils.getNewDate(event.getStartDateTime(), 30));
                    information.setEventStartTime(event.getStartDateTime());
                    information.setEnventEndTime(event.getEndDateTime());
                    information.setEvent(true);
                    information.setEventInfo(event);
                    InvtAppPreferences.setServiceDetails(information);
                    startService();
                } else {
                    Log.d("Data", "No future events found");
                }
            };
        }.execute();
    }

    private void startService() {
        ServiceInformation serviceDetails = InvtAppPreferences.getServiceDetails();
        if (serviceDetails.isEvent()) {
            Log.d("Data", "Service started at " + StringUtils.getCurrentDate());
            Intent myIntent = new Intent(this, MyService.class);
            myIntent.setAction(serviceDetails.getServiceStartTime());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000 * 2, pendingIntent);
        }
    }

    public void doExit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.show();
    }

    public void pictureDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_image_dialog);
        ListView listView = (ListView) dialog.findViewById(R.id.dialogListView);
        List<String> galleryList = new ArrayList<String>();
        galleryList.add("Take Photo");
        galleryList.add("Choose From Gallery");
        final ImageDialogAdapter dialogAdapter = new ImageDialogAdapter(this, R.layout.gallery_title, galleryList);
        listView.setAdapter(dialogAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                String strName = dialogAdapter.getItem(position);
                if (strName.contains("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, InvitationAppConstants.IMAGE_CAPTURE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, InvitationAppConstants.IMAG_LOAD);
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }
}