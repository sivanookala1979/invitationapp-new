package com.cerone.invitation.activities;

import android.os.Bundle;

import com.cerone.invitation.R;

public class ChatsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);
		addToolbarView();

	}
}
