package com.cerone.invitation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.FontTypes;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.ChatInfo;
import com.example.dataobjects.ChatMessage;
import com.example.utills.HTTPUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.n;

/**
 * Created by suzuki on 11-05-2016.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText, dateTime, userName,me;
   // private ImageView userImage;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    int otherUserID;
    FontTypes fontTypes;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    @Override
    public void addAll(Collection<? extends ChatMessage> collection) {
        chatMessageList.addAll(collection);
        super.addAll(collection);
    }

    @Override
    public void clear() {
        super.clear();
        chatMessageList.clear();
    }

    Context context;

    public ChatArrayAdapter(Context context, int textViewResourceId, int otherUserID) {
        super(context, textViewResourceId);
        this.context = context;
        this.otherUserID = otherUserID;
        fontTypes = new FontTypes(context);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ChatInfo chatInfo = InvtAppPreferences.getChatInfo();
        if (chatMessageObj.getFromID() == otherUserID) {
            row = inflater.inflate(R.layout.chat_view_left_side, parent, false);
            userName = (TextView) row.findViewById(R.id.chat_user_name);
            if(chatInfo!= null && chatInfo.getName()!= null && !chatInfo.getName().trim().isEmpty()) {
                userName.setText(chatInfo.getName());
            }
        } else {
            row = inflater.inflate(R.layout.chat_view_right_side, parent, false);
            me = (TextView) row.findViewById(R.id.me);

        }
       // userImage = (ImageView) row.findViewById(R.id.user_image);

        chatText = (TextView) row.findViewById(R.id.msgr);
        dateTime = (TextView) row.findViewById(R.id.date_time);
        chatText.setText(chatMessageObj.getMessage());


      /*  if (chatInfo.getImageUrl() != null && !chatInfo.getImageUrl().isEmpty()) {

            Picasso.with(context).load(chatInfo.getImageUrl()).into(new Target() {

                @Override
                public void onPrepareLoad(Drawable arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                    Bitmap roundedCornerBitmap = HttpUtils.getRoundedCornerBitmap(bitmap, 96);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    userImage.setImageBitmap(roundedCornerBitmap);
                }

                @Override
                public void onBitmapFailed(Drawable arg0) {
                    // TODO Auto-generated method stub

                }
            });
        }*/
        // String dateTimeText = chatMessageObj.getDate()!=null ? HttpUtils.getFormatedDate(chatMessageObj.getDate()) + " "+ HTTPUtils.getFormatedTime(chatMessageObj.getDate()) :"";
        dateTime.setText(HTTPUtils.convertDateToString(chatMessageObj.getDate()));
        fontTypes.setTypeface(chatText, dateTime,userName,me);
        return row;
    }
}
