package com.cerone.invitation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cerone.invitation.fragement.ChatFragment;
import com.cerone.invitation.fragement.EventInfoFragment;
import com.cerone.invitation.fragement.HomeFoldingFragment;
import com.cerone.invitation.fragement.PublicHomeFragment;
import com.example.dataobjects.ScreenTab;

import java.util.List;


/**
 * Created by B Hemanth Reddy on 27/9/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    List<ScreenTab> screenTabs;

    public PagerAdapter(FragmentManager fm, List<ScreenTab> screenTabs) {
        super(fm);
        this.screenTabs = screenTabs;
    }

    @Override
    public int getCount() {
        return screenTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        ScreenTab screenTab = screenTabs.get(position);
        switch (screenTab.getIndex()) {
            case 1:
                return HomeFoldingFragment.newInstance();
            case 0:
                return PublicHomeFragment.newInstance();
            case 2:
                return EventInfoFragment.newInstance();//EventDetailsFragment
            default:
                return ChatFragment.newInstance();
        }
    }


}
