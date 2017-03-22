package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.Notification;

import java.util.List;

/**
 * Created by peekay on 29/12/16.
 */

public class NotificationAdapter extends ArrayAdapter<Notification> {

    List<Notification> notification;
    public NotificationAdapter(Context context, List<Notification> notification) {

        super(context,0,notification);
        this.notification = notification;

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        Notification notification = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_list_row, parent, false);

        }

        // Lookup view for data population

        TextView content = (TextView) convertView.findViewById(R.id.notificationTextView);

        content.setText(notification.getContent());

        return convertView;

    }

    public void updateAdapter(List<Notification> notification) {
        this.notification = notification;
        notifyDataSetChanged();
    }
}
