package com.cerone.invitation.fragement;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.FoldingCellListAdapter;
import com.cerone.invitation.adapter.PublicFoldingCellListAdapter;
import com.cerone.invitation.helpers.HomeScreenCommunicator;
import com.example.dataobjects.Event;
import com.example.syncher.EventSyncher;

import java.util.ArrayList;
import java.util.List;

import static com.cerone.invitation.R.id.eventImage;
import static com.cerone.invitation.R.id.trendingLayout;

/**
 * Created by adarsht on 17/04/17.
 */

public class PublicHomeEventsFragment extends BaseFragment implements View.OnClickListener {
    ListView eventsList;
    List<Event> allEventsList = new ArrayList<Event>();
    EventSyncher eventSyncher = new EventSyncher();
    PublicFoldingCellListAdapter adapter;
    View mainView;

    public static PublicHomeEventsFragment newInstance() {
        PublicHomeEventsFragment fragment = new PublicHomeEventsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.public_home_events_fragment, container, false);
        eventsList = (ListView) view.findViewById(R.id.events_list);
        adapter = new PublicFoldingCellListAdapter(getActivity(), allEventsList);
        eventsList.setAdapter(adapter);
        mainView = view;
        applyOnClickListeners(trendingLayout, R.id.recommendedLayout, R.id.freeLayout, R.id.weekendLayout, R.id.offersLayout, R.id.friendsAttendingLayout, R.id.favoritesLayout);
        return view;
    }

    private void applyOnClickListeners(int... layoutIds) {
        for (int viewId : layoutIds) {
            RelativeLayout layout = (RelativeLayout) mainView.findViewById(viewId);
            layout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case trendingLayout:
                changeFilterIcon(R.id.trendingIcon);
                break;
            case R.id.recommendedLayout:
                changeFilterIcon(R.id.recommendedIcon);
                break;
            case R.id.freeLayout:
                changeFilterIcon(R.id.freeIcon);
                break;
            case R.id.weekendLayout:
                changeFilterIcon(R.id.weekendIcon);
                break;
            case R.id.offersLayout:
                changeFilterIcon(R.id.offersIcon);
                break;
            case R.id.friendsAttendingLayout:
                changeFilterIcon(R.id.friendsAttendingIcon);
                break;
            case R.id.favoritesLayout:
                changeFilterIcon(R.id.favoritesIcon);
                break;
        }
    }
    
    private void changeFilterIcon(int id) {
        int imageIds[] = {R.id.trendingIcon, R.id.recommendedIcon, R.id.freeIcon, R.id.weekendIcon, R.id.offersIcon, R.id.friendsAttendingIcon, R.id.favoritesIcon};
        int orangeColour = getActivity().getResources().getColor(R.color.happening_orange);
        int greyColour = getActivity().getResources().getColor(R.color.happening_text_grey_color);
        for (int imageId : imageIds) {
            ImageView image = (ImageView) mainView.findViewById(imageId);
            if (imageId == id) {
                image.setColorFilter(orangeColour);
            } else {
                image.setColorFilter(greyColour);
            }
        }
    }
}