package com.cerone.invitation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cerone.invitation.fragement.ChatFragment;
import com.cerone.invitation.fragement.EventDetailsFragment;

import java.util.List;


/**
 * Created by B Hemanth Reddy on 27/9/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EventDetailsFragment.newInstance();
            default:
                return ChatFragment.newInstance();
        }
    }


}
