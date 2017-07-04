package com.feresr.atlassianchat

import android.util.Log
import com.feresr.atlassianchat.networking.GoogleSearchEndpoints
import com.feresr.parser.interfaces.Mapper
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrieves the title from each link and maps them to a
 * [JsonObject] with the format = { "url": "link.com", "title": "url title" }
 */
@Singleton
class LinkMapper @Inject constructor(private val searchEndpoints: GoogleSearchEndpoints) : Mapper {

    val KEY_URL: String = "url"
    val KEY_TITLE: String = "title"

    /**
     * @param item the url string
     * @return [JSONObject] with the format { "url": item, "title": getUrtTitle(item) }
     */
    override fun toJsonObject(item: String): JsonObject {
        val result: JsonObject = JsonObject()
        result.addProperty(KEY_URL, item)
        result.addProperty(KEY_TITLE, getUrlTitle(item))
        return result
    }

    /**
     * I tried several approaches to retrieve the title from a given URL:
     *
     * 1. Fetching the entire HTML and then use XML parsers like xPath, SAX and XMLPullParser
     * 2. Using a library like JSOUP or which works very well but loads the entire DOM into memory.
     * 3. Using a external API
     *
     * I asked the interviewer and she said that it was OK to use an API to do the job.
     * I used this approach since its the one I would use in a real world application
     *
     * @param url target URL
     * @return the string inside the html tags <Title></Title> if the request responds with an
     * 200OK http code. An error message if the response is not successful or if the
     * request could't be executed at all.
     */
    private fun getUrlTitle(url: String): String? {

        try {
            val response: Response<JsonObject> = searchEndpoints
                    .titleSearch("info:" + url)
                    .execute()

            if (response.isSuccessful) {
                val resultArray: JsonArray? = response.body()
                        ?.getAsJsonArray(GoogleSearchEndpoints.ITEMS)

                if (resultArray != null && resultArray.size() == 1) {
                    return resultArray.first().asJsonObject
                            ?.get(GoogleSearchEndpoints.TITLE)
                            ?.asString
                }
            }
        } catch (ignored: Exception) {
            Log.e(LinkMapper::class.java.simpleName, ignored.message)
        }

        return "Title not found"
    }

    /**
     * CHALLENGE SIDE NOTE:
     * If I had an API that was able to retrieve titles for multiple URL
     * at a time (on a single http request), which would be optimal for this use case.
     * I could just perform the call inside this method.
     */
    override fun bulkAction(items: Set<String>?) {}
}