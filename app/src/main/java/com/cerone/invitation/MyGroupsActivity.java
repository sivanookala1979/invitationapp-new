package com.cerone.invitation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.activities.BaseActivity;
import com.cerone.invitation.activities.NewGroupActivity;
import com.cerone.invitation.activities.ParticipantsActivity;
import com.cerone.invitation.adapter.GroupsAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.example.dataobjects.Group;
import com.example.syncher.GroupSyncher;

import java.util.ArrayList;
import java.util.List;

public class MyGroupsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    GroupsAdapter adapter;
    List<Group> groupsList = new ArrayList<Group>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);
        addToolbarView();
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("My Groups");
        ImageView addGroup = (ImageView) findViewById(R.id.toolbarEvent);
        listView = (ListView) findViewById(R.id.events_list);
        listView.setOnItemClickListener(this);
        addGroup.setOnClickListener(this);
        getgroups();
    }

    private void getgroups() {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                GroupSyncher groupSyncher = new GroupSyncher();
                groupsList = groupSyncher.getGroupItems();
            }

            @Override
            public void afterPostExecute() {
                if (groupsList.size() > 0) {
                    adapter = new GroupsAdapter(getApplicationContext(), R.layout.participants_item, groupsList);
                    listView.setAdapter(adapter);
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.toolbarEvent:
                Intent intent = new Intent(getApplicationContext(), NewGroupActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(position+"", "item clicked");
        Group group = (Group)parent.getItemAtPosition(position);
        final int groupId = group.getGroupId();
        Intent intent = new Intent(getApplicationContext(), ParticipantsActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("title", "Group Members");
        startActivity(intent);
    }
}
