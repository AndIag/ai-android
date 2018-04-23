package com.andiag.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTabsFragment<P extends BasePresenter> extends BaseFragment<P> {

    private final static String SELECTED_TAB = "SELECTED_TAB";

    ViewPager mViewPager;
    TabLayout mTabLayout;

    private TabsAdapter mAdapter;
    private int mSelectedTab = 0;

    @Override
    protected int getLayout() {
        return R.layout.fragment_tabs;
    }

    private void setupTabs() {
        mAdapter = new TabsAdapter(getChildFragmentManager());
        for (BaseTab tab : getTabs()) {
            mAdapter.addTab(tab);
        }
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabLayout();
    }

    private void setupTabLayout() {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(getTabs().get(i).getIcon());
        }
    }

    protected void reloadTabs() {
        if (mAdapter != null) {
            mAdapter.clearAll();
            for (BaseTab tab : getTabs()) {
                mAdapter.addTab(tab);
            }
            mAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            setupTabLayout();
            mViewPager.setCurrentItem(mSelectedTab);
            return;
        }
        setupTabs();
    }

    public abstract List<BaseTab> getTabs();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_TAB, mSelectedTab);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            mViewPager = view.findViewById(R.id.viewPager);
            mTabLayout = view.findViewById(R.id.tabLayout);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mSelectedTab = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_TAB)) {
                mSelectedTab = savedInstanceState.getInt(SELECTED_TAB);
                mViewPager.setCurrentItem(mSelectedTab);
            }
            setupTabs();
        }
        return view;
    }

    static class TabsAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        void addTab(BaseTab tab) {
            mFragments.add(tab.getFragment());
            mFragmentTitles.add(tab.getTitle());
        }

        void clearAll() {
            mFragments.clear();
            mFragmentTitles.clear();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence pageTitle = mFragmentTitles.get(position);
            return pageTitle.equals("") ? pageTitle : null;
        }
    }
}
