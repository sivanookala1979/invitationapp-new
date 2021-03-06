package com.cerone.invitation.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerone.invitation.MyGroupsActivity;
import com.cerone.invitation.R;
import com.cerone.invitation.UserProfile;
import com.cerone.invitation.activities.chat.AllChatsActivity;
import com.cerone.invitation.adapter.HomeEventAdapter;
import com.cerone.invitation.adapter.PagerAdapter;
import com.cerone.invitation.fcm.RegistrationIntentService;
import com.cerone.invitation.fragement.HomeFoldingFragment;
import com.cerone.invitation.fragement.PublicHomeEventsFragment;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.HomeScreenCommunicator;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.example.dataobjects.ScreenTab;
import com.example.dataobjects.User;
import com.example.syncher.UserSyncher;
import com.google.android.gms.iid.InstanceID;
import com.squareup.picasso.Picasso;


public class HomeScreenActivity extends BaseActivity implements OnClickListener, NavigationView.OnNavigationItemSelectedListener, HomeScreenCommunicator
{

    FloatingActionButton floatingActionButton;
    HomeEventAdapter adapter;
    int ownerId = 0;
    User profile;
    TextView userName;
    ImageView userImage, eventFilterIcon, invitationFilterIcon, searchImage;
    ViewPager viewPager;
    TabLayout mPagerSlidingTabStrip;
    PagerAdapter mPagerAdapter;
    TextView screenTitle;
    LinearLayout eventFilter, invitationsFilter;
    LinearLayout filteringLayout;
    NavigationView navigationView;
    boolean showPublicScreen;
    boolean searchSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_menu_activity);
        InvtAppPreferences.setScreenRefreshStatus(false);
        InvtAppPreferences.setProfileUpdatedStatus(false);
        showPublicScreen = getIntent().getBooleanExtra("showPublicScreen", false);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);
        floatingActionButton.setVisibility(View.VISIBLE);
        screenTitle = (TextView) findViewById(R.id.toolbar_title);
        eventFilterIcon = (ImageView) findViewById(R.id.eventFilterIcon);
        searchImage = (ImageView) findViewById(R.id.image_search);
        invitationFilterIcon = (ImageView) findViewById(R.id.invitationFilterIcon);
        eventFilter = (LinearLayout) findViewById(R.id.radio_events);
        invitationsFilter = (LinearLayout) findViewById(R.id.radio_invitations);
        filteringLayout = (LinearLayout) findViewById(R.id.filteringMenu);
        filteringLayout.setVisibility(View.VISIBLE);
        Log.d("Token", InvtAppPreferences.getAccessToken());
        ownerId = InvtAppPreferences.getOwnerId();
        Log.d("Owner id", ownerId + "");
        createLeftMenu();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        checkUserPermissions();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        userImage = (ImageView) hView.findViewById(R.id.nav_userImage);
        userName = (TextView) hView.findViewById(R.id.txt_nav_userName);
        navigationView.setNavigationItemSelectedListener(this);
        floatingActionButton.setOnClickListener(this);
        mPagerSlidingTabStrip = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        screenTabs = getScreenTabs(0);
        eventFilter.setSelected(true);
        invitationsFilter.setSelected(true);
        eventFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eventFilter.isSelected()) {
                    eventFilter.setSelected(true);
                } else {
                    if (invitationsFilter.isSelected()) {
                        eventFilter.setSelected(false);
                    } else {
                        eventFilter.setSelected(false);
                        invitationsFilter.setSelected(true);
                    }
                }
                applyFilters();
            }
        });
        invitationsFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!invitationsFilter.isSelected()) {
                    invitationsFilter.setSelected(true);
                } else {
                    if (eventFilter.isSelected()) {
                        invitationsFilter.setSelected(false);
                    } else {
                        invitationsFilter.setSelected(false);
                        eventFilter.setSelected(true);
                    }
                }
                applyFilters();
            }
        });

        searchImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchSelected){
                    searchSelected = true;
                    showSearch(searchSelected);
                }else{
                    searchSelected = false;
                    showSearch(searchSelected);
                }
            }
        });
        mPagerSlidingTabStrip.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() != null) {
                    updateTabSelectinOptions((Integer) tab.getTag());
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
        if (MobileHelper.hasNetwork(getApplicationContext(), HomeScreenActivity.this, " to get Home Screen", null)) {
            applyFilters();
            updateProfileImageAndName();
            updateNavigationMenuOptions(false);
            activateService();
            createTabViews();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InvtAppPreferences.isProfileUpdated()) {
            updateProfileImageAndName();
            InvtAppPreferences.setProfileUpdatedStatus(false);
        }
    }

    private void createTabViews() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), screenTabs);
        viewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setupWithViewPager(viewPager);
        int tab = 0;
        if(showPublicScreen) {
            filteringLayout.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            searchImage.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(1);
            updateNavigationMenuOptions(true);
            tab = 1;
        }

            for (int i = 0; i < mPagerSlidingTabStrip.getTabCount(); i++) {
                LinearLayout tabLayout = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, null);
                TextView title = (TextView) tabLayout.findViewById(R.id.tabTitle);
                ImageView image = (ImageView) tabLayout.findViewById(R.id.tabIcon);
                ScreenTab screenTab = screenTabs.get(i);
                if (i == tab) {
                    title.setText(screenTab.getName());
                    image.setImageResource(screenTab.getImageResource());
                    title.setTextColor(Color.WHITE);
                    image.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                } else {
                    title.setText(screenTab.getName());
                    image.setImageResource(screenTab.getImageResource());
                    image.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                }
                tabViews.add(tabLayout);
                mPagerSlidingTabStrip.getTabAt(i).setTag(i);
                mPagerSlidingTabStrip.getTabAt(i).setCustomView(tabLayout);
            }

    }

    private void applyFilters() {
        int color = getResources().getColor(R.color.happening_red);
        eventFilterIcon.setColorFilter(eventFilterIcon.isSelected() ? color : Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        invitationFilterIcon.setColorFilter(invitationFilterIcon.isSelected() ? color : Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((HomeFoldingFragment) page).updateData(eventFilter.isSelected(), invitationsFilter.isSelected());
        }
    }

    private void showSearch(boolean select){
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 1 && page != null) {
            ((PublicHomeEventsFragment) page).updateData(select);
        }
    }

    private void createLeftMenu() {
        addToolbarView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void updateProfileImageAndName() {
        new InvtAppAsyncTask(this) {

            @Override
            public void process() {

                UserSyncher syncher = new UserSyncher();
                profile = syncher.getUserdetails();
            }

            @Override
            public void afterPostExecute() {
                if (profile != null) {
                    if (profile.getImage() != null && !profile.getImage().isEmpty() && profile.getUserName() != null) {
                        Picasso.with(getApplicationContext()).load(profile.getImage()).transform(new CircleTransform()).into(userImage);
                        userImage.setOnClickListener(null);
                        userName.setText(profile.getUserName());
                    } else if (profile.getUserName() != null && profile.getImage().isEmpty()) {
                        userName.setText(profile.getUserName());
                    } else if (profile.getUserName() == null && profile.getImage() != null && !profile.getImage().isEmpty()) {
                        Picasso.with(getApplicationContext()).load(profile.getImage()).transform(new CircleTransform()).into(userImage);
                    }
                }
            }

        }.setShowProgress(false).execute();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                InvtAppPreferences.setEventDetails(null);
                startActivity(new Intent(this, CreateNewEventActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        doExit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                intent = new Intent(this, HomeScreenActivity.class);
                break;
            case R.id.nav_profile:
                intent = new Intent(this, UserProfile.class);
                break;
            case R.id.nav_eventsHistory:
                intent = new Intent(this, EventsHistoryActivity.class);
                break;
            case R.id.nav_changeCity:
                intent = new Intent(this, PublicHomeActivity.class);
                break;
            case R.id.nav_myChat:
                intent = new Intent(this, AllChatsActivity.class);
                break;
            case R.id.nav_myGroup:
                intent = new Intent(this, MyGroupsActivity.class);
                break;
            case R.id.nav_myNotification:
                intent = new Intent(this, NotificationsActivity.class);
                break;
            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.nav_logout:
                try {
                    InstanceID.getInstance(getApplicationContext()).deleteInstanceID();
                } catch (Exception ex) {
                    Log.e("InstanceID", ex.getMessage(), ex);
                }
                InvtAppPreferences.setLoggedIn(false);
                InvtAppPreferences.reset();
                Intent logoutIntent = new Intent(HomeScreenActivity.this, SignInActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                break;
        }
        if (R.id.logout != id && intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left_out_animation, R.anim.right_to_left_in_animation);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateTabSelectinOptions(int index) {

        if (index == 0) {
            screenTitle.setText("Events");
            floatingActionButton.setVisibility(View.VISIBLE);
            filteringLayout.setVisibility(View.VISIBLE);
            searchImage.setVisibility(View.GONE);
            updateNavigationMenuOptions(false);
        } else {
            if(!InvtAppPreferences.getEventFilters().isValid()) {
                screenTitle.setText("Select City");
                searchImage.setVisibility(View.GONE);
            }else{
                searchImage.setVisibility(View.VISIBLE);
            }
            floatingActionButton.setVisibility(View.GONE);
            filteringLayout.setVisibility(View.GONE);
            updateNavigationMenuOptions(true);
        }
    }

    @Override
    public void updateServices(boolean status) {
        if(status){
            activateService();
        }
    }

    public void updateNavigationMenuOptions(boolean isPublicFragment) {
        Menu menu = navigationView.getMenu();
        if(isPublicFragment){
            menu.findItem(R.id.nav_changeCity).setVisible(true);
        }else{
            menu.findItem(R.id.nav_changeCity).setVisible(false);
        }
        Log.d("Adarsh","Status info "+isPublicFragment);
    }
}