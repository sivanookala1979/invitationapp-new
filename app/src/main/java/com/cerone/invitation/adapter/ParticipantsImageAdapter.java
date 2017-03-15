package com.cerone.invitation.adapter;

import android.content.Context;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.Event;
import com.example.utills.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.cerone.invitation.R.drawable.event;
import static com.cerone.invitation.R.drawable.events;
import static com.cerone.invitation.R.id.acceptCount;
import static com.cerone.invitation.R.id.eventName;

/**
 * Created by adarsht on 15/03/17.
 */

public class ParticipantsImageAdapter extends RecyclerView.Adapter<ParticipantsImageAdapter.MyImageViewHolder> {
    Context context;
    int imageResources[] = {
            R.drawable.event_picture,
            R.drawable.dinner,
            R.drawable.music};

    public ParticipantsImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_image_layout, parent, false);
        return new MyImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyImageViewHolder holder, int position) {
        Picasso.with(context).load("http://invtapp.cerone-software.com//assets/image/55/original/picture.png?1489483662").transform(new CircleTransform()).into(holder.profileImage);
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public static class MyImageViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;

        public MyImageViewHolder(View view) {
            super(view);
            profileImage = (ImageView) view.findViewById(R.id.profileImage);
        }
    }
}
