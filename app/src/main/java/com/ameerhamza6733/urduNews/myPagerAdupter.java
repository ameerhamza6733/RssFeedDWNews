package com.ameerhamza6733.urduNews;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by DELL 3542 on 7/21/2016.
 */
public  class myPagerAdupter extends FragmentStatePagerAdapter {
    public String[] getTabs() {
        return tabs;
    }

    private final String [] tabs;
    private Context c;

    public myPagerAdupter(FragmentManager fm , Context c) {

        super(fm);
        this.c=c;
        tabs = c.getResources().getStringArray(R.array.tabs);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }


    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        android.support.v4.app.Fragment  fragment = new android.support.v4.app.Fragment();
        if(position ==0)
        {
            fragment = new HeadLineFragment();
        }
        else if(position==1)
        {
            fragment = new HalatHazraFragment();
        }
        else  if(position==2)
        {
            fragment = new MoreNewsFragment();
        }


        return fragment;
    }


    @Override
    public int getCount() {
        return 3;
    }




}