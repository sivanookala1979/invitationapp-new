package com.cerone.invitation.adapter;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.FontTypes;
import com.example.dataobjects.Notification;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class NotificationAdapter extends BaseAdapter {
    Context context;
    int resourceId;
    List<Notification> pushNotifications;
    FontTypes fontTypes;

    public NotificationAdapter(Context context, int resourceId,
                               List<Notification> list) {
        this.context = context;
        this.resourceId = resourceId;
        this.pushNotifications = list;
        fontTypes = new FontTypes(context);
    }

    @Override
    public int getCount() {

        return pushNotifications.size();
    }

    @Override
    public Notification getItem(int position) {

        return pushNotifications.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notification_item, parent, false);

        TextView message_type_text = (TextView) view
                .findViewById(R.id.message_type_text);
        TextView message = (TextView) view.findViewById(R.id.message_text);
        TextView messageTime = (TextView) view.findViewById(R.id.message_time);
        ImageView imageView = (ImageView) view.findViewById(R.id.optionIcon);
        if (pushNotifications.get(position).getImage() != null && !pushNotifications.get(position).getImage().isEmpty()) {
            Picasso.with(context).load(pushNotifications.get(position).getImage()).into(imageView);
        }
        message.setText(pushNotifications.get(position).getContent() + "");
        message_type_text.setText(pushNotifications.get(position).getCategoryName());

        messageTime.setText(pushNotifications.get(position).getSpaceCreationDate());
        //Date receivedTime = pushNotifications.get(position).getSpaceCreationDate();

		/*String dateTime[] =new String[2];
        dateTime[0] = convertDateToString.substring(0,11);
		dateTime[1] = convertDateToString.substring(11,convertDateToString.length());*/
		/*try {
			getMonthDayAgo(dateTime, messageTime);
		} catch (java.text.ParseException e) {

			e.printStackTrace();
		}
*/
        fontTypes.setTypeface(message, message_type_text);
        return view;
    }

    void getMonthDayAgo(String[] formattedDate, TextView textView)
            throws java.text.ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date now = new Date();
        Date past = null;
        try {
            past = formatter.parse(formattedDate[0]);
            System.out.println(past);
            System.out.println(formatter.format(past));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) == 0) {
            textView.setText(formattedDate[1]);
        } else if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) == 1) {
            textView.setText("Yesterday");
        } else {
            // textView.setText(daysAgo + "daysago");
            textView.setText(formattedDate[0]);
        }

    }
}
