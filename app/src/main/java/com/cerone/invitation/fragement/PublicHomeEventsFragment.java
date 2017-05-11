package com.cerone.invitation.fragement;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.PublicEventDetailsActivity;
import com.cerone.invitation.activities.PublicHomeActivity;
import com.cerone.invitation.adapter.PublicFoldingCellListAdapter;
import com.cerone.invitation.helpers.FontTypes;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.example.dataobjects.City;
import com.example.dataobjects.EventFilter;
import com.example.dataobjects.PublicEvent;
import com.example.dataobjects.SaveResult;
import com.example.syncher.PublicEventsSyncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by adarsht on 17/04/17.
 */

public class PublicHomeEventsFragment extends BaseFragment implements View.OnClickListener {
    EditText searchText;
    ImageView iconSearch;
    ListView eventsList;
    LinearLayout selectorTags, searchTags, searchBar, showEventDetails ;
    List<PublicEvent> newEventsList = new ArrayList<PublicEvent>();
    List<PublicEvent> favouriteEvents = new ArrayList<PublicEvent>();
    List<PublicEvent> trendingEvents = new ArrayList<PublicEvent>();
    List<PublicEvent> recommendedEvents = new ArrayList<PublicEvent>();
    List<PublicEvent> freeEvents = new ArrayList<PublicEvent>();
    List<PublicEvent> weekendEvents = new ArrayList<PublicEvent>();
    List<PublicEvent> offerEvents = new ArrayList<PublicEvent>();
    List<PublicEvent> searchedEvents = new ArrayList<PublicEvent>();
    Set<Integer> setId = new HashSet<>();
    PublicEventsSyncher publicEventSyncher = new PublicEventsSyncher();
    PublicFoldingCellListAdapter adapter;
    EventFilter eventFilters = new EventFilter();
    SaveResult saveResult = new SaveResult();
    PublicEvent publicEvent = new PublicEvent();
    City city = new City();
    String selectedCity;
    boolean isFavourite;
    View mainView;

