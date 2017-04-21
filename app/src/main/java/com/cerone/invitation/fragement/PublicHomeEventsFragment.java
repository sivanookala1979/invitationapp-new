package com.cerone.invitation.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.PublicHomeActivity;
import com.cerone.invitation.adapter.PublicFoldingCellListAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.City;
import com.example.dataobjects.EventFilter;
import com.example.dataobjects.FavoriteTopic;
import com.example.dataobjects.PublicEvent;
import com.example.syncher.PublicEventsSyncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cerone.invitation.R.id.trendingLayout;

/**
 * Created by adarsht on 17/04/17.
 */

public class PublicHomeEventsFragment extends BaseFragment implements View.OnClickListener {
    EditText searchText;
    ImageView iconSearch;
    ListView eventsList;
    LinearLayout selectorTags, searchTags;
    List<PublicEvent> allEventsList = new ArrayList<PublicEvent>();
    List<PublicEvent> newEventsList = new ArrayList<PublicEvent>();
    PublicEventsSyncher publicEventSyncher = new PublicEventsSyncher();
    PublicFoldingCellListAdapter adapter;
    EventFilter eventFilters;
    View mainView;
    String selectedCity;

    public static PublicHomeEventsFragment newInstance() {
        PublicHomeEventsFragment fragment = new PublicHomeEventsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.public_home_events_fragment, container, false);
        mainView = view;
        eventFilters = InvtAppPreferences.getEventFilters();
        eventsList = (ListView) view.findViewById(R.id.events_list);
        searchText = (EditText) view.findViewById(R.id.search);
        iconSearch = (ImageView) view.findViewById(R.id.icon_search);
        selectorTags = (LinearLayout) view.findViewById(R.id.selector_tags);
        searchTags = (LinearLayout) view.findViewById(R.id.search_tags);

        iconSearch.setOnClickListener(this);

        applyOnClickListeners(trendingLayout, R.id.recommendedLayout, R.id.freeLayout, R.id.weekendLayout, R.id.offersLayout, R.id.friendsAttendingLayout, R.id.favoritesLayout);
        loadPublicEvents();
        return view;
    }

    private void loadPublicEvents() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    allEventsList = publicEventSyncher.getPublicEvents();
                }

                @Override
                public void afterPostExecute() {
                    if(allEventsList!=null&&allEventsList.size()>0) {
                        City city = eventFilters.getSelectedCity();
                        selectedCity = city.getName();
                        selectorTags(selectedCity+" trending");
                        List<FavoriteTopic> favoriteTopicList = eventFilters.getFavoriteTopicList();
                        for (PublicEvent publicEvent:allEventsList) {
                            if(publicEvent.getCity().equalsIgnoreCase(city.getName())){
                                Log.d("selected city", city.getName());
                                for (FavoriteTopic favoriteTopic:favoriteTopicList) {
                                    if(publicEvent.getService().equalsIgnoreCase(favoriteTopic.getName())){
                                        Log.d("selected topic", favoriteTopic.getName());
                                        newEventsList.add(publicEvent);
                                    }
                                }
                            }
                        }
                        adapter = new PublicFoldingCellListAdapter(getActivity(), newEventsList);
                        eventsList.setAdapter(adapter);
                    }
                }
            }.execute();
        }
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
            case R.id.trendingLayout:
                changeFilterIcon(R.id.trendingIcon);
                selectorTags(selectedCity+" trending");
                break;
            case R.id.recommendedLayout:
                changeFilterIcon(R.id.recommendedIcon);
                selectorTags(selectedCity+" recommended");
                break;
            case R.id.freeLayout:
                changeFilterIcon(R.id.freeIcon);
                selectorTags(selectedCity+" free");
                break;
            case R.id.weekendLayout:
                changeFilterIcon(R.id.weekendIcon);
                selectorTags(selectedCity+" weekend");
                break;
            case R.id.offersLayout:
                changeFilterIcon(R.id.offersIcon);
                selectorTags(selectedCity+" offers");
                break;
            case R.id.friendsAttendingLayout:
                changeFilterIcon(R.id.friendsAttendingIcon);
                selectorTags(selectedCity+" friendsAttending");
                break;
            case R.id.favoritesLayout:
                changeFilterIcon(R.id.favoritesIcon);
                selectorTags(selectedCity+" favourites");
                break;
            case R.id.icon_search:
                searchTags(searchText.getText().toString().trim());
                break;
        }
    }

    private void selectorTags(final String selectorTag) {
        selectorTags.removeAllViews();
        final String[] selected = selectorTag.trim().split(" ");
        final List<String> stringList = new ArrayList<String>(Arrays.asList(selected));
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < stringList.size(); i++) {
            View child = inflater.inflate(R.layout.custom_tag, null);
            TextView select = (TextView) child.findViewById(R.id.txt_searched);
            Button remove = (Button) child.findViewById(R.id.remove);
            if(i==1){
                remove.setVisibility(View.GONE);
            }
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PublicHomeActivity.class);
                    startActivity(intent);
                }
            });
            select.setText(stringList.get(i));
            selectorTags.addView(child);
        }
    }

    private void searchTags(final String searchTag) {
        searchTags.removeAllViews();
        if(!searchText.getText().toString().isEmpty()) {
            searchTags.setVisibility(View.VISIBLE);
            final String[] search = searchTag.trim().split(" ");
            final List<String> stringList = new ArrayList<String>(Arrays.asList(search));
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < stringList.size(); i++) {
                View child = inflater.inflate(R.layout.custom_tag, null);
                TextView searched = (TextView) child.findViewById(R.id.txt_searched);
                Button remove = (Button) child.findViewById(R.id.remove);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View removed = (View) v.getParent();
                        TextView text = (TextView) removed.findViewById(R.id.txt_searched);
                        stringList.remove(text.getText().toString());
                        String stringAfter = "";
                        for (int j = 0; j < stringList.size(); j++) {
                            stringAfter += stringList.get(j) + " ";
                        }
                        searchText.setText(stringAfter);
                        searchTags(searchText.getText().toString());
                        if(!searchText.getText().toString().isEmpty()) {
                            searchTags.setVisibility(View.VISIBLE);
                        }else{
                            searchTags.setVisibility(View.GONE);
                        }
                    }
                });
                searched.setText(stringList.get(i));
                searchTags.addView(child);
            }
        }else{
            ToastHelper.blueToast(getContext(), "Please enter search.");
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