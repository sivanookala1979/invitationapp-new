package com.cerone.invitation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.Event;
import com.example.utills.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.cerone.invitation.R.id.acceptCount;
import static com.cerone.invitation.R.id.eventName;

/**
 * Created by adarsht on 08/03/17.
 */

public class RecycleEventAdapter extends RecyclerView.Adapter<RecycleEventAdapter.MyViewHolder> {
    Context context;
    List<Event> events = new ArrayList<Event>();
    boolean displayTag;
    int imageResources[] = {
            R.drawable.event_picture,
            R.drawable.dinner,
            R.drawable.music};
    public RecycleEventAdapter(Context context, int resource, List<Event> events, boolean displayTag) {
        this.context = context;
        this.events = events;
        this.displayTag = displayTag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_home_event_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event event = events.get(position);
        holder.imageView.setBackgroundResource(imageResources[position % 3]);
        holder.eventName.setText(event.getName());
        holder.eventDate.setText(StringUtils.formatDateAndTime(event.getStartDateTime(), 5));
        holder.acceptCount.setText(event.getAcceptedCount() + "");
        holder.checkInCount.setText(event.getCheckedInCount() + "");
        holder.total.setText(event.getInviteesCount() + "");
        if (displayTag) {
            int ownerId = InvtAppPreferences.getOwnerId();
            holder.tag.setVisibility(View.VISIBLE);
            if (event.getOwnerId() == ownerId) {
                holder.tag.setBackgroundResource(R.color.green_light);
            } else {
                holder.tag.setBackgroundResource(R.color.red_light);
            }
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void updateEventsList(List<Event> newEventsList) {
        events = newEventsList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName,eventDate,eventdescription,acceptCount,checkInCount,total,tag;
        ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
             eventName = (TextView) view.findViewById(R.id.eventName);
             eventDate = (TextView) view.findViewById(R.id.startDate);
             eventdescription = (TextView) view.findViewById(R.id.eventDescription);
             imageView = (ImageView) view.findViewById(R.id.eventPicture);
             acceptCount = (TextView) view.findViewById(R.id.eventAcceptCount);
             checkInCount = (TextView) view.findViewById(R.id.checkedinCount);
             total = (TextView) view.findViewById(R.id.totalInvitees);
            tag = (TextView) view.findViewById(R.id.role);
        }
    }
}
