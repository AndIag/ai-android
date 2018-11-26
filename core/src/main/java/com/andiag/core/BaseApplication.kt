package com.andiag.core

import android.app.Application

import uk.co.chrisjenx.calligraphy.CalligraphyConfig

abstract class BaseApplication : Application() {

    protected abstract val applicationFont: String?

    override fun onCreate() {
        super.onCreate()
        if (applicationFont != null) {
            CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                    .setDefaultFontPath(applicationFont)
                    .setFontAttrId(R.attr.fontPath)
                    .build())
        }
    }

}
