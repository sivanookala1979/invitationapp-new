package com.cerone.invitation.fragement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.chat.BaseChatActivity;
import com.cerone.invitation.activities.chat.IntraChatActivity;
import com.cerone.invitation.adapter.AllChatDetailsAdapter;
import com.cerone.invitation.adapter.ChatArrayAdapter;
import com.cerone.invitation.helpers.ActivityCommunicator;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.service.MyGcmListenerService;
import com.example.dataobjects.ChatMessage;
import com.example.dataobjects.ChatRoom;
import com.example.dataobjects.Event;
import com.example.syncher.IntraChatSyncher;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static com.cerone.invitation.R.id.rootView;

/**
 * Created by adarsht on 09/03/17.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener{

    ListView chatMessagesView;
    ChatArrayAdapter chatMessagesAdapter;
    EmojIconActions emojIcon;
    EmojiconEditText chatMessageTextView;
    View rootView;
    ImageView emojiButton;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String CHAT_MESSAGE_RECEIVED = "chatMessageReceived";
    Integer user_id;
    Integer chatRoomId;
    public static boolean isActive;
    Event event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        event = InvtAppPreferences.getEventDetails();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(CHAT_MESSAGE_RECEIVED)) {
                    if (isRelevantMessage(intent)) {
                        handlePushNotification(intent.getStringExtra("message"), Integer.parseInt(intent.getStringExtra("from_user_id")),intent.getStringExtra("user_name"));
//                        pushNotification.putExtra("is_group", data.getString("is_group"));
//                        pushNotification.putExtra("event_id", data.getString("event_id"));
//                        pushNotification.putExtra("user_name", data.getString("user_name"));
                    }else {
                        Log.d("ChatFragment", "Not relavent message");
                    }
                }
            }
        };
        chatMessagesView = (ListView) view.findViewById(R.id.chat_messages);
        chatMessageTextView = (EmojiconEditText) view.findViewById(R.id.chat_message_text);
        emojiButton = (ImageView) view.findViewById(R.id.chat_add);
        rootView = view.findViewById(R.id.rootView);
        emojIcon = new EmojIconActions(getActivity(), rootView, chatMessageTextView, emojiButton);
        emojIcon.ShowEmojIcon();
        chatMessagesAdapter = new ChatArrayAdapter(getContext(), R.layout.chat_view_right_side, InvtAppPreferences.getOwnerId());
        chatMessagesView.setAdapter(chatMessagesAdapter);
        chatMessagesAdapter.clear();
        ImageView sendChatMessageButton = (ImageView) view.findViewById(R.id.send_chat_message);
        sendChatMessageButton.setOnClickListener(this);
        if(event.isAccepted()) {
            loadChatMessages();
        }else{
            chatMessageTextView.setEnabled(false);
        }
        return view;
    }
    void loadChatMessages() {
        new InvtAppAsyncTask(getActivity()) {
            List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();

            @Override
            public void process() {
                chatMessages = getChatMessages();
            }

            @Override
            public void afterPostExecute() {
                chatMessagesAdapter.addAll(chatMessages);
                chatMessagesAdapter.notifyDataSetChanged();
                chatMessagesView.smoothScrollToPosition(chatMessagesAdapter.getCount() - 1);
            }
        }.execute();

    }
    protected boolean isRelevantMessage(Intent intent) {
        return intent.hasExtra("event_id") && Integer.parseInt(intent.getStringExtra("event_id")) == InvtAppPreferences.getEventDetails().getEventId();
    }
    protected void handlePushNotification(String message, int fromID,String userName) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(message);
        chatMessage.setFromID(fromID);
        chatMessage.setDate(new Date());
        chatMessage.setUserName(userName);
        chatMessagesAdapter.add(chatMessage);
        chatMessagesAdapter.notifyDataSetChanged();
        chatMessagesView.smoothScrollToPosition(chatMessagesAdapter.getCount() - 1);
    }
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_chat_message:
                final String chatMessage = StringEscapeUtils.escapeJava(chatMessageTextView.getText().toString());
                if (chatMessage != null && chatMessage.length() > 0) {
                    chatMessageTextView.setText("");
                    // TODO - Should pass the current user ID instead of -1
                    handlePushNotification(StringEscapeUtils.unescapeJava(chatMessage), InvtAppPreferences.getOwnerId(),"me");
                    new InvtAppAsyncTask(getActivity()) {

                        @Override
                        public void process() {
                            sendMessage(chatMessage);
                        }

                        @Override
                        public void afterPostExecute() {
                        }
                    }.execute();
                    break;
                }
        }
    }
    protected void sendMessage(String chatMessage) {
        new IntraChatSyncher().sendMessageToGroup(event.getEventId(), chatMessage);
    }

    protected List<ChatMessage> getChatMessages() {
        return new IntraChatSyncher().getGroupChat(event.getEventId());
    }
    protected void initializeChat() {
        user_id = getActivity().getIntent().getIntExtra("UserId", 0);
        chatRoomId = getActivity().getIntent().getIntExtra("ChatRoomId", 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
        MyGcmListenerService.chatList.clear();
        MyGcmListenerService.notificationChatCount = 0;
        Log.d("ChatFragment", "onResume "+ isActive+" current id "+InvtAppPreferences.getOwnerId());
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(CHAT_MESSAGE_RECEIVED));
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        Log.d("ChatFragment", "onPause "+ isActive+" current id "+InvtAppPreferences.getOwnerId());

    }
}
