package com.andiag.core

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layout: Int

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        ButterKnife.bind(this)
    }

    companion object {

        fun resolveColorAttribute(context: Context, attribute: Int): Int {
            val value = TypedValue()
            context.theme.resolveAttribute(attribute, value, true)
            return value.data
        }

        fun getCurrentLocale(context: Context): Locale {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales.get(0)
            } else {
                context.resources.configuration.locale
            }
        }
    }

}
