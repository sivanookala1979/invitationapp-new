package com.cerone.invitation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cerone.invitation.fragement.TabCheckIn;
import com.cerone.invitation.fragement.TabNotGoing;
import com.cerone.invitation.fragement.TabPending;


public class StatusTabAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public StatusTabAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0 :
                return new TabPending();
            case 1 :
                return new TabCheckIn();
            case 2 :
                return new TabNotGoing();
            default :
                break;
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Section " + (position + 1);
    }
}
