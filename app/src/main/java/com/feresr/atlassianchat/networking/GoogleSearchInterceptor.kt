package com.feresr.atlassianchat.networking

import com.feresr.atlassianchat.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Intercepts all http calls and adds [GoogleSearchEndpoints.PARAM_KEY] & [GoogleSearchEndpoints.PARAM_CX]
 * query parameters
 */
class GoogleSearchInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter(GoogleSearchEndpoints.PARAM_KEY, BuildConfig.CUSTOM_SEARCH_APIKEY)
                .addQueryParameter(GoogleSearchEndpoints.PARAM_CX, BuildConfig.CUSTOM_SEARCH_ENGINEID)
                .build()

        val requestBuilder = original.newBuilder().url(url)

        val request = requestBuilder?.build()
        return chain.proceed(request)
    }
}