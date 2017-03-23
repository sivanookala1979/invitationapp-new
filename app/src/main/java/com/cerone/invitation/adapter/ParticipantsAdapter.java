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
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.Invitee;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, Jan 2, 2016
 */
public class ParticipantsAdapter extends ArrayAdapter<Invitee> {

    Context context;
    List<Invitee> participantsList = new ArrayList<Invitee>();
    boolean invisibleInfo;

    public ParticipantsAdapter(Context context, int resource, List<Invitee> participantsList, boolean invisibleInfo) {
        super(context, resource);
        this.context = context;
        this.participantsList = participantsList;
        this.invisibleInfo = invisibleInfo;
    }

    @Override
    public int getCount() {
        return participantsList.size();
    }

    @Override
    public Invitee getItem(int position) {
        return participantsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.participants_item, parent, false);
        Invitee invitee = getItem(position);
        TextView participantName = (TextView) row.findViewById(R.id.participant_name);
        TextView participantMobileNumber = (TextView) row.findViewById(R.id.participant_number);
        TextView participantStatus = (TextView) row.findViewById(R.id.status);
        TextView distance = (TextView) row.findViewById(R.id.distance);
        ImageView chat = (ImageView)row.findViewById(R.id.chat);
        ImageView call = (ImageView)row.findViewById(R.id.call);
        ImageView admin = (ImageView)row.findViewById(R.id.admin);
        ImageView block = (ImageView)row.findViewById(R.id.block);
        if(invitee.isAdmin())
            admin.setColorFilter(getContext().getResources().getColor(R.color.green));
        if(invitee.isBlocked())
            block.setColorFilter(getContext().getResources().getColor(R.color.red));

        chat.setOnClickListener(createOnClickListener(position,parent));
        call.setOnClickListener(createOnClickListener(position,parent));
        admin.setOnClickListener(createOnClickListener(position,parent));
        block.setOnClickListener(createOnClickListener(position,parent));
        participantName.setText(" " + invitee.getInviteeName());
        participantMobileNumber.setText(" " + invitee.getMobileNumber());
        participantStatus.setText("Last seen " + invitee.getStatus() + " ago.");
        distance.setText("" + StringUtils.getTrimmeDistance(invitee.getDistance()) + " away.");
        if (invisibleInfo) {
            distance.setVisibility(View.GONE);
            participantStatus.setVisibility(View.GONE);
        }
        row.setOnClickListener(createOnClickListener(position,parent));
        return row;
    }
    public View.OnClickListener createOnClickListener(final int position, final ViewGroup parent) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        };
    }
}
