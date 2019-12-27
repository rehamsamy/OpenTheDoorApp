package com.m.openthedoorapp.fragmentTabs;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PaymentPagerAdapter extends FragmentPagerAdapter {
    public PaymentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MasterPaymentFragment();
            case 1:
                return new VisaPaymentFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
//                return "MasterCard";
                return "";
            case 1:
//                return "Visa";
                return "";
            default:
                return null;
        }
    }
}
