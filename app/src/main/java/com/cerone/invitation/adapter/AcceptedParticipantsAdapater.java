package com.cerone.invitation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.ParticipantsActivity;
import com.cerone.invitation.helpers.CircleTransform;
import com.example.dataobjects.Invitee;
import com.example.dataobjects.Invitees;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by adarsht on 07/04/17.
 */

public class AcceptedParticipantsAdapater extends RecyclerView.Adapter<AcceptedParticipantsAdapater.MyImageViewHolder> {
    static Context context;
    View mainView;
    public static int CELL_HEIGHT = 120;
    public static int PROFILE_IMAGE_WIDTH = 90;
    List<Invitees> allInvitees;
    int eventId;



    public View getMainView() {
        return mainView;
    }

    public AcceptedParticipantsAdapater(Context context, List<Invitees> allInvitees, int eventId) {
        this.context = context;
        this.allInvitees = allInvitees;
        this.eventId = eventId;
    }

    @Override
    public MyImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accepted_participants_details_layout, parent, false);
        MyImageViewHolder holder = new MyImageViewHolder(view);
        mainView = view;
        return holder;
    }

    @Override
    public void onBindViewHolder(MyImageViewHolder holder, int position) {
        if(allInvitees!= null) {
            holder.timeToReachEvent.setText( allInvitees.get(position).getTitle());
            addParticipants(holder, allInvitees.get(position).getInviteesList().size(), position);
        }
    }

    private void addParticipants(final MyImageViewHolder holder, final int cout, final int position) {
        holder.participantsInfoView.removeAllViews();
        holder.participantsInfoView.post(new Runnable()
        {
            @Override
            public void run()
            {
                int width = holder.participantsInfoView.getWidth();
                Log.d("participantsInfoView","participantsInfoView widht "+width+" count "+cout);
                updateData(holder,cout,width, position);
            }
        });
    }

    public void updateData(MyImageViewHolder holder, int count, int layoutWidth, int position) {
        int maxCount = 6; //(layoutWidth/PROFILE_IMAGE_WIDTH);
        int remainingCount = (maxCount>count)?0:(count-(maxCount-1));
        Log.d("participantsInfoView","maxCount  "+maxCount+" count "+count+" remainingCount "+remainingCount+" layoutWidth "+layoutWidth);
        if(allInvitees!= null) {
            List<Invitee> inviteesList = allInvitees.get(position).getInviteesList();
            if (inviteesList!=null) {
                for (int i = 0; i < count; i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.profile_image_layout, null, false);
                    ImageView image = (ImageView) view.findViewById(R.id.profileImage);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =  new Intent(context, ParticipantsActivity.class);
                            intent.putExtra("eventId", eventId);
                            intent.putExtra("title", "Invitees");
                            context.startActivity(intent);
                        }
                    });
                    Picasso.with(context).load(inviteesList.get(i).getImage()).placeholder(R.drawable.logo).transform(new CircleTransform()).into(image);
                    boolean showRemaining = (i == (maxCount - 1));
                    Log.d("showRemaining", showRemaining+"");

                    if (showRemaining) {
                        image.setVisibility(View.GONE);
                        TextView moreCount = (TextView) view.findViewById(R.id.moreCount);
                        moreCount.setText("" + remainingCount + "+");
                        moreCount.setVisibility(View.VISIBLE);

                        moreCount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent =  new Intent(context, ParticipantsActivity.class);
                                intent.putExtra("eventId", eventId);
                                intent.putExtra("title", "Invitees");
                                context.startActivity(intent);
                            }
                        });
                    }
                    holder.participantsInfoView.addView(view);
                    if (showRemaining) {
                        break;
                    }
                }
            }

        }
    }

    public void updateAdapter(List<Invitees> allInvitees) {
        this.allInvitees = allInvitees;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 4;
    }


    public static class MyImageViewHolder extends RecyclerView.ViewHolder {
        TextView timeToReachEvent;
        LinearLayout participantsInfoView;
        public MyImageViewHolder(View view) {
            super(view);
            timeToReachEvent = (TextView) view.findViewById(R.id.timeToReachEvent);
            participantsInfoView = (LinearLayout) view.findViewById(R.id.participants);
        }

    }
}
