package com.cerone.invitation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.FoldingCellListAdapter;
import com.cerone.invitation.adapter.HomeEventAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.syncher.EventSyncher;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import static com.cerone.invitation.R.id.myEvents;

/**
 * Created by adarsht on 15/03/17.
 */

public class FoldingEventsActivity extends BaseActivity {
    ListView theListView;
    List<Event> allEventsList= new ArrayList<Event>();
    EventSyncher eventSyncher = new EventSyncher();
    FoldingCellListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folding_activity_layout);
        addToolbarView();
        theListView = (ListView) findViewById(R.id.events_list);

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        adapter = new FoldingCellListAdapter(this, allEventsList);

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
            }
        });

        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });
        loadEvents();
    }
    private void loadEvents() {
        if (MobileHelper.hasNetwork(getApplicationContext(), FoldingEventsActivity.this, " to get events", null)) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    allEventsList = eventSyncher.getAllEventsNew();
                }

                @Override
                public void afterPostExecute() {
                    adapter.updateList(allEventsList);
                }
            }.execute();
        }
    }
}
