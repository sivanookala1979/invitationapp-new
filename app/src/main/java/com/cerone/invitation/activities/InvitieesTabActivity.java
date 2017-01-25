/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.activities;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.cerone.invitation.R;
import com.cerone.invitation.adapter.StatusTabAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author peekay
 * @version 1.0, 03-Feb-2016
 */
public class InvitieesTabActivity extends AppCompatActivity implements View.OnClickListener {

    ActionBar actionBar;
    ViewPager viewPager;
    StatusTabAdapter statusTabAdapter;
    List<? extends Fragment> participantsList = new ArrayList();

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView titleText = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageButton backButton = (ImageButton) toolbar.findViewById(R.id.toolbar_back_image);
        backButton.setOnClickListener(this);
        toolbar.findViewById(R.id.action_layout).setOnClickListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Checked In"));
        tabLayout.addTab(tabLayout.newTab().setText("Not Going"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        statusTabAdapter = new StatusTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(statusTabAdapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabReselected(TabLayout.Tab arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO Auto-generated method stub
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_layout :
            case R.id.toolbar_back_image :
                this.finish();
                break;
            default :
                break;
        }
    }
}
