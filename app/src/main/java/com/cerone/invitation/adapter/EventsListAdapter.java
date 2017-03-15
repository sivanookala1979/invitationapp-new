package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.EventItem;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by peekay on 15/3/17.
 */

    public class EventsListAdapter extends BaseAdapter{
        private Context context;
        private List<EventItem> eventItems;

        public EventsListAdapter(Context context, List<EventItem> eventItems) {
            this.context = context;
            this.eventItems = eventItems;
        }

        @Override
        public int getCount() {
            return eventItems.size();
        }

        @Override
        public EventItem getItem(int position) {
            return eventItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.event_row_item, parent, false);
            EventItem event = getItem(position);
            TextView eventText = (TextView) row.findViewById(R.id.eventItem_name);
            ImageView eventImage = (ImageView)row.findViewById(R.id.eventItem_image);
            eventText.setText(event.getName());
            Picasso.with(context).load(event.getImagePath()).into(eventImage);
            row.setOnClickListener(createOnClickListener(position, parent));
            return row;
        }

    public View.OnClickListener createOnClickListener(final int position, final ViewGroup parent) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((GridView) parent).performItemClick(v, position, 0);
            }
        };
    }
    }


