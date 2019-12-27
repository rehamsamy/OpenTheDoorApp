package com.m.openthedoorapp.fragmentTabs;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new CurrentFragmentTab();
                return fragment;
            case 1:
                fragment = new InProcessFragmentTab();
                return fragment;
            case 2:
                fragment = new ScheduledFragmentTab();
                return fragment;
            case 3:
                fragment = new CancledFragmebtTab();
                return fragment;
            case 4:
                fragment = new CompletedFragmentTab();
                return fragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
