package com.andiag.core;

import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

/**
 * Created by Andy on 03/01/2018.
 */

public class BaseTab {

    private Fragment mFragment;
    private String mTitle;
    private @DrawableRes int mIcon;

    public BaseTab(Fragment fragment, String title, int icon) {
        this.mFragment = fragment;
        this.mTitle = title;
        this.mIcon = icon;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }
}
