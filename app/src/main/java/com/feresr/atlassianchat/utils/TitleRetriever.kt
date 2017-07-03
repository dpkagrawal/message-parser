package com.feresr.atlassianchat.utils

import com.feresr.atlassianchat.finder.HTMLTitleFinder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Extracts fetches the html body form a given url and uses [HTMLTitleFinder] to find
 * the string within the html tag <Title>
 */
open class TitleRetriever(private val client: OkHttpClient, val finder: HTMLTitleFinder) {

    /**
     * @param url target URL
     * @return the string inside the html tags <Title></Title> if the request responds with an
     * 200OK http code. An error message if the response is not successful or if the
     * request could't be executed at all.
     */
    open fun getUrlTitle(url: String): String? {
        val sanitisedUrl = sanitizeUrl(url)

        try {
            val request = Request.Builder().url(HttpUrl.parse(sanitisedUrl)).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body: String = response.body()?.string() ?: ""
                return finder.findFirst(body)
            }
        } catch (e: Exception) {
            if (sanitisedUrl.startsWith("http://")) {
                //try with https
                return getUrlTitle(sanitisedUrl.replace("http://", "https://"))
            }
        }

        return "Title not found :("
    }

    private fun sanitizeUrl(url: String): String {
        var result = url
        if (!result.startsWith("http://") && !result.startsWith("https://")) {
            result = "http://" + result
        }
        return result
    }
}