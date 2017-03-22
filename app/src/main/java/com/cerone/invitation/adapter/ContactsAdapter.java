package com.cerone.invitation.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.User;
import com.github.akashandroid90.imageletter.MaterialLetterIcon;

import java.util.List;

/**
 * Created by adarsh on 12/26/16.
 */

public class ContactsAdapter extends ArrayAdapter<User> {
    Context context;
    int resourceId;
    List<User> referFriends;
    private final TypedArray mColors;


    public ContactsAdapter(Context context, int resourceId, List<User> referFriends) {
        super(context, resourceId, referFriends);
        this.context = context;
        this.resourceId = resourceId;
        this.referFriends = referFriends;
        final Resources res = context.getResources();
        mColors = res.obtainTypedArray(R.array.letter_tile_colors);
    }

    @Override
    public int getCount() {
        return referFriends.size();
    }

    @Override
    public User getItem(int position) {
        return referFriends.get(position);
    }

    public void updateAdapter(List<User> referFriends) {
        this.referFriends = referFriends;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.contact_layout, parent, false);
        User contactInfo = getItem(position);
        TextView userName = (TextView) row.findViewById(R.id.userName);
        LinearLayout superView = (LinearLayout) row.findViewById(R.id.layout_contacts);
        superView.setBackgroundColor(contactInfo.isSelected() ? Color.parseColor("#A9A9A9") :Color.parseColor("#ffffff"));
        MaterialLetterIcon materialLetterIcon = (MaterialLetterIcon) row.findViewById(R.id.nameIndicator);
        materialLetterIcon.setLetter("A");
        materialLetterIcon.setShapeColor(mColors.getColor(position%8, Color.BLACK));
        if (!contactInfo.getUserName().isEmpty()) {
            materialLetterIcon.setLetter(""+contactInfo.getUserName().substring(0, 1));
        }
        TextView mobileNumber = (TextView) row.findViewById(R.id.contactNumber);
        userName.setText("" + contactInfo.getUserName());
        mobileNumber.setText("" + contactInfo.getPhoneNumber());
        return row;

    }
}
