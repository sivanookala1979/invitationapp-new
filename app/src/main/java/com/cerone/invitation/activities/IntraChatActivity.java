package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dataobjects.ChatMessage;

import java.util.List;

public class IntraChatActivity extends BaseChatActivity {
	Integer user_id;
	Integer chatRoomId;
	String userName;
	static boolean isActive;
	
	@Override
	protected void initializeChat() {
		user_id = getIntent().getIntExtra("UserId", 0);
		chatRoomId = getIntent().getIntExtra("ChatRoomId", 0);
		Bundle extras = getIntent().getExtras();
		userName = extras.getString("UserName");

	}
	
	@Override
	protected boolean isRelevantMessage(Intent intent) {
		return intent.hasExtra("from_user_id") && Integer.parseInt(intent.getStringExtra("from_user_id")) == user_id;
	}

	@Override
	protected void sendMessage(String chatMessage) {
		//new IntraChatSyncher().sendMessage(user_id, chatMessage);
	}

	@Override
	protected List<ChatMessage> getChatMessages() {
		//return new IntraChatSyncher().getIntraChat(user_id);
		return null;
	}

	@Override
	protected int getOtherUserID() {
		return user_id;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActive = true;
		MyGcmListenerService.chatList.clear();
		MyGcmListenerService.notificationChatCount = 0;
	}
	@Override
	protected void onPause() {
		super.onPause();
		isActive = false;
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
}
