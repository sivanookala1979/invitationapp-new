package com.cerone.invitation.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.chat.IntraChatActivity;
import com.cerone.invitation.adapter.FoldingCellListAdapter;
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


public class EventsHistoryActivity extends BaseActivity {
    ListView eventsList;
    List<Event> allEventsList = new ArrayList<Event>();
    List<Event> filterList = new ArrayList<Event>();
    EventSyncher eventSyncher = new EventSyncher();
    FoldingCellListAdapter adapter;
    ServerResponse serverResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_history);
        addToolbarView();

        eventsList = (ListView) findViewById(R.id.events_list);
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                final Event event = filterList.get(pos);
                Intent intent;
                InvtAppPreferences.setEventDetails(event);
                switch (view.getId()) {
                    case R.id.actionOne:
                        if (!event.isInvitation() || (event.isAccepted() && event.isAdmin())) {
                            intent = new Intent(getApplicationContext(), ShareEventActivity.class);
                            intent.putExtra("newEvent", false);
                            startActivity(intent);
                        } else {
                            eventDetails = event;
                            if (event.isAccepted()) {
                                intent = new Intent(getApplicationContext(), ShowInviteePositions.class);
                                startActivity(intent);
                            } else {
                                showLocationPermissionDialog();
                            }
                        }
                        break;
                    case R.id.actionTwo:
                        if (!event.isInvitation() || (event.isAccepted() && event.isAdmin())) {
                            intent = new Intent(getApplicationContext(), CreateNewEventActivity.class);
                            startActivity(intent);
                        } else {
                            if (event.isAccepted()) {
                                intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
                                intent.putExtra("showChatFragment", true);
                                startActivity(intent);
                            }
                        }
                        break;
                    case R.id.actionThree:
                        if ((event.isInvitation()&&event.isAccepted()) || (event.isAccepted() && event.isAdmin())) {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EventsHistoryActivity.this);
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (event.isInvitation()&&event.isAdmin()) {
                                        deleteAdmin(event.getEventId());
                                        if(serverResponse != null) {
                                            if (serverResponse.isSuccess()) {
                                                deleteEvent(event);
                                            } else {
                                                ToastHelper.blueToast(getApplicationContext(),serverResponse.getStatus() );
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
//                        intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
//                        startActivity(intent);
                        break;
                    case R.id.chatLayout:
                    case R.id.chatIcon:
                        intent = new Intent(getApplicationContext(), IntraChatActivity.class);
                        intent.putExtra("UserId", event.getOwnerId());
                        intent.putExtra("UserImage", "");
                        intent.putExtra("UserName", event.getOwnerName());
                        startActivity(intent);
                        break;
                    case R.id.nav_address:
                        if(event!=null&&event.getLatitude()>0) {
                            intent = new Intent(getApplicationContext(), LocationDetailsActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Lat, Long not provided", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        ((FoldingCell) view).toggle(false);
                        adapter.registerToggle(pos);
                        break;
                }
            }
        });
        loadEvents();
    }

    private void loadEvents() {
        if (MobileHelper.hasNetwork(getApplicationContext(), EventsHistoryActivity.this, " to get events", null)) {
            new InvtAppAsyncTask(this) {

                @Override
                public void process() {
                    allEventsList = eventSyncher.getAllEventsNew();
                }

                @Override
                public void afterPostExecute() {
                    if(allEventsList!=null){
                        for (Event event:allEventsList) {
                            if(event.isExpired()){
                                filterList.add(event);
                            }
                        }
                        adapter = new FoldingCellListAdapter(getApplicationContext(), filterList);
                        eventsList.setAdapter(adapter);
                    }
                }
            }.execute();
        }
    }

    private void deleteAdmin(final int eventId) {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {
                if(eventId > 0){
                    serverResponse = eventSyncher.deleteAdmin(eventId);
                }else{
                    ToastHelper.blueToast(getApplicationContext(), "No inputs");
                }
            }

            @Override
            public void afterPostExecute() {
                if (serverResponse!=null) {
                    ToastHelper.blueToast(getApplicationContext(), serverResponse.getStatus());
                }
            }
        }.execute();
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        startActivity(new Intent(getApplicationContext(), PersonalizeActivity.class));
//    }

    private void deleteEvent(final Event event) {
        new InvtAppAsyncTask(this) {

            ServerResponse serverResponse;

            @Override
            public void process() {
                EventSyncher eventSyncher = new EventSyncher();
                serverResponse = eventSyncher.deleteEvent(event.getEventId());
            }

            @Override
            public void afterPostExecute() {
                Toast.makeText(getApplicationContext(), serverResponse.getStatus(), Toast.LENGTH_LONG).show();
                if (serverResponse.getId() > 0) {
                    deleteEventByEventId(event.getEventId());
                }
            }
        }.execute();
    }

    @Override
    public void acceptOrRejectInvitation(final boolean status, final Event event, final String locationPermission) {
        new InvtAppAsyncTask(this) {

            Eventstatistics response;

            @Override
            public void process() {
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                response = invitationSyncher.getInvitationStatus(event.getEventId(), status, locationPermission);
            }

            @Override
            public void afterPostExecute() {
                if (response.isValid()) {
                    for (Event eventInfo : filterList) {
                        if (eventInfo.getEventId() == event.getEventId()) {
                            eventInfo.setAccepted(true);
                            break;
                        }
                    }
                    if(!status){
                        deleteEventByEventId(event.getEventId());
                    }else{
                    }
                }
                ToastHelper.blueToast(getApplicationContext(), response.getMessage());
            }


        }.execute();
    }
    public void deleteEventByEventId(int eventId) {
        for (Event eventInfo : filterList) {
            if (eventInfo.getEventId() == eventId) {
                filterList.remove(eventInfo);
                Log.d("Delete","Removed event");
                break;
            }
        }
    }
}
