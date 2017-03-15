package com.cerone.invitation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.EventsListAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.EventItem;
import com.example.syncher.PublicHomeSyncher;

import java.util.ArrayList;
import java.util.List;

public class PersonalizeActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    List<EventItem> eventItems;
    GridView gridViewEvents;
    EventsListAdapter eventsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize);
        addToolbarView();
        setFontType(R.id.txt_personalize);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Personalize");
        gridViewEvents = (GridView) findViewById(R.id.gridView_events);
        gridViewEvents.setOnItemClickListener(this);
        getEventItems();
    }

    private void getEventItems() {
        if (MobileHelper.hasNetwork(getApplicationContext(), PersonalizeActivity.this, " to get eventItems", null)) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    PublicHomeSyncher Syncher = new PublicHomeSyncher();
                    eventItems = new ArrayList<EventItem>();
                    eventItems = Syncher.getEventItems();
                }

                @Override
                public void afterPostExecute() {
                    if(eventItems.size()>0){
                        eventsListAdapter = new EventsListAdapter(getApplicationContext(), eventItems);
                        gridViewEvents.setAdapter(eventsListAdapter);
                    } else {
                        ToastHelper.blueToast(getApplicationContext(), "No events found.");
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
