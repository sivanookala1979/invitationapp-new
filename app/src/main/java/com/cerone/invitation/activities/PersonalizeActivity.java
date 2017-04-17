package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.EventsListAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.EventFilter;
import com.example.dataobjects.FavoriteTopic;
import com.example.syncher.PublicHomeSyncher;

import java.util.ArrayList;
import java.util.List;

public class PersonalizeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    List<FavoriteTopic> favoriteTopics = new ArrayList<FavoriteTopic>();
    ;
    GridView gridViewEvents;
    EventsListAdapter eventsListAdapter;
    RelativeLayout saveAndNextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize);
        addToolbarView();
        setFontType(R.id.txt_personalize);
        saveAndNextView = (RelativeLayout) findViewById(R.id.next);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Personalize");
        gridViewEvents = (GridView) findViewById(R.id.gridView_events);
        gridViewEvents.setOnItemClickListener(this);
        saveAndNextView.setOnClickListener(this);
        getEventItems();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                List<FavoriteTopic> selectedTipics = getSelectedTopics();
                if (selectedTipics.size() > 2) {
                    EventFilter eventFilters = InvtAppPreferences.getEventFilters();
                    eventFilters.getFavoriteTopicList().clear();
                    eventFilters.setFavoriteTopicList(selectedTipics);
                    eventFilters.setCityList(eventFilters.getSelectedCitysList());
                    eventFilters.getSelectedCitysList().clear();
                    eventFilters.setValid(true);
                    InvtAppPreferences.setEventFilters(eventFilters);
                    Intent intent = new Intent(PersonalizeActivity.this, HomeScreenActivity.class);
                    intent.putExtra("showPublicScreen",true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    ToastHelper.redToast(this, "Please select atleast two favorite topics.");
                }
                break;
        }
    }

    private List<FavoriteTopic> getSelectedTopics() {
        List<FavoriteTopic> filteredList = new ArrayList<FavoriteTopic>();
        for (FavoriteTopic topic : favoriteTopics) {
            if (topic.isSelected()) {
                filteredList.add(topic);
            }
        }
        return filteredList;
    }

    private void getEventItems() {
        if (MobileHelper.hasNetwork(getApplicationContext(), PersonalizeActivity.this, " to get favorites.", null)) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    PublicHomeSyncher Syncher = new PublicHomeSyncher();
                    favoriteTopics = new ArrayList<FavoriteTopic>();
                    favoriteTopics = Syncher.getEventItems();
                }

                @Override
                public void afterPostExecute() {
                    if (favoriteTopics.size() > 0) {
                        eventsListAdapter = new EventsListAdapter(getApplicationContext(), favoriteTopics);
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
        FavoriteTopic eventItem = favoriteTopics.get(position);
        if (eventItem.isSelected()) {
            eventItem.setSelected(false);
        } else {
            eventItem.setSelected(true);
        }
        favoriteTopics.set(position, eventItem);
        eventsListAdapter.updateAdapter(favoriteTopics);
    }
}
