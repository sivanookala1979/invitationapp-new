/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.*;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, 02-Mar-2016
 */
public class HomeEventAdapter extends ArrayAdapter<Event> {

    Context context;
    List<Event> events = new ArrayList<Event>();
    boolean displayTag;
    int imageResources[] = {
            R.drawable.event_picture,
            R.drawable.dinner,
            R.drawable.music};

    public HomeEventAdapter(Context context, int resource, List<Event> events, boolean displayTag) {
        super(context, resource);
        this.context = context;
        this.events = events;
        this.displayTag = displayTag;
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
        View row = inflater.inflate(R.layout.home_event_item, parent, false);
        Event event = getItem(position);
        TextView eventName = (TextView) row.findViewById(R.id.eventName);
        TextView eventDate = (TextView) row.findViewById(R.id.startDate);
        TextView eventdescription = (TextView) row.findViewById(R.id.eventDescription);
        ImageView imageView = (ImageView) row.findViewById(R.id.eventPicture);
        TextView acceptCount = (TextView) row.findViewById(R.id.eventAcceptCount);
        TextView checkInCount = (TextView) row.findViewById(R.id.checkedinCount);
        TextView total = (TextView) row.findViewById(R.id.totalInvitees);
        imageView.setBackgroundResource(imageResources[position % 3]);
        eventName.setText(event.getName());
        eventDate.setText(StringUtils.formatDateAndTime(event.getStartDateTime(), 5));
        acceptCount.setText(event.getAcceptedCount() + "");
        checkInCount.setText(event.getCheckedInCount() + "");
        total.setText(event.getInviteesCount() + "");
        if (displayTag) {
            int ownerId = InvtAppPreferences.getOwnerId();
            TextView tag = (TextView) row.findViewById(R.id.role);
            tag.setVisibility(View.VISIBLE);
            if (event.getOwnerId() == ownerId) {
                tag.setBackgroundResource(R.color.green_light);
            } else {
                tag.setBackgroundResource(R.color.red_light);
            }
        }
        return row;
    }
}
