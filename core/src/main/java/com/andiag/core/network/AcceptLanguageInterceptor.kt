package com.andiag.core.network

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

class AcceptLanguageInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithHeaders = originalRequest.newBuilder()
                .header("Accept-Language", localeToBcp47Language(Locale.getDefault()))
                .build()
        return chain.proceed(requestWithHeaders)
    }

    /*
     * From https://github.com/apache/cordova-plugin-globalization/blob/master/src/android/Globalization.java#L140
     * @Description: Returns a well-formed ITEF BCP 47 language tag representing
     * the locale identifier for the client's current locale
     *
     * @Return: String: The BCP 47 language tag for the current locale
     */
    private fun localeToBcp47Language(loc: Locale): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return loc.toLanguageTag()
        }

        val SEP = '-'       // we will use a dash as per BCP 47
        var language = loc.language
        var region = loc.country
        var variant = loc.variant

        // special case for Norwegian Nynorsk since "NY" cannot be a variant as per BCP 47
        // this goes before the string matching since "NY" wont pass the variant checks
        if (language == "no" && region == "NO" && variant == "NY") {
            language = "nn"
            region = "NO"
            variant = ""
        }

        if (language.isEmpty() || !language.matches("\\p{Alpha}{2,8}".toRegex())) {
            language = "und"       // Follow the Locale#toLanguageTag() implementation
            // which says to return "und" for Undetermined
        } else if (language == "iw") {
            language = "he"        // correct deprecated "Hebrew"
        } else if (language == "in") {
            language = "id"        // correct deprecated "Indonesian"
        } else if (language == "ji") {
            language = "yi"        // correct deprecated "Yiddish"
        }

        // ensure valid country code, if not well formed, it's omitted
        if (!region.matches("\\p{Alpha}{2}|\\p{Digit}{3}".toRegex())) {
            region = ""
        }

        // variant subtags that begin with a letter must be at least 5 characters long
        if (!variant.matches("\\p{Alnum}{5,8}|\\p{Digit}\\p{Alnum}{3}".toRegex())) {
            variant = ""
        }

        val bcp47Tag = StringBuilder(language)
        if (!region.isEmpty()) {
            bcp47Tag.append(SEP).append(region)
        }
        if (!variant.isEmpty()) {
            bcp47Tag.append(SEP).append(variant)
        }

        return bcp47Tag.toString()
    }
}
