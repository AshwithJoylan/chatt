package com.facebook.chatt.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.facebook.chatt.Home.ChatFragment;
import com.facebook.chatt.Home.FriendsFragment;
import com.facebook.chatt.Home.RequestsFragment;

public class CustomPageAdapter extends FragmentPagerAdapter {

    public CustomPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;

            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return  chatFragment;

            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
