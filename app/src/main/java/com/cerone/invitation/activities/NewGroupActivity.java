/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.ContactAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.InvtTextWatcher;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.*;
import com.example.syncher.GroupSyncher;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, 24-Feb-2016
 */
public class NewGroupActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

    EditText groupName;
    ListView contactsListView;
    List<User> users = new ArrayList<User>();
    ContactAdapter contactAdapter;
    Button createGroup;
    List<String> selectedContacts = new ArrayList<String>();
    TextInputLayout groupNameInput;
    ImageView newPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_layout);
        addToolbarView();
        groupNameInput = (TextInputLayout) findViewById(R.id.groupNameInput);
        groupName = (EditText) findViewById(R.id.groupName);
        contactsListView = (ListView) findViewById(R.id.contacts);
        createGroup = (Button) findViewById(R.id.createGroup);
        groupName.addTextChangedListener(new InvtTextWatcher(groupName, groupNameInput, "Group name should not be empty."));
        contactsListView.setOnItemClickListener(this);
        createGroup.setOnClickListener(this);
        newPerson = (ImageView) findViewById(R.id.toolbarEvent);
        newPerson.setOnClickListener(this);
        //createLeftMenu(this);
        contactAdapter = new ContactAdapter(getApplicationContext(), R.layout.contact_item, users, true);
        contactsListView.setAdapter(contactAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        if (!(adapterView.getAdapter() instanceof ContactAdapter)) {
//            if (index != 3)
//                onLeftMenuItemClick(adapterView, view, index, id);
//            else
//                ToastHelper.blueToast(getApplicationContext(), "You are in create group.");
        } else {
            User user = users.get(index);
            switch (view.getId()) {
                case R.id.removeContact :
                    selectedContacts.remove(user.getPhoneNumber());
                    users.remove(user);
                    contactAdapter.setUsers(users);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbarEvent :
                InvtAppPreferences.setContacts(new ArrayList<User>());
                Intent contactIntent = new Intent(this, MultipleContactsActivity.class);
                contactIntent.putExtra("group", true);
                startActivityForResult(contactIntent, 100);
                break;
            case R.id.createGroup :
                final String groupNameInfo = groupName.getText().toString();
                if (selectedContacts.size() > 0 && groupNameInfo.length() > 0) {
                    new InvtAppAsyncTask(this) {

                        ServerResponse serverResponse;

                        @Override
                        public void process() {
                            String contactsString = StringUtils.getContactsString(selectedContacts);
                            Group group = new Group();
                            group.setGroupName(groupNameInfo);
                            group.setContacts(contactsString);
                            GroupSyncher groupSyncher = new GroupSyncher();
                            serverResponse = groupSyncher.createGroup(group);
                        }

                        @Override
                        public void afterPostExecute() {
                            if (serverResponse.getId()>0) {
                                ToastHelper.blueToast(getApplicationContext(), serverResponse.getStatus());
                                finish();
                            } else {
                                ToastHelper.redToast(getApplicationContext(), serverResponse.getStatus());
                            }
                        }
                    }.execute();
                } else {
                    if (groupNameInfo.length() == 0) {
                        ToastHelper.redToast(getApplicationContext(), "Please enter group name.");
                    } else {
                        ToastHelper.redToast(getApplicationContext(), "Please choose contacts.");
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Result", "requestCode " + requestCode + " resultCode " + resultCode);
        if (data != null && requestCode == 100 && resultCode == 1) {
            List<User> contacts = InvtAppPreferences.getContacts();
            for (User user : contacts) {
                if (!selectedContacts.contains(user.getPhoneNumber())) {
                    selectedContacts.add(user.getPhoneNumber());
                    users.add(user);
                }
            }
            if (users.size() > 0) {
                contactAdapter.setUsers(users);
            }
        }
    }
}