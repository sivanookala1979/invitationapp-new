package com.cerone.invitation.activities;

import android.content.Intent;

import com.example.dataobjects.ChatMessage;

import java.util.List;

public class SupportChatActivity extends BaseChatActivity {

    private static final int SUPPORT_USER_ID = -999;
    static boolean isActive;

    @Override
    protected List<ChatMessage> getChatMessages() {
        //return new SupportChatSyncher().getSupportChat();
        return null;
    }

    @Override
    protected boolean isRelevantMessage(Intent intent) {
        return intent.hasExtra("support_message") && Boolean.parseBoolean(intent.getStringExtra("support_message"));
    }

    @Override
    protected void sendMessage(final String chatMessage) {
        //new SupportChatSyncher().sendMessage(chatMessage);
    }

    @Override
    protected void initializeChat() {
    }

    @Override
    protected int getOtherUserID() {
        return SUPPORT_USER_ID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        MyGcmListenerService.supportChatList.clear();
        MyGcmListenerService.supportNotificationCount = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }
}
