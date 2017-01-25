/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.*;

import java.util.List;


/**
 * @author Adarsh.T
 * @version 1.0, 27-Feb-2016
 */
public class GroupAdapter extends ArrayAdapter<Group> {

    List<Group> groups;
    Context context;
    int screenWhite = Color.parseColor("#fafafa");
    int lightBlue = Color.parseColor("#c5cae9");
    int orange = Color.parseColor("#ff9800");
    boolean selection;

    public GroupAdapter(Context context, int resource, List<Group> groups, boolean selection) {
        super(context, resource);
        this.context = context;
        this.groups = groups;
        this.selection = selection;
    }

    @Override
    public Group getItem(int position) {
        return groups.get(position);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.group_item, parent, false);
        final Group group = getItem(position);
        TextView name = (TextView) row.findViewById(R.id.groupName);
        Button removeGroup = (Button) row.findViewById(R.id.removeGroup);
        removeGroup.setOnClickListener(createOnClickListener(position, parent));
        name.setText(group.getGroupName());
        final LinearLayout layout = (LinearLayout) row.findViewById(R.id.parentLayout);
        if (selection) {
            removeGroup.setVisibility(View.GONE);
            layout.setBackgroundColor(group.isSelected() ? lightBlue : screenWhite);
            layout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    updateLayout(group, layout);
                    return true;
                }

                private void updateLayout(final Group group, final LinearLayout layout) {
                    Log.d("Selected", " " + group.isSelected());
                    if (group.isSelected()) {
                        group.setSelected(false);
                        layout.setBackgroundColor(screenWhite);
                    } else {
                        group.setSelected(true);
                        layout.setBackgroundColor(lightBlue);
                    }
                }
            });
        }
        return row;
    }

    public OnClickListener createOnClickListener(final int position, final ViewGroup parent) {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        };
    }

    public void setUsers(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }
}
