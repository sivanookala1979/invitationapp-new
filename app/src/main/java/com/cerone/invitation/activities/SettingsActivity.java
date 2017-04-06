package com.cerone.invitation.activities;

import android.os.Bundle;

import com.cerone.invitation.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addToolbarView();
        setFontType(R.id.switch_host, R.id.switch_guests, R.id.switch_invitation, R.id.switch_comments);
    }
}
