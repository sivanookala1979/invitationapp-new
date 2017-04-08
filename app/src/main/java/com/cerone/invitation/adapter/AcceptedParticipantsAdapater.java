package com.cerone.invitation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerone.invitation.R;

import static com.cerone.invitation.R.id.view;

/**
 * Created by adarsht on 07/04/17.
 */

public class AcceptedParticipantsAdapater extends RecyclerView.Adapter<AcceptedParticipantsAdapater.MyImageViewHolder> {
    Context context;
    View mainView;
    public static int CELL_HEIGHT = 120;
    public static int PROFILE_IMAGE_WIDTH = 90;


    public View getMainView() {
        return mainView;
    }

    public AcceptedParticipantsAdapater(Context context) {
        this.context = context;
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
        int index = (position%3);
        switch(index){
            case 0:
                holder.timeToReachEvent.setText("5-10 min");
                addParticipants(holder,8);
                break;
            case 1:
                holder.timeToReachEvent.setText("10-20 min");
                addParticipants(holder,4);
                break;
            case 2:
                holder.timeToReachEvent.setText("20-30 min");
                addParticipants(holder,7);
                break;
        }
    }

    private void addParticipants(final MyImageViewHolder holder, final int cout) {
        holder.participantsInfoView.removeAllViews();
        holder.participantsInfoView.post(new Runnable()
        {
            @Override
            public void run()
            {
                int width = holder.participantsInfoView.getWidth();
                Log.d("participantsInfoView","participantsInfoView widht "+width);
                updateData(holder,cout,width);

            }
        });
    }
    public void updateData(MyImageViewHolder holder, int count, int layoutWidth) {
        int maxCount = (layoutWidth/PROFILE_IMAGE_WIDTH);
        int remainingCount = (maxCount>count)?0:(count-(maxCount-1));
        Log.d("participantsInfoView","maxCount  "+maxCount+" remainingCount "+remainingCount+" layoutWidth "+layoutWidth);

        for(int i=0;i<count;i++){
            View view = LayoutInflater.from(context).inflate(R.layout.profile_image_layout, null, false);
            ImageView profileImage = (ImageView)view.findViewById(R.id.profileImage);
            boolean showRemaining = (i==(maxCount-1));
            if(showRemaining) {
                profileImage.setVisibility(View.GONE);
                TextView moreCount = (TextView) view.findViewById(R.id.moreCount);
                moreCount.setText("" + remainingCount + "+");
                moreCount.setVisibility(View.VISIBLE);
            }
            holder.participantsInfoView.addView(view);
            if(showRemaining){
                break;
            }
        }
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
