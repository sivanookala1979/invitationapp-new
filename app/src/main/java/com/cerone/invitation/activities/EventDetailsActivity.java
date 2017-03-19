package com.cerone.invitation.activities;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.dataobjects.ScreenTab;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarsht on 09/03/17.
 */

public class EventDetailsActivity extends BaseActivity implements ActivityCommunicator {

    ViewPager viewPager;
    TabLayout mPagerSlidingTabStrip;
    Event eventDetails;
    PagerAdapter mPagerAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multli_tabs_template);
        addToolbarView();
        InvtAppPreferences.setScreenRefreshStatus(false);
        TextView eventName = (TextView) findViewById(R.id.toolbar_title);
        eventDetails = InvtAppPreferences.getEventDetails();
        eventName.setText(eventDetails.getName());
        mPagerSlidingTabStrip = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        screenTabs = getScreenTabs(1);
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void createTabViews() {
       // updateTabsCount();
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), screenTabs);
        viewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setupWithViewPager(viewPager);
        for (int i = 0; i < mPagerSlidingTabStrip.getTabCount(); i++) {
            LinearLayout tabLayout = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, null);
            TextView title = (TextView) tabLayout.findViewById(R.id.tabTitle);
            ImageView image = (ImageView) tabLayout.findViewById(R.id.tabIcon);
            ScreenTab screenTab = screenTabs.get(i);
            if (i == 0) {
                image.setImageResource(screenTab.getImageResource());
                title.setTextColor(Color.WHITE);
                title.setText(screenTab.getName());
            } else {
                title.setText(screenTab.getName());
            }
            tabViews.add(tabLayout);
            mPagerSlidingTabStrip.getTabAt(i).setTag(i);
            mPagerSlidingTabStrip.getTabAt(i).setCustomView(tabLayout);
        }
    }

    private void updateTabsCount() {
        if (eventDetails.getOwnerId() != InvtAppPreferences.getOwnerId()) {
            if (!eventDetails.isAccepted()) {
                screenTabs.remove(1);
            }
        }
    }

    @Override
    public void enableChatView(boolean enableChat) {
        InvtAppPreferences.setScreenRefreshStatus(true);
        eventDetails.setAccepted(true);
        createTabViews();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("EventDetails Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
