package com.softcodeinfotech.helpapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.softcodeinfotech.helpapp.ui.AllHelpFragment;

public class PagerFragment extends Fragment {

    TabLayout tabs;
    ViewPager pager;
    String cat , lat , lng , rad , sta;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_frag_layout , container , false);

        assert getArguments() != null;
        cat = getArguments().getString("cat");
        lat = getArguments().getString("lat");
        lng = getArguments().getString("lng");
        rad = getArguments().getString("rad");
        sta = getArguments().getString("state");


        tabs = view.findViewById(R.id.tabLayout);
        pager = view.findViewById(R.id.pager);

        tabs.addTab(tabs.newTab().setText(getString(R.string.active)));
        tabs.addTab(tabs.newTab().setText(getString(R.string.completed)));

        pagerAdapter adapter = new pagerAdapter(getChildFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        tabs.getTabAt(0).setText(getString(R.string.active));
        tabs.getTabAt(1).setText(getString(R.string.completed));

        return view;
    }

    class pagerAdapter extends FragmentStatePagerAdapter
    {

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0)
            {
                AllHelpFragment allHelpFragment = new AllHelpFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cat", cat);
                bundle.putString("lat", lat);
                bundle.putString("lng", lng);
                bundle.putString("rad", rad);
                bundle.putString("state", sta);
                allHelpFragment.setArguments(bundle);
                return allHelpFragment;
            }
            else
            {
                allCompletedFragment allHelpFragment = new allCompletedFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cat", cat);
                bundle.putString("lat", lat);
                bundle.putString("lng", lng);
                bundle.putString("rad", rad);
                bundle.putString("state", sta);
                allHelpFragment.setArguments(bundle);
                return allHelpFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
