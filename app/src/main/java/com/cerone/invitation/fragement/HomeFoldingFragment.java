package com.cerone.invitation.fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.CreateNewEventActivity;
import com.cerone.invitation.activities.EventDetailsActivity;
import com.cerone.invitation.activities.LocationDetailsActivity;
import com.cerone.invitation.activities.PersonalizeActivity;
import com.cerone.invitation.activities.ShareEventActivity;
import com.cerone.invitation.activities.ShowInviteePositions;
import com.cerone.invitation.activities.chat.IntraChatActivity;
import com.cerone.invitation.adapter.FoldingCellListAdapter;
import com.cerone.invitation.helpers.HomeScreenCommunicator;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.Eventstatistics;
import com.example.dataobjects.ServerResponse;
import com.example.syncher.EventSyncher;
import com.example.syncher.InvitationSyncher;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsh on 3/19/17.
 */

public class HomeFoldingFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    ListView eventsList;
    List<Event> allEventsList = new ArrayList<Event>();
    List<Event> filterList = new ArrayList<Event>();
    EventSyncher eventSyncher = new EventSyncher();
    FoldingCellListAdapter adapter;
    ServerResponse serverResponse;
    private HomeScreenCommunicator activityCommunicator;

    boolean eventFilter = true, invitationFilter = true;

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
                final Event event = filterList.get(pos);
                Intent intent;
                InvtAppPreferences.setEventDetails(event);
                switch (view.getId()) {
                    case R.id.actionOne:
                        if (!event.isInvitation() || (event.isAccepted() && event.isAdmin())) {
                            intent = new Intent(getActivity(), ShareEventActivity.class);
                            intent.putExtra("newEvent", false);
                            startActivity(intent);
                        } else {
                            eventDetails = event;
                            if (event.isAccepted()) {
                                intent = new Intent(getActivity(), ShowInviteePositions.class);
                                startActivity(intent);
                            } else {
                                showLocationPermissionDialog();
                            }
                        }
                        break;
                    case R.id.actionTwo:
                        if (!event.isInvitation() || (event.isAccepted() && event.isAdmin())) {
                            intent = new Intent(getActivity(), CreateNewEventActivity.class);
                            startActivity(intent);
                        } else {
                            if (event.isAccepted()) {
                                intent = new Intent(getActivity(), EventDetailsActivity.class);
                                intent.putExtra("fragmentNumber", 3);
                                startActivity(intent);
                            }
                        }
                        break;
                    case R.id.actionThree:
                        if ((event.isInvitation()&&event.isAccepted()) || (event.isAccepted() && event.isAdmin())) {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (event.isInvitation()&&event.isAdmin()) {
                                        deleteAdmin(event.getEventId());
                                        if(serverResponse != null) {
                                            if (serverResponse.isSuccess()) {
                                                deleteEvent(event);
                                            } else {
                                                ToastHelper.blueToast(getActivity(),serverResponse.getStatus() );
                                            }
                                        }
                                    } else {
                                        deleteEvent(event);
                                    }
                                }
                            });

                            alertDialog.setNegativeButton("No", null);
                            alertDialog.setMessage("Do you want to delete?");
                            alertDialog.show();
                        } else {
                                acceptOrRejectInvitation(false, event, locationPermission);
                            }
                        break;
                    case R.id.showEventIcon:
                    case R.id.showEventDetails:
                        intent = new Intent(getActivity(), EventDetailsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.chatLayout:
                    case R.id.chatIcon:
                        intent = new Intent(getActivity(), IntraChatActivity.class);
                        intent.putExtra("UserId", event.getOwnerId());
                        intent.putExtra("UserImage", "");
                        intent.putExtra("UserName", event.getOwnerName());
                        startActivity(intent);
                        break;
                    case R.id.nav_address:
                        intent = new Intent(getActivity(), LocationDetailsActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        ((FoldingCell) view).toggle(false);
                        // register in adapter that state for selected cell is toggled
                        adapter.registerToggle(pos);
                        break;
                }
            }
        });

        activityCommunicator = (HomeScreenCommunicator) getActivity();
        loadEvents();
        return view;
    }

    public void updateData(boolean eventFilter, boolean invitationsFilter) {
        this.eventFilter = eventFilter;
        this.invitationFilter = invitationsFilter;
        Log.d("Adarsh", "Log  eventFilter " + eventFilter + " " + invitationFilter);
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
    private void deleteAdmin(final int eventId) {
        new InvtAppAsyncTask(getActivity()) {

            @Override
            public void process() {
                if(eventId > 0){
                    serverResponse = eventSyncher.deleteAdmin(eventId);
                }else{
                    ToastHelper.blueToast(getActivity(), "No inputs");
                }
            }

            @Override
            public void afterPostExecute() {
                if (serverResponse!=null) {
                    ToastHelper.blueToast(getActivity(), serverResponse.getStatus());
                }
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), PersonalizeActivity.class));
    }

    private void deleteEvent(final Event event) {
        new InvtAppAsyncTask(getActivity()) {

            ServerResponse serverResponse;

            @Override
            public void process() {
                EventSyncher eventSyncher = new EventSyncher();
                serverResponse = eventSyncher.deleteEvent(event.getEventId());
            }

            @Override
            public void afterPostExecute() {
                Toast.makeText(getActivity(), serverResponse.getStatus(), Toast.LENGTH_LONG).show();
                if (serverResponse.getId() > 0) {
                    deleteEventByEventId(event.getEventId());
                    applyFilters();
                }
            }
        }.execute();
    }

    @Override
    public void acceptOrRejectInvitation(final boolean status, final Event event, final String locationPermission) {
        new InvtAppAsyncTask(getActivity()) {

            Eventstatistics response;

            @Override
            public void process() {
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                response = invitationSyncher.getInvitationStatus(event.getEventId(), status, locationPermission);
            }

            @Override
            public void afterPostExecute() {
                if (response.isValid()) {
                    for (Event eventInfo : allEventsList) {
                        if (eventInfo.getEventId() == event.getEventId()) {
                            eventInfo.setAccepted(true);
                            break;
                        }
                    }
                    if(!status){
                        deleteEventByEventId(event.getEventId());
                    }else{
                        activityCommunicator.updateServices(true);
                    }
                    applyFilters();
                }
                ToastHelper.blueToast(getActivity(), response.getMessage());
            }


        }.execute();
    }
    public void deleteEventByEventId(int eventId) {
        for (Event eventInfo : allEventsList) {
            if (eventInfo.getEventId() == eventId) {
                allEventsList.remove(eventInfo);
                Log.d("Delete","Removed event");
                break;
            }
        }
    }

}