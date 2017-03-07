/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.MultipleContactAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.example.dataobjects.User;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, Feb 24, 2016
 */
public class MultipleContactsActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    List<User> users = new ArrayList<User>();
    List<User> actualUsers = new ArrayList<User>();
    List<String> listOfParticipantsPhoneNumbers = new ArrayList<String>();
    List<User> selectedUsers = new ArrayList<User>();
    ListView listView;
    MultipleContactAdapter adapter;
    Button done;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_contacts_layout);
        addToolbarView();
        setFontType(R.id.shareEventData);
        listView = (ListView) findViewById(R.id.events_list);
        done = (Button) findViewById(R.id.shareEventData);
        search = (EditText) findViewById(R.id.search);
        done.setText("Done");
        visibleLayout(done);
        done.setOnClickListener(this);
        actualUsers = InvtAppPreferences.getContacts();
        boolean isGroup = getIntent().getExtras().getBoolean("group");
        listView.setOnItemClickListener(this);
        setTextChangedListener();
        loadContacts(isGroup);
    }

    private void setTextChangedListener() {
        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String serchFlag = search.getText().toString().toUpperCase();
                if (serchFlag.length() > 1) {
                    new AsyncTask<String, Void, String>() {

                        @Override
                        protected String doInBackground(String... params) {
                            users.clear();
                            for (User filterUser : actualUsers) {
                                String number = filterUser.getPhoneNumber().toUpperCase();
                                String name = filterUser.getUserName().toUpperCase();
                                if (number.contains(serchFlag) || name.contains(serchFlag)) {
                                    users.add(filterUser);
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            updateAdapter();
                        };
                    }.execute();
                } else {
                    if (serchFlag.isEmpty()) {
                        users.clear();
                        users.addAll(actualUsers);
                        updateAdapter();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void loadContacts(final boolean isGroup) {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                if (isGroup)
                    actualUsers = MobileHelper.getAllPhoneContacts(getApplicationContext());
            }

            @Override
            public void afterPostExecute() {
                users.addAll(actualUsers);
                updateAdapter();
            }
        }.execute();
    }

    public void updateAdapter() {
        adapter = new MultipleContactAdapter(getApplicationContext(), R.layout.multiple_contact_item, users);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerBack :
                finish();
                break;
            case R.id.shareEventData :
                Intent intent = new Intent();
                setResult(1, intent);
                InvtAppPreferences.setContacts(getValidContacts());
                finish();
                break;
        }
    }

    private List<User> getValidContacts() {
        List<User> valicContacts = new ArrayList<User>();
        List<String> stringContacts = new ArrayList<String>();
        for (User user : selectedUsers) {
            if (user.isSelected() && !stringContacts.contains(user.getPhoneNumber())) {
                valicContacts.add(user);
                stringContacts.add(user.getPhoneNumber());
            }
        }
        return valicContacts;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        User selectedUser = users.get(position);
        if (selectedUser.isSelected()) {
            selectedUser.setSelected(false);
            selectedUsers.remove(getIndex(selectedUser));
        } else {
            selectedUser.setSelected(true);
            selectedUsers.add(selectedUser);
        }
        adapter.setUsers(users);
    }

    private int getIndex(User selectedUser) {
        int index = 0;
        for (User iterable_element : selectedUsers) {
            if (iterable_element.getPhoneNumber().equals(selectedUser.getPhoneNumber()) && iterable_element.getUserName().equals(selectedUser.getUserName())) {
                break;
            }
            index++;
        }
        return index;
    }
}