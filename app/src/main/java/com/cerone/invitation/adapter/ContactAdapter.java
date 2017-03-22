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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.User;

import java.util.List;

/**
 * @author Adarsh.T
 * @version 1.0, Dec 24, 2015
 */
public class ContactAdapter extends ArrayAdapter<User> {

    private List<User> users;
    Context context;

    public ContactAdapter(Context context, int resource, List<User> users, boolean isnewGroup) {
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
        View row = inflater.inflate(R.layout.contact_item, parent, false);
        User user = getItem(position);
        CheckBox isAdmin = (CheckBox) row.findViewById(R.id.shareEvent_check);
        Button removeContact = (Button) row.findViewById(R.id.removeContact);
        TextView name = (TextView) row.findViewById(R.id.contactPersonName);
        TextView mobileNumber = (TextView) row.findViewById(R.id.contactPersonMobileNumber);
        name.setText(user.getUserName());
        mobileNumber.setText(user.getPhoneNumber());
        removeContact.setOnClickListener(createOnClickListener(position, parent));
        isAdmin.setChecked(user.isAdmin());
        isAdmin.setOnClickListener(createOnClickListener(position, parent));
        return row;
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
