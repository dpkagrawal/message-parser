package com.feresr.atlassianchat.networking

import com.feresr.atlassianchat.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by feresr on 3/7/17.
 */
class GoogleSearchInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("key", BuildConfig.CUSTOM_SEARCH_APIKEY)
                .addQueryParameter("cx", BuildConfig.CUSTOM_SEARCH_ENGINEID)
                .build()

        val requestBuilder = original.newBuilder().url(url)

        val request = requestBuilder?.build()
        return chain.proceed(request)
    }
}