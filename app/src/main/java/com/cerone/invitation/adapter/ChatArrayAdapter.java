package com.cerone.invitation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.FontTypes;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.ChatInfo;
import com.example.dataobjects.ChatMessage;
import com.example.utills.HTTPUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

import static com.cerone.invitation.R.id.cameraIcon;

/**
 * Created by suzuki on 11-05-2016.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView dateTime, userName,me;
    EmojiconTextView chatText;

   // private ImageView userImage;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    int ownerId;
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

    public ChatArrayAdapter(Context context, int textViewResourceId, int ownerId) {
        super(context, textViewResourceId);
        this.context = context;
        this.ownerId = ownerId;
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
        Log.d("ChatArrayAdapter",chatMessageObj.getFromID()+" vs "+ ownerId);
        if (chatMessageObj.getFromID() != ownerId) {
            row = inflater.inflate(R.layout.chat_view_left_side, parent, false);
            userName = (TextView) row.findViewById(R.id.chat_user_name);
            if(chatInfo!= null && chatInfo.getName()!= null && !chatInfo.getName().trim().isEmpty()) {
                userName.setText(chatInfo.getName());
            }else{
                userName.setText(chatMessageObj.getUserName());
            }
            ImageView profileImage = (ImageView) row.findViewById(R.id.participantImage);
            String image = chatMessageObj.getUserImage();
            Picasso.with(this.context).load((image==null ||image.isEmpty())?"sfd":chatMessageObj.getUserImage()).placeholder(R.drawable.logo).transform(new CircleTransform()).into(profileImage);

        } else {
            row = inflater.inflate(R.layout.chat_view_right_side, parent, false);
            me = (TextView) row.findViewById(R.id.me);

        }
       // userImage = (ImageView) row.findViewById(R.id.user_image);

        chatText = (EmojiconTextView) row.findViewById(R.id.msgr);
        dateTime = (TextView) row.findViewById(R.id.date_time);
        chatText.setText(StringEscapeUtils.unescapeJava(chatMessageObj.getMessage()));
        dateTime.setText(HTTPUtils.convertDateToString(chatMessageObj.getDate()));
        fontTypes.setTypeface(chatText, dateTime,userName,me);
        return row;
    }
}