    public static PublicHomeEventsFragment newInstance() {
        PublicHomeEventsFragment fragment = new PublicHomeEventsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.public_home_events_fragment, container, false);
        mainView = view;
        eventsList = (ListView) view.findViewById(R.id.events_list);
        eventsList.setEmptyView(view.findViewById( R.id.empty_list_view));
        searchText = (EditText) view.findViewById(R.id.search);
        iconSearch = (ImageView) view.findViewById(R.id.icon_search);
        selectorTags = (LinearLayout) view.findViewById(R.id.selector_tags);
        searchTags = (LinearLayout) view.findViewById(R.id.search_tags);
        searchBar = (LinearLayout) view.findViewById(R.id.search_bar);
        showEventDetails = (LinearLayout) view.findViewById(R.id.showEventDetails);
        iconSearch.setOnClickListener(this);
        eventsList.setOnItemClickListener(this);
        eventFilters = InvtAppPreferences.getEventFilters();
        city = eventFilters.getSelectedCity();
        selectedCity = city.getName();
        selectorTags(selectedCity+" trending");
        getTrendingPublicEvents();
        FontTypes.setEditTextRegularFont(searchText);
        applyOnClickListeners(R.id.trendingLayout, R.id.recommendedLayout, R.id.freeLayout, R.id.weekendLayout, R.id.offersLayout, R.id.favoritesLayout);
        return view;
    }

    public void updateData(boolean select) {
        if(select){
            searchBar.setVisibility(View.VISIBLE);
            searchText.requestFocus();
        }else{
            searchBar.setVisibility(View.GONE);
            searchTags.setVisibility(View.GONE);
            searchText.getText().clear();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;
        publicEvent = newEventsList.get(i);
        InvtAppPreferences.setPublicEventDetails(publicEvent);
        if(!setId.contains(i)){
            addViews(publicEvent.getId());
        }
        setId.add(i);
        switch (view.getId()) {
            case R.id.layout_favourite:
            case R.id.header_favourite:
                if(!publicEvent.isFavourite()){
                    addFavourites(publicEvent.getId(), city.getId());
                }else{
                    removeFavourites(publicEvent.getId(), city.getId());
                }
                break;
            case R.id.layout_cart:
            case R.id.header_cart:
                intent = new Intent(getActivity(), PublicEventDetailsActivity.class);
                intent.putExtra("eventId", publicEvent.getId());
                intent.putExtra("cityId", city.getId());
                startActivityForResult(intent, 1);
                break;
            case R.id.layout_friendsAttending:
            case R.id.header_friendsAttending:
                publicEvent.setFriendsAttending(true);
                adapter.updateList(newEventsList);
                InvtAppPreferences.setPublicEventDetails(publicEvent);
                intent = new Intent(getActivity(), PublicEventDetailsActivity.class);
                intent.putExtra("eventId", publicEvent.getId());
                intent.putExtra("cityId", city.getId());
                startActivityForResult(intent, 1);
                break;
            case R.id.layout_close:
            case R.id.header_close:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelPublicEvents(publicEvent.getId());
                    }
                });
                alertDialog.setNegativeButton("No", null);
                alertDialog.setMessage("Do you want to delete?");
                alertDialog.show();
                break;
            default:
                publicEvent.setFriendsAttending(true);
                adapter.updateList(newEventsList);
                InvtAppPreferences.setPublicEventDetails(publicEvent);
                intent = new Intent(getActivity(), PublicEventDetailsActivity.class);
                intent.putExtra("eventId", publicEvent.getId());
                intent.putExtra("cityId", city.getId());
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if(data!=null) {
                int eventId = data.getExtras().getInt("eventId");
                Log.d("calcel", "true"+eventId);
                cancelPublicEvents(eventId);
            }
        }
    }

    private void addViews(final int eventId) {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to add views", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    saveResult = publicEventSyncher.addViews(eventId);
                }

                @Override
                public void afterPostExecute() {
                    if(saveResult!=null){
                        if(saveResult.isSuccess()){
                            publicEvent.setFriendsAttending(true);
                            adapter.updateList(newEventsList);
                            Toast.makeText(getActivity(), saveResult.getStatus(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), saveResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }.execute();
        }
    }

    private void addFavourites(final int eventId, final int cityId) {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to add favourite", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    saveResult = publicEventSyncher.addFavourites(eventId, cityId);
                }

                @Override
                public void afterPostExecute() {
                    if(saveResult!=null){
                        if(saveResult.isSuccess()){
                            publicEvent.setFavourite(true);
                            adapter.updateList(newEventsList);
                            //Toast.makeText(getActivity(), saveResult.getStatus(), Toast.LENGTH_SHORT).show();
                        }else{
                            //Toast.makeText(getActivity(), saveResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }.execute();
        }
    }

    private void removeFavourites(final int eventId, final int cityId) {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to remove favourite", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    saveResult = publicEventSyncher.removeFavourites(eventId, cityId);
                }

                @Override
                public void afterPostExecute() {
                    if(saveResult!=null){
                        if(saveResult.isSuccess()){
                            if(isFavourite){
                                for (PublicEvent publicEvent:newEventsList) {
                                    if(publicEvent.getId()==eventId){
                                        newEventsList.remove(publicEvent);
                                        break;
                                    }
                                }
                            }
                            publicEvent.setFavourite(false);
                            adapter.updateList(newEventsList);
                            Toast.makeText(getActivity(), saveResult.getStatus(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), saveResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }.execute();
        }
    }

    private void cancelPublicEvents(final int eventId) {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to cancel public event", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    saveResult = publicEventSyncher.cancelPublicEvents(eventId);
                }

                @Override
                public void afterPostExecute() {
                    if(saveResult!=null){
                        if(saveResult.isSuccess()){
                            for (PublicEvent publicEvent:newEventsList) {
                                if(publicEvent.getId()==eventId){
                                    newEventsList.remove(publicEvent);
                                    break;
                                }
                            }
                            adapter.updateList(newEventsList);
                            Toast.makeText(getActivity(), saveResult.getStatus(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), saveResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }.execute();
        }
    }

    private void getMyFavourites() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get favourite events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    favouriteEvents = publicEventSyncher.getMyFavourites(city.getId());
                }

                @Override
                public void afterPostExecute() {
                    if(favouriteEvents!=null) {
                        isFavourite = true;
                        newEventsList.clear();
                        newEventsList.addAll(favouriteEvents);
                        adapter = new PublicFoldingCellListAdapter(getActivity(), favouriteEvents);
                        eventsList.setAdapter(adapter);
                    }else{
                        Toast.makeText(getActivity(), "no favourite events", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    private void getTrendingPublicEvents() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get trending events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    trendingEvents = publicEventSyncher.getTrendingPublicEvents(city.getId());
                }

                @Override
                public void afterPostExecute() {
                    if(freeEvents!=null) {
                        isFavourite = false;
                        newEventsList.clear();
                        newEventsList.addAll(trendingEvents);
                        adapter = new PublicFoldingCellListAdapter(getActivity(), trendingEvents);
                        eventsList.setAdapter(adapter);
                    }else{
                        Toast.makeText(getActivity(), "no trending events", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    private void getRecommendedEvents() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get recommended events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    recommendedEvents = publicEventSyncher.getRecommendedEventsList(city.getId());
                }

                @Override
                public void afterPostExecute() {
                    if(freeEvents!=null) {
                        isFavourite = false;
                        newEventsList.clear();
                        newEventsList.addAll(recommendedEvents);
                        adapter = new PublicFoldingCellListAdapter(getActivity(), recommendedEvents);
                        eventsList.setAdapter(adapter);
                    }else{
                        Toast.makeText(getActivity(), "no recommended events", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    private void getFreePublicEvents() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get free events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    freeEvents = publicEventSyncher.getFreePublicEvents(city.getId());
                }

                @Override
                public void afterPostExecute() {
                    if(freeEvents!=null) {
                        isFavourite = false;
                        newEventsList.clear();
                        newEventsList.addAll(freeEvents);
                        adapter = new PublicFoldingCellListAdapter(getActivity(), freeEvents);
                        eventsList.setAdapter(adapter);
                    }else{
                        Toast.makeText(getActivity(), "no free events", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    private void getWeekendPublicEvents() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get weekend events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    weekendEvents = publicEventSyncher.getWeekendPublicEvents(city.getId());
                }

                @Override
                public void afterPostExecute() {
                    if(weekendEvents!=null) {
                        isFavourite = false;
                        newEventsList.clear();
                        newEventsList.addAll(weekendEvents);
                        adapter = new PublicFoldingCellListAdapter(getActivity(), weekendEvents);
                        eventsList.setAdapter(adapter);
                    }else{
                        Toast.makeText(getActivity(), "no weekend events", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    private void getOfferedPublicEvents() {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get offer events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    offerEvents = publicEventSyncher.getOfferedPublicEvents(city.getId());
                }

                @Override
                public void afterPostExecute() {
                    if(weekendEvents!=null) {
                        isFavourite = false;
                        newEventsList.clear();
                        newEventsList.addAll(offerEvents);
                        adapter = new PublicFoldingCellListAdapter(getActivity(), offerEvents);
                        eventsList.setAdapter(adapter);
                    }else{
                        Toast.makeText(getActivity(), "no offer events", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    private void getPublicEventsWithKeywords(final String keyWords) {
        if (MobileHelper.hasNetwork(getActivity(), getActivity(), " to get search events", null)) {
            new InvtAppAsyncTask(getActivity()) {

                @Override
                public void process() {
                    searchedEvents = publicEventSyncher.getPublicEventsWithKeywords(city.getId(), keyWords);
                    Log.d("keyWords", keyWords);
                }

                @Override
                public void afterPostExecute() {
                    if(weekendEvents!=null) {
                        isFavourite = false;
                        newEventsList.clear();
                        newEventsList.addAll(searchedEvents);
                        adapter = new PublicFoldingCellListAdapter(getActivity(), searchedEvents);
                        eventsList.setAdapter(adapter);
                    }else{
                        Toast.makeText(getActivity(), "no searched events", Toast.LENGTH_SHORT).show();
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
                changeHomeFilterIcon(R.id.trendingIcon);
                selectorTags(selectedCity+" trending");
                getTrendingPublicEvents();
                break;
            case R.id.recommendedLayout:
                changeHomeFilterIcon(R.id.recommendedIcon);
                selectorTags(selectedCity+" recommended");
                getRecommendedEvents();
                break;
            case R.id.freeLayout:
                changeHomeFilterIcon(R.id.freeIcon);
                selectorTags(selectedCity+" free");
                getFreePublicEvents();
                break;
            case R.id.weekendLayout:
                changeHomeFilterIcon(R.id.weekendIcon);
                selectorTags(selectedCity+" weekend");
                getWeekendPublicEvents();
                break;
            case R.id.offersLayout:
                changeHomeFilterIcon(R.id.offersIcon);
                selectorTags(selectedCity+" offers");
                getOfferedPublicEvents();
                break;
            case R.id.favoritesLayout:
                changeHomeFilterIcon(R.id.favoritesIcon);
                selectorTags(selectedCity+" favourites");
                getMyFavourites();
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

    private void searchTags(String searchTag) {
        searchTags.removeAllViews();
        if(!searchText.getText().toString().isEmpty()) {
            searchTags.setVisibility(View.VISIBLE);
        }else{
            searchTags.setVisibility(View.GONE);
        }            searchTag = searchTag.replaceAll("\\s{2,}"," ");
            getPublicEventsWithKeywords(searchTag.replaceAll(" ",","));
            final String[] search = searchTag.trim().split(" ");
            final List<String> stringList = new ArrayList<String>(Arrays.asList(search));
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < stringList.size(); i++) {
                View child = inflater.inflate(R.layout.custom_tag, null);
                TextView searched = (TextView) child.findViewById(R.id.txt_searched);
                Button remove = (Button) child.findViewById(R.id.remove);
                LinearLayout searchTagLayout = (LinearLayout) child.findViewById(R.id.layout_searchTag);
                searchTagLayout.setOnClickListener(new View.OnClickListener() {
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
                        searchText.setSelection(searchText.getText().length());
                        searchTags(searchText.getText().toString().trim());
                    }
                });
                searched.setText(stringList.get(i));
                searchTags.addView(child);
            }
    }

    private void changeHomeFilterIcon(int id) {
        int imageIds[] = {R.id.trendingIcon, R.id.recommendedIcon, R.id.freeIcon, R.id.weekendIcon, R.id.offersIcon, R.id.favoritesIcon};
        for (int imageId : imageIds) {
            ImageView image = (ImageView) mainView.findViewById(imageId);
            if (imageId == id) {
                image.setColorFilter(getActivity().getResources().getColor(R.color.happening_orange));
            } else {
                image.setColorFilter(getActivity().getResources().getColor(R.color.happening_text_grey_color));
            }
        }
    }
}