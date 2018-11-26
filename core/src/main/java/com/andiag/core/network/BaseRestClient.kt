package com.andiag.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

abstract class BaseRestClient {
    companion object {

        protected fun getBaseOkHttpClientBuilder(loggingLevel: String): OkHttpClient.Builder {
            return getBaseOkHttpClientBuilder(HttpLoggingInterceptor.Level.valueOf(loggingLevel))
        }

        fun getBaseOkHttpClientBuilder(level: HttpLoggingInterceptor.Level): OkHttpClient.Builder {
            return OkHttpClient.Builder()
                    .connectTimeout(12, TimeUnit.SECONDS)
                    .readTimeout(12, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(getLoggingLevel(level))
        }

        private fun getLoggingLevel(loggingLevel: HttpLoggingInterceptor.Level): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = loggingLevel
            return httpLoggingInterceptor
        }
    }

}
