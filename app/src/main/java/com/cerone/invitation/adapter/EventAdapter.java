/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, Dec 24, 2015
 */
public class EventAdapter extends ArrayAdapter<Event> {

    private static final int INVITATIONS_SCREEN = 1;
    private static final int SHARE_EVENTS_SCREEN = 2;
    private static final int HOME_SCREEN = 0;
    Context context;
    private List<Event> events = new ArrayList<Event>();
    private List<Invitation> myInvitations = new ArrayList<Invitation>();
    int screenType = 1;

    public EventAdapter(Context context, int resource, List<Event> events, int screenType) {
        super(context, resource);
        this.context = context;
        this.events = events;
        this.screenType = screenType;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.event_item, parent, false);
        Event event = getItem(position);
        TextView eventName = (TextView) row.findViewById(R.id.eventName);
        TextView eventDate = (TextView) row.findViewById(R.id.startDate);
        TextView eventdescription = (TextView) row.findViewById(R.id.eventDescription);
        TextView eventAddress = (TextView) row.findViewById(R.id.eventAddress);
        TextView participantsInfo = (TextView) row.findViewById(R.id.participants);
        Button accept = (Button) row.findViewById(R.id.acceptEvent);
        Button reject = (Button) row.findViewById(R.id.rejectEvent);
        Button invite = (Button) row.findViewById(R.id.inviteEvent);
        Button invites = (Button) row.findViewById(R.id.invites);
        ImageView imageButton = (ImageView) row.findViewById(R.id.trackLocations);
        imageButton.setOnClickListener(createOnClickListener(position, parent));
        invites.setOnClickListener(createOnClickListener(position, parent));
        eventName.setText("Event Name : " + event.getName());
        eventDate.setText("Start Date : " + event.getStartDateTime());
        eventdescription.setText("Description : " + event.getDescription());
        eventAddress.setText("Address : " + event.getAddress());
        participantsInfo.setText("Accepte: " + event.getAcceptedCount() + "  Reject : " + event.getRejectedCount() + " Total: " + event.getInviteesCount());
        if (screenType != HOME_SCREEN) {
            if (screenType == INVITATIONS_SCREEN) {
                imageButton.setVisibility(View.VISIBLE);
                if (!isInvitationAccepted(event.getEventId())) {
                    accept.setVisibility(View.VISIBLE);
                    reject.setVisibility(View.VISIBLE);
                    accept.setOnClickListener(createOnClickListener(position, parent));
                    reject.setOnClickListener(createOnClickListener(position, parent));
                }
            } else if (screenType == SHARE_EVENTS_SCREEN) {
                row.findViewById(R.id.parentLayout).setOnClickListener(createOnClickListener(position, parent));
                invites.setVisibility(View.GONE);
                invite.setVisibility(View.GONE);
                //                invite.setVisibility(View.VISIBLE);
                //                invite.setOnClickListener(createOnClickListener(position, parent));
            }
        } else {
            invites.setVisibility(View.GONE);
        }
        return row;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public OnClickListener createOnClickListener(final int position, final ViewGroup parent) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        };
    }

    public void setMyInvitations(List<Invitation> myInvitations) {
        this.myInvitations = myInvitations;
    }

    public boolean isInvitationAccepted(int ParticipintId) {
        boolean status = false;
        for (Invitation event : myInvitations) {
            if (ParticipintId == event.getEventId()) {
                if (event.isSelected()) {
                    status = true;
                }
                break;
            }
        }
        return status;
    }
}
