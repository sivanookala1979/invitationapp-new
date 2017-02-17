/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.GroupAdapter;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, 27-Feb-2016
 */
public class MultipleGroupSelectionActivity extends BaseActivity implements OnClickListener {

    List<Group> groups = new ArrayList<Group>();
    ListView listView;
    GroupAdapter adapter;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);
        addToolbarView();
        listView = (ListView) findViewById(R.id.events_list);
        done = (Button) findViewById(R.id.shareEventData);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Groups");
        done.setText("Done");
        visibleLayout(done);
        done.setOnClickListener(this);
        groups = InvtAppPreferences.getGroups();
        adapter = new GroupAdapter(getApplicationContext(), R.layout.multiple_contact_item, groups, true);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareEventData :
                Intent intent = new Intent();
                setResult(1, intent);
                InvtAppPreferences.setGroups(getSelectedGroups());
                finish();
                break;
        }
    }

    private List<Group> getSelectedGroups() {
        List<Group> validGroups = new ArrayList<Group>();
        for (Group group : groups) {
            if (group.isSelected())
                validGroups.add(group);
        }
        return validGroups;
    }
}