package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.cerone.invitation.R;

import java.util.List;

public class ImageDialogAdapter extends BaseAdapter {

    Context context;
    int resourceId;
    List<String> galleryList;

    public ImageDialogAdapter(Context context, int resourceId, List<String> galleryList) {
        this.context = context;
        this.resourceId = resourceId;
        this.galleryList = galleryList;
    }

    @Override
    public int getCount() {
        return galleryList.size();
    }

    @Override
    public String getItem(int position) {
        return galleryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.gallery_title, parent, false);
        TextView title = (TextView) row.findViewById(R.id.title);
        title.setText(galleryList.get(position));
        return row;
    }
}
