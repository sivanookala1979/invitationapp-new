package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.Group;

import java.util.List;

/**
 * Created by peekay on 16/2/17.
 */

public class GroupsAdapter extends ArrayAdapter<Group> {
    List<Group> groups;
    Context context;
    int resourse;
    public GroupsAdapter(Context context, int resource, List<Group> groups) {
        super(context, resource);
        this.context = context;
        this.resourse = resource;
        this.groups = groups;
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
        removeGroup.setVisibility(View.GONE);
        name.setText(group.getGroupName());

        return row;
    }

    public void setUsers(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }


}
