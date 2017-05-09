package com.cerone.invitation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.PublicEvent;
import com.example.utills.StringUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import static com.example.utills.StringUtils.getFormatedDateFromServerFormatedDate;

/**
 * Created by peekay on 9/5/17.
 */

public class SimilarEventsAdapter extends RecyclerView.Adapter<SimilarEventsAdapter.MyViewHolder> {
    Context context;
    private List<PublicEvent> similarEvents;

    public SimilarEventsAdapter(Context context, List<PublicEvent> similarEvents) {
        this.context = context;
        this.similarEvents = similarEvents;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.public_similar_event_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PublicEvent publicEvent = similarEvents.get(position);
        holder.eventName.setText(publicEvent.getEventName());
        holder.eventAddress.setText(publicEvent.getAddress());
        try {
            holder.eventTimings.setText(StringUtils.getEventDateFormat(getFormatedDateFromServerFormatedDate(publicEvent.getStartRime())) +" "+ StringUtils.getEventTimeFormat(getFormatedDateFromServerFormatedDate(publicEvent.getStartRime()), getFormatedDateFromServerFormatedDate(publicEvent.getEndTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.entryFee.setText(publicEvent.getEventName());
        if(publicEvent.getImage()!= null && !publicEvent.getImage().trim().isEmpty()) {
            Picasso.with(context).load(publicEvent.getImage()).placeholder(R.drawable.event_picture).into(holder.eventIcon);
        }else {
            Picasso.with(context).load("java").placeholder(R.drawable.event_picture).into(holder.eventIcon);
        }    }

    @Override
    public int getItemCount() {
        return similarEvents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName, eventAddress, eventTimings, entryFee;
        ImageView eventIcon;

        public MyViewHolder(View view) {
            super(view);
            eventName = (TextView) view.findViewById(R.id.eventName);
            eventAddress = (TextView) view.findViewById(R.id.eventAddress);
            eventTimings = (TextView) view.findViewById(R.id.eventTimings);
            entryFee = (TextView) view.findViewById(R.id.entryFee);
            eventIcon = (ImageView) view.findViewById(R.id.eventIcon);

        }
    }
}
