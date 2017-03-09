package com.cerone.invitation.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.PagerAdapter;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.squareup.picasso.Picasso;

import static com.cerone.invitation.R.id.eventName;
import static com.cerone.invitation.R.id.view;
import static com.google.android.gms.analytics.internal.zzy.o;

/**
 * Created by adarsht on 09/03/17.
 */

public class EventDetailsActivity extends BaseActivity {
    ViewPager viewPager;
    TabLayout mPagerSlidingTabStrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_layout);
        addToolbarView();
        TextView eventName = (TextView) findViewById(R.id.toolbar_title);
        eventName.setText(InvtAppPreferences.getEventDetails().getName());
        mPagerSlidingTabStrip = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setupWithViewPager(viewPager);
        for (int i = 0; i < mPagerSlidingTabStrip.getTabCount(); i++) {
            LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, null);
            TextView title = (TextView) tabTwo.findViewById(R.id.tabTitle);
            ImageView image = (ImageView) tabTwo.findViewById(R.id.tabIcon);
            if(i==0) {
                image.setBackgroundResource(R.drawable.event);
                title.setText("Event");
            }else{
                title.setText("Chat");
            }
            mPagerSlidingTabStrip.getTabAt(i).setTag(i);
            mPagerSlidingTabStrip.getTabAt(i).setCustomView(tabTwo);
        }
    }
}
