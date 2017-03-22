/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import com.example.dataobjects.Event;
import com.example.dataobjects.Group;
import com.example.dataobjects.ServerResponse;
import com.example.dataobjects.User;
import com.example.syncher.EventSyncher;
import com.example.syncher.GroupSyncher;
import com.example.syncher.InvitationSyncher;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, Dec 24, 2015
 */
public class ShareEventActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    List<User> allSelectedlist = new ArrayList<User>();
    List<User> listOfParticipantsPhoneNumbers = new ArrayList<User>();
    List<Group> listOfSelectedGroups = new ArrayList<Group>();
    List<String> listOfGroupIds = new ArrayList<String>();
    ListView listView, groupList;
    ContactAdapter adapter;
    GroupAdapter groupAdapter;
    Button shareEvent, groupsView, contactsView;
    List<Group> myGroups = new ArrayList<Group>();
    ArrayList<User> allPhoneContacts = new ArrayList<User>();
    boolean isNewEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_events_layout);
        addToolbarView();
        setFontType(R.id.shareEventData,R.id.cancel,R.id.eventDescription,R.id.txt_start,R.id.txt_end,R.id.eventstartDate,R.id.eventStartTime,
                R.id.eventEndDate,R.id.eventEndtime,R.id.eventLocation);
        isNewEvent = getIntent().getExtras().getBoolean("newEvent");
        listView = (ListView) findViewById(R.id.attendeeslist);
        groupList = (ListView) findViewById(R.id.grouplist);
        shareEvent = (Button) findViewById(R.id.shareEventData);
        groupsView = (Button) findViewById(R.id.groups);
        contactsView = (Button) findViewById(R.id.contacts);
        shareEvent.setOnClickListener(this);
        groupsView.setOnClickListener(this);
        contactsView.setOnClickListener(this);
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
                myGroups = groupSyncher.getGroupItems();
            }

            @Override
            public void afterPostExecute() {
                if (myGroups.size() > 0)
                    groupsView.setVisibility(View.VISIBLE);
                    contactsView.setVisibility(View.VISIBLE);
                updateButtons();
            }
        }.execute();
    }

    public void updateButtons() {
        groupsView.setText("GROUPS " + listOfSelectedGroups.size() + " of " + myGroups.size());
        contactsView.setText("CONTACTS " + allSelectedlist.size() + " of " + allPhoneContacts.size());
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
                InvtAppPreferences.setGroups(myGroups);
                Intent groupIntent = new Intent(this, MultipleGroupSelectionActivity.class);
                startActivityForResult(groupIntent, 300);
                break;
            case R.id.contacts :
                InvtAppPreferences.setContacts(allPhoneContacts);
                Intent contactIntent = new Intent(this, MobileContactsActivity.class);
                startActivityForResult(contactIntent, 100);
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
                                getLatestContacts();
                                InvitationSyncher invitationSyncher = new InvitationSyncher();
                                serverResponse = invitationSyncher.createNewInvitation(eventId, listOfParticipantsPhoneNumbers, listOfGroupIds);
                            }
                        }

                        @Override
                        public void afterPostExecute() {
                            if (serverResponse!=null) {
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
        if (requestCode == 100) {
            updateContacts();
            adapter = new ContactAdapter(getApplicationContext(), R.layout.contact_item, allSelectedlist, false);
            setContactsListSize();
            listView.setAdapter(adapter);
        }
        if (requestCode == 300) {
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

    private void updateContacts() {
        List<User> usersList = InvtAppPreferences.getContacts();
        Log.d("selected contacts", usersList.size()+"");

        for (User user : usersList) {
            if (!listOfParticipantsPhoneNumbers.contains(user.getPhoneNumber())) {
                listOfParticipantsPhoneNumbers.add(user);
                allSelectedlist.add(user);
            }
        }
    }

    private void getLatestContacts(){
        listOfParticipantsPhoneNumbers = new ArrayList<User>();
            listOfParticipantsPhoneNumbers.addAll(allSelectedlist);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(allSelectedlist.size()>0) {
            User user = allSelectedlist.get(position);
            switch (view.getId()) {
                case R.id.removeContact:
                    ToastHelper.blueToast(getApplicationContext(), "Contact Removed");
                    listOfParticipantsPhoneNumbers.remove(user.getPhoneNumber());
                    allSelectedlist.remove(user);
                    adapter.setUsers(allSelectedlist);
                    setContactsListSize();
                    break;
                case R.id.removeGroup:
                    Group group = listOfSelectedGroups.get(position);
                    ToastHelper.blueToast(getApplicationContext(), "Group Removed");
                    listOfSelectedGroups.remove(group);
                    listOfGroupIds.remove(position);
                    groupAdapter.setUsers(listOfSelectedGroups);
                    setGroupListSize();
                    break;
                case R.id.shareEvent_check:
                    Log.d("is admin", position + " " + !user.isAdmin() + "");
                    user.setAdmin(!user.isAdmin());
                    adapter.setUsers(allSelectedlist);
                    if (user.isAdmin()) {
                        Toast.makeText(getApplicationContext(), user.getUserName() + " made as admin", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

    }
}