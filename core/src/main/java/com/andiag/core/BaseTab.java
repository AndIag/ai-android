package com.andiag.core;

import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

/**
 * Created by Andy on 03/01/2018.
 */

public class BaseTab {

    private Fragment fragment;
    private String title;
    private @DrawableRes int icon;

    public BaseTab(Fragment fragment, String title, int icon) {
        this.fragment = fragment;
        this.title = title;
        this.icon = icon;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
