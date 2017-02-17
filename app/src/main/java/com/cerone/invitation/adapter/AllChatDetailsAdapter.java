package com.cerone.invitation.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.FontTypes;
import com.example.dataobjects.ChatRoom;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AllChatDetailsAdapter extends ArrayAdapter<ChatRoom> {
    Context context;
    int resourceId;
    List<ChatRoom> chatRommList;
    FontTypes fontTypes;

    public AllChatDetailsAdapter(Context context, int resourceId, List<ChatRoom> chatRoomList) {
        super(context, resourceId);
        this.context = context;
        this.resourceId = resourceId;
        this.chatRommList = chatRoomList;
        fontTypes = new FontTypes(context);
    }

    @Override
    public int getCount() {

        return chatRommList.size();
    }

    @Override
    public ChatRoom getItem(int position) {

        return chatRommList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public void addAll(Collection<? extends ChatRoom> collection) {
        chatRommList.addAll(collection);
        super.addAll(collection);
    }

    @Override
    public void clear() {
        super.clear();
        chatRommList.clear();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.all_chats_row, parent, false);
        final ImageView picture = (ImageView) row.findViewById(R.id.person_image);
        if (chatRommList.get(position).getImage() != null && !chatRommList.get(position).getImage().isEmpty()) {
            Picasso.with(context).load(chatRommList.get(position).getImage()).transform(new CircleTransform()).into(picture);
        }
        TextView weightText = (TextView) row.findViewById(R.id.name_text);
        weightText.setText(chatRommList.get(position).getOtherUserName());
        TextView appxText = (TextView) row.findViewById(R.id.time_text);
        if (chatRommList.get(position).getLatestMsgDateTime() != null) {
            String dateTime = chatRommList.get(position).getLatestMsgDateTime();
            appxText.setText(dateTime + "");
            try {
                getMonthDayAgo(dateTime, appxText);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        TextView packageText = (TextView) row.findViewById(R.id.last_chat);
        packageText.setText(chatRommList.get(position).getLatestMessage());


        fontTypes.setTypeface(row.findViewById(R.id.name_text), row.findViewById(R.id.time_text),
                row.findViewById(R.id.last_chat));
        row.setOnClickListener(createOnClickListener(position, parent));
        return row;
    }

    public View.OnClickListener createOnClickListener(final int position,
                                                      final ViewGroup parent) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        };
    }

    void getMonthDayAgo(String formattedDate, TextView textView)
            throws java.text.ParseException {
        SimpleDateFormat readFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date now = new Date();
        Date past = null;
        String fdate = formattedDate.substring(0, formattedDate.length() - 5);
        String ftime = formattedDate.substring(formattedDate.length() - 6, formattedDate.length());
        try {
            past = readFormat.parse(formattedDate);
            System.out.println(past);
            System.out.println(readFormat.format(past));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) < 30) {
//            if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) == 0) {
//                textView.setText(ftime);
//            } else if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) == 1) {
//                textView.setText("Yesterday");
//            } else {
//                // textView.setText(daysAgo + "daysago");
//                String daysAgo = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + "days ago";
//                textView.setText(daysAgo);
//            }
//        } else {
//            long d1 = 0l;
//            long d2 = 0l;
//            d1 = past.getTime();
//            d2 = now.getTime();
//            long l = Math.abs((d1 - d2) / (1000 * 60 * 60 * 24));
//            String MonthsAgo = l / 30l + "months ago";
//            textView.setText(MonthsAgo);
//
//        }

    }

}
