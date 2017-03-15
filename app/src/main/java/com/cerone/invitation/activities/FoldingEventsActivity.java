package com.cerone.invitation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.FoldingCellListAdapter;
import com.example.dataobjects.Event;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

/**
 * Created by adarsht on 15/03/17.
 */

public class FoldingEventsActivity extends BaseActivity {
    ListView theListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folding_activity_layout);
        addToolbarView();
        theListView = (ListView) findViewById(R.id.events_list);
        final ArrayList<Event> items = new ArrayList<Event>();
        items.add(new Event());
        items.add(new Event());
        items.add(new Event());
        items.add(new Event());
        items.add(new Event());
        items.add(new Event());
        items.add(new Event());

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, items);

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

    }
}
