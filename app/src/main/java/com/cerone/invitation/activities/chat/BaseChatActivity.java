package com.cerone.invitation.activities.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.BaseActivity;
import com.cerone.invitation.adapter.ChatArrayAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.example.dataobjects.ChatMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by suzuki on 11-05-2016.
 */
public abstract class BaseChatActivity extends BaseActivity implements View.OnClickListener {
    ListView chatMessagesView;
    ChatArrayAdapter chatMessagesAdapter;
    TextView chatMessageTextView;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String CHAT_MESSAGE_RECEIVED = "chatMessageReceived";

    public BaseChatActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeChat();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(CHAT_MESSAGE_RECEIVED)) {
                    if (isRelevantMessage(intent)) {
                        handlePushNotification(intent.getStringExtra("message"), getOtherUserID());
                    }
                }
            }
        };
        setContentView(R.layout.activity_support_chat);
        addToolbarView();

        chatMessagesView = (ListView) findViewById(R.id.chat_messages);
        chatMessageTextView = (TextView) findViewById(R.id.chat_message_text);
        setFontType(R.id.chat_message_text);
        chatMessagesAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.chat_view_right_side, getOtherUserID());
        chatMessagesView.setAdapter(chatMessagesAdapter);
        chatMessagesAdapter.clear();
        ImageView sendChatMessageButton = (ImageView) findViewById(R.id.send_chat_message);
        sendChatMessageButton.setOnClickListener(this);

        loadChatMessages();
    }

    void loadChatMessages() {
        new InvtAppAsyncTask(BaseChatActivity.this) {
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

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(CHAT_MESSAGE_RECEIVED));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_chat_message:
                final String chatMessage = chatMessageTextView.getText().toString();
                if (chatMessage != null && chatMessage.length() > 0) {
                    chatMessageTextView.setText("");
                    // TODO - Should pass the current user ID instead of -1
                    handlePushNotification(chatMessage, -1);
                    new InvtAppAsyncTask(BaseChatActivity.this) {

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

    protected void handlePushNotification(String message, int fromID) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(message);
        chatMessage.setFromID(fromID);
        chatMessage.setDate(new Date());
        chatMessagesAdapter.add(chatMessage);
        chatMessagesAdapter.notifyDataSetChanged();
        chatMessagesView.smoothScrollToPosition(chatMessagesAdapter.getCount() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract void initializeChat();

    protected abstract void sendMessage(final String chatMessage);

    protected abstract List<ChatMessage> getChatMessages();

    protected abstract boolean isRelevantMessage(Intent intent);

    protected abstract int getOtherUserID();
}
