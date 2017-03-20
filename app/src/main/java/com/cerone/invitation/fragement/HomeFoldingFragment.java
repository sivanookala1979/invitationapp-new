package com.cerone.invitation.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cerone.invitation.R;

import com.cerone.invitation.activities.PersonalizeActivity;
import com.cerone.invitation.adapter.FoldingCellListAdapter;
import com.cerone.invitation.helpers.ActivityCommunicator;
import com.cerone.invitation.helpers.HomeScreenCommunicator;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.MobileHelper;
import com.example.dataobjects.Event;
import com.example.syncher.EventSyncher;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.r;

/**
 * Created by adarsh on 3/19/17.
 */

public class HomeFoldingFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    ListView eventsList;
    List<Event> allEventsList = new ArrayList<Event>();
    List<Event> filterList = new ArrayList<Event>();
    EventSyncher eventSyncher = new EventSyncher();
    FoldingCellListAdapter adapter;
    private HomeScreenCommunicator activityCommunicator;

    boolean eventFilter = true, invitationFilter = false;

    public static HomeFoldingFragment newInstance() {
        HomeFoldingFragment fragment = new HomeFoldingFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_folding_layout, container, false);
        eventsList = (ListView) view.findViewById(R.id.events_list);
        adapter = new FoldingCellListAdapter(getActivity(), allEventsList);
        eventsList.setAdapter(adapter);
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });

        activityCommunicator = (HomeScreenCommunicator) getActivity();

        loadEvents();
        return view;
    }

    public void updateData(boolean eventFilter, boolean invitationsFilter) {
        this.eventFilter = eventFilter;
        this.invitationFilter = invitationsFilter;
        Log.d("Adarsh","Log  eventFilter "+eventFilter+" "+invitationFilter);
        applyFilters();
    }

    private void applyFilters() {
        filterList.clear();
        if (eventFilter && invitationFilter) {
            filterList.addAll(allEventsList);
        } else if (eventFilter) {
            for (Event event : allEventsList) {
                if (!event.isInvitation()) {
                    filterList.add(event);
                }
            }
        } else {
            for (Event event : allEventsList) {
                if (event.isInvitation()) {
                    filterList.add(event);
                }
            }
        }
        adapter.updateList(filterList);

    }

    private void loadEvents() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    allEventsList = eventSyncher.getAllEventsNew();
                }

                @Override
                public void afterPostExecute() {
                    applyFilters();
                    //adapter.updateList(allEventsList);
                }
            }.execute();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), PersonalizeActivity.class));
    }
}