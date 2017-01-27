/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.ContactAdapter;
import com.cerone.invitation.adapter.GroupAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.*;
import com.example.syncher.EventSyncher;
import com.example.syncher.GroupSyncher;
import com.example.syncher.InvitationSyncher;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.cerone.invitation.R.id.headerBack;


/**
 * @author Adarsh.T
 * @version 1.0, Dec 24, 2015
 */
public class ShareEventActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    List<User> allSelectedlist = new ArrayList<User>();
    List<String> listOfParticipantsPhoneNumbers = new ArrayList<String>();
    List<Group> listOfSelectedGroups = new ArrayList<Group>();
    List<String> listOfGroupIds = new ArrayList<String>();
    ListView listView, groupList;
    ContactAdapter adapter;
    GroupAdapter groupAdapter;
    Button shareEvent, groupsView, appContactsView, smsContactsView;
    ShareContact shareContacts = new ShareContact();
    List<Group> myGroups = new ArrayList<Group>();
    ArrayList<User> allPhoneContacts = new ArrayList<User>();
    List<User> appContacts = new ArrayList<User>();
    List<User> smsContacts = new ArrayList<User>();
    boolean isNewEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_events_layout);
        addToolbarView();
        isNewEvent = getIntent().getExtras().getBoolean("newEvent");
        listView = (ListView) findViewById(R.id.attendeeslist);
        groupList = (ListView) findViewById(R.id.grouplist);
        shareEvent = (Button) findViewById(R.id.shareEventData);
        groupsView = (Button) findViewById(R.id.groups);
        appContactsView = (Button) findViewById(R.id.appContacts);
        smsContactsView = (Button) findViewById(R.id.smsContacts);
        shareEvent.setOnClickListener(this);
        groupsView.setOnClickListener(this);
        appContactsView.setOnClickListener(this);
        smsContactsView.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        groupList.setOnItemClickListener(this);
        showEventData();
        loadGroupsAndContacts();
    }

    private void loadGroupsAndContacts() {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                allPhoneContacts = MobileHelper.getAllPhoneContacts(getApplicationContext());
                GroupSyncher groupSyncher = new GroupSyncher();
                shareContacts = groupSyncher.getDifferentiateContacts(StringUtils.getContactsListFromUsers(allPhoneContacts));
                splitContacts();
                myGroups = groupSyncher.getGroups();
            }

            @Override
            public void afterPostExecute() {
                if (myGroups.size() > 0)
                    groupsView.setVisibility(View.VISIBLE);
                if (shareContacts.getAppContacts().size() > 0)
                    appContactsView.setVisibility(View.VISIBLE);
                if (shareContacts.getSmsContacts().size() > 0)
                    smsContactsView.setVisibility(View.VISIBLE);
                updateButtons();
            }
        }.execute();
    }

    public void updateButtons() {
        int smsCount = getsmsContactsCount();
        int phoneCount = allSelectedlist.size() - smsCount;
        groupsView.setText("GROUPS " + listOfSelectedGroups.size() + " of " + myGroups.size());
        appContactsView.setText("IN-APP CONTACTS " + smsCount + " of " + shareContacts.getAppContacts().size());
        smsContactsView.setText("SMS CONTACTS " + phoneCount + " of " + shareContacts.getSmsContacts().size());
    }

    private int getsmsContactsCount() {
        int count = 0;
        for (User user : allSelectedlist) {
            if (user.isPhoneContact())
                count++;
        }
        return count;
    }

    private void splitContacts() {
        List<String> appContactsinfo = shareContacts.getAppContacts();
        for (User user : allPhoneContacts) {
            if (appContactsinfo.contains(user.getPhoneNumber())) {
                user.setPhoneContact(true);
                appContacts.add(user);
            } else {
                smsContacts.add(user);
            }
        }
    }

    private void showEventData() {
        Event eventDetails = InvtAppPreferences.getEventDetails();
        TextView eventName = (TextView) findViewById(R.id.eventNameInfo);
        TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
        TextView eventStartDate = (TextView) findViewById(R.id.eventstartDate);
        TextView eventEndDate = (TextView) findViewById(R.id.eventEndDate);
        TextView eventStartTime = (TextView) findViewById(R.id.eventStartTime);
        TextView eventEndTime = (TextView) findViewById(R.id.eventEndtime);
        TextView eventLocation = (TextView) findViewById(R.id.eventLocation);
        eventName.setText(eventDetails.getName().trim() + " Details");
        eventDescription.setText(eventDetails.getDescription());
        eventStartDate.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 1));
        eventEndDate.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 1));
        eventStartTime.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 2));
        eventEndTime.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 2));
        eventLocation.setText(eventDetails.getAddress());
        if (!eventDetails.getExtraAddress().isEmpty()) {
            LinearLayout extraAddressLayout = (LinearLayout) findViewById(R.id.extraAddressLayout);
            extraAddressLayout.setVisibility(View.VISIBLE);
            TextView extraAddress = (TextView) findViewById(R.id.extraAddress);
            extraAddress.setText("" + eventDetails.getExtraAddress());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.groups :
                //ToastHelper.blueToast(getApplicationContext(), "Need to implement groups information");
                InvtAppPreferences.setGroups(myGroups);
                Intent groupIntent = new Intent(this, MultipleGroupSelectionActivity.class);
                startActivityForResult(groupIntent, 300);
                break;
            case R.id.appContacts :
                InvtAppPreferences.setContacts(appContacts);
                Intent contactIntent = new Intent(this, MultipleContactsActivity.class);
                contactIntent.putExtra("group", false);
                startActivityForResult(contactIntent, 100);
                break;
            case R.id.smsContacts :
                InvtAppPreferences.setContacts(smsContacts);
                Intent smsIntent = new Intent(this, MultipleContactsActivity.class);
                smsIntent.putExtra("group", false);
                startActivityForResult(smsIntent, 200);
                break;
            case R.id.cancel :
                finish();
                break;
            case R.id.shareEventData :
                if (listOfParticipantsPhoneNumbers.size() > 0 || listOfGroupIds.size() > 0) {
                    new InvtAppAsyncTask(this) {

                        ServerResponse serverResponse;
                        int eventId = 0;

                        @Override
                        public void process() {
                            Event eventDetails = InvtAppPreferences.getEventDetails();
                            if (isNewEvent) {
                                EventSyncher eventSyncher = new EventSyncher();
                                serverResponse = eventSyncher.createEvent(eventDetails);
                                eventId = serverResponse.getId();
                            } else {
                                eventId = eventDetails.getEventId();
                            }
                            System.out.println("Event id " + eventId);
                            if (eventId > 0) {
                                InvitationSyncher invitationSyncher = new InvitationSyncher();
                                serverResponse = invitationSyncher.createInvitation(eventId, listOfParticipantsPhoneNumbers, listOfGroupIds);
                            }
                        }

                        @Override
                        public void afterPostExecute() {
                            String serverStatus = serverResponse.getStatus();
                            Toast.makeText(getApplicationContext(), serverStatus, Toast.LENGTH_LONG).show();
                            if (eventId > 0 && serverStatus.equals("Success")) {
                                if (isNewEvent) {
                                    Intent intent = new Intent(ShareEventActivity.this, HomeScreenActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    finish();
                                }
                            }
                        }
                    }.execute();
                } else
                    ToastHelper.redToast(getApplicationContext(), "Please select contacts");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ToastHelper.yellowToast(getApplicationContext(), "you are done requestCode" + requestCode + " resultCode " + resultCode);
        if (resultCode == 1 && (requestCode == 100 || requestCode == 200)) {
            updateContacts(requestCode);
            adapter = new ContactAdapter(getApplicationContext(), R.layout.contact_item, allSelectedlist, false);
            setContactsListSize();
            listView.setAdapter(adapter);
        }
        if (resultCode == 1 && requestCode == 300) {
            updateGroupsData();
            groupAdapter = new GroupAdapter(getApplicationContext(), R.layout.group_item, listOfSelectedGroups, false);
            setGroupListSize();
            groupList.setAdapter(groupAdapter);
        }
        updateButtons();
    }

    private void setContactsListSize() {
        int totalHeight = 0;
        if (adapter.getCount() > 0) {
            View listItem = adapter.getView(0, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight * adapter.getCount()) + (adapter.getCount() * 5);
        listView.setLayoutParams(params);
    }

    private void setGroupListSize() {
        int totalHeight = 0;
        if (groupAdapter.getCount() > 0) {
            View listItem = groupAdapter.getView(0, null, groupList);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = groupList.getLayoutParams();
        params.height = (totalHeight * groupAdapter.getCount()) + (groupAdapter.getCount() * 5);
        groupList.setLayoutParams(params);
    }

    private void updateGroupsData() {
        List<Group> groupsInfo = InvtAppPreferences.getGroups();
        for (Group group : groupsInfo) {
            if (!listOfGroupIds.contains(group.getGroupId())) {
                listOfSelectedGroups.add(group);
                listOfGroupIds.add(group.getGroupId() + "");
            }
        }
    }

    private void updateContacts(int requestCode) {
        List<User> usersList = InvtAppPreferences.getContacts();
        for (User user : usersList) {
            if (!listOfParticipantsPhoneNumbers.contains(user.getPhoneNumber())) {
                listOfParticipantsPhoneNumbers.add(user.getPhoneNumber());
                allSelectedlist.add(user);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.removeContact :
                User user = allSelectedlist.get(position);
                ToastHelper.blueToast(getApplicationContext(), "Contact Removed");
                listOfParticipantsPhoneNumbers.remove(user.getPhoneNumber());
                allSelectedlist.remove(user);
                adapter.setUsers(allSelectedlist);
                setContactsListSize();
                break;
            case R.id.removeGroup :
                Group group = listOfSelectedGroups.get(position);
                ToastHelper.blueToast(getApplicationContext(), "Contact Removed");
                listOfSelectedGroups.remove(group);
                listOfGroupIds.remove(position);
                groupAdapter.setUsers(listOfSelectedGroups);
                setGroupListSize();
                break;
            default :
                break;
        }
    }
}