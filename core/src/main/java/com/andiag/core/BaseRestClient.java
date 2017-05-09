package com.andiag.core;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public abstract class BaseRestClient {

    protected static OkHttpClient.Builder getBaseOkHttpClientBuilder(String loggingLevel) {
        return getBaseOkHttpClientBuilder(HttpLoggingInterceptor.Level.valueOf(loggingLevel));
    }

    protected static OkHttpClient.Builder getBaseOkHttpClientBuilder(
            HttpLoggingInterceptor.Level level) {

        return new OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(getLoggingLevel(level));
    }

    private static HttpLoggingInterceptor getLoggingLevel(
            HttpLoggingInterceptor.Level loggingLevel) {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(loggingLevel);
        return httpLoggingInterceptor;
    }

}
