package com.example.skinmanager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;


public class MyFragmentPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;


    public MyFragmentPagerAdapter(FragmentActivity activity, List<Fragment> fragments) {
        super(activity);
        mFragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getItemCount() {
        return  mFragments.size();
    }
}
