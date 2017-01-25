/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.*;

import java.util.List;


/**
 * @author adarsh
 * @version 1.0, Feb 24, 2016
 */
public class MultipleContactAdapter extends ArrayAdapter<User> {

    private List<User> users;
    Context context;
    int screenWhite = Color.parseColor("#fafafa");
    int lightBlue = Color.parseColor("#c5cae9");

    public MultipleContactAdapter(Context context, int resource, List<User> users) {
        super(context, resource);
        this.context = context;
        this.setUsers(users);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.multiple_contact_item, parent, false);
        User user = getItem(position);
        CheckBox selectedItem = (CheckBox) row.findViewById(R.id.checkeditem);
        selectedItem.setChecked(user.isSelected());
        selectedItem.setOnClickListener(createOnClickListener(position, parent));
        LinearLayout layout = (LinearLayout) row.findViewById(R.id.parentLayout);
        layout.setOnClickListener(createOnClickListener(position, parent));
        TextView name = (TextView) row.findViewById(R.id.contactPersonName);
        TextView mobileNumber = (TextView) row.findViewById(R.id.contactPersonMobileNumber);
        name.setText(user.getUserName());
        mobileNumber.setText(user.getPhoneNumber());
        return row;
    }

    private OnLongClickListener createOnLongClickListener(final int position, final ViewGroup parent) {
        return new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return ((ListView) parent).performLongClick();
            }
        };
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
}