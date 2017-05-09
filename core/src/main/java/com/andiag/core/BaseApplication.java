package com.andiag.core;

import android.app.Application;
import android.support.annotation.Nullable;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public abstract class BaseApplication extends Application {

    @Nullable
    protected abstract String getApplicationFont();

    @Override
    public void onCreate() {
        super.onCreate();
        if (getApplicationFont() != null) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(getApplicationFont())
                    .setFontAttrId(R.attr.fontPath)
                    .build());
        }
    }

}
