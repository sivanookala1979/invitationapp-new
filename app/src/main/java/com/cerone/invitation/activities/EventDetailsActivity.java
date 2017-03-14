package com.cerone.invitation.activities;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.PagerAdapter;
import com.cerone.invitation.helpers.ActivityCommunicator;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsht on 09/03/17.
 */

public class EventDetailsActivity extends BaseActivity implements ActivityCommunicator {

    ViewPager viewPager;
    TabLayout mPagerSlidingTabStrip;
    List<LinearLayout> tabViews = new ArrayList<LinearLayout>();
    Event eventDetails;
    PagerAdapter mPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_layout);
        addToolbarView();
        InvtAppPreferences.setScreenRefreshStatus(false);
        TextView eventName = (TextView) findViewById(R.id.toolbar_title);
        eventDetails = InvtAppPreferences.getEventDetails();
        eventName.setText(eventDetails.getName());
        mPagerSlidingTabStrip = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        createTabViews();

        final Resources res = getApplicationContext().getResources();
        mPagerSlidingTabStrip.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() != null) {
                    LinearLayout tabLayout = tabViews.get((Integer) tab.getTag());
                    TextView title = (TextView) tabLayout.findViewById(R.id.tabTitle);
                    ImageView image = (ImageView) tabLayout.findViewById(R.id.tabIcon);
                    image.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    title.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getTag() != null) {
                    LinearLayout tabLayout = tabViews.get((Integer) tab.getTag());
                    TextView title = (TextView) tabLayout.findViewById(R.id.tabTitle);
                    title.setTextColor(Color.BLACK);
                    ImageView image = (ImageView) tabLayout.findViewById(R.id.tabIcon);
                    image.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createTabViews() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(),getTabsCount());
        viewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setupWithViewPager(viewPager);
        for (int i = 0; i < mPagerSlidingTabStrip.getTabCount(); i++) {
            LinearLayout tabLayout = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, null);
            TextView title = (TextView) tabLayout.findViewById(R.id.tabTitle);
            ImageView image = (ImageView) tabLayout.findViewById(R.id.tabIcon);
            if(i==0) {
                image.setImageResource(R.drawable.event);
                title.setTextColor(Color.WHITE);
                title.setText("Event");
            }else{
                title.setText("Chat");
            }
            tabViews.add(tabLayout);
            mPagerSlidingTabStrip.getTabAt(i).setTag(i);
            mPagerSlidingTabStrip.getTabAt(i).setCustomView(tabLayout);
        }
    }

    private int getTabsCount() {
        int count = 2;
        if(eventDetails.getOwnerId()!=InvtAppPreferences.getOwnerId()){
            if(!eventDetails.isAccepted()){
                count = 1;
            }
        }
        return count;
    }

    @Override
    public void enableChatView(boolean enableChat) {
        InvtAppPreferences.setScreenRefreshStatus(true);
        eventDetails.setAccepted(true);
        createTabViews();
    }
}
