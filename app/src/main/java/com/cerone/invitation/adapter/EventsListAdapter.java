package com.cerone.invitation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.FavoriteTopic;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by peekay on 15/3/17.
 */

    public class EventsListAdapter extends BaseAdapter{
        private Context context;
        private List<FavoriteTopic> eventItems;

        public EventsListAdapter(Context context, List<FavoriteTopic> eventItems) {
            this.context = context;
            this.eventItems = eventItems;
        }

        @Override
        public int getCount() {
            return eventItems.size();
        }

        @Override
        public FavoriteTopic getItem(int position) {
            return eventItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    public void updateAdapter(List<FavoriteTopic> eventItems) {
        this.eventItems = eventItems;
        notifyDataSetChanged();
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.event_row_item, parent, false);
            FavoriteTopic event = getItem(position);
            TextView eventText = (TextView) row.findViewById(R.id.eventItem_name);
            ImageView eventImage = (ImageView)row.findViewById(R.id.eventItem_image);
            eventText.setText(event.getName());
            Picasso.with(context).load(event.getImagePath()).into(eventImage);
            if(event.isSelected()){
            eventText.setTextColor(Color.parseColor("#f06824"));
            eventImage.setColorFilter(Color.parseColor("#f06824"));
        }else{
                eventText.setTextColor(Color.parseColor("#696969"));
                eventImage.setColorFilter(Color.parseColor("#696969"));
        }
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


