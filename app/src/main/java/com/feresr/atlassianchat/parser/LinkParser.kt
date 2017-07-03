package com.feresr.atlassianchat.parser

import android.util.Log
import com.feresr.atlassianchat.finder.LinkFinder
import com.feresr.atlassianchat.model.JSONNode
import com.feresr.atlassianchat.networking.GoogleSearchEndpoints
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides a [rx.Single] that emits a [JSONNode]
 * with a JSONNode.value = JSONArray of [JSONObject]:
 */
@Singleton
class LinkParser @Inject constructor(linkFinder: LinkFinder,
                                     private val searchEndpoints: GoogleSearchEndpoints) : ObjectParser(linkFinder) {

    val KEY_URL: String = "url"
    val KEY_TITLE: String = "title"

    override fun getNodeName(): String {
        return "links"
    }

    /**
     * @param item the url string
     * @return [JSONObject] with the format { "url": item, "title": getUrtTitle(item) }
     */
    override fun createJSONObject(item: String): JSONObject {
        val result: JSONObject = JSONObject()
        result.put(KEY_URL, item)
        result.put(KEY_TITLE, getUrlTitle(item))
        return result
    }

    /**
     * I want to point out that I tried several approaches to retrieve the title from a given URL:
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
                    return resultArray.get(0)?.asJsonObject
                            ?.get(GoogleSearchEndpoints.TITLE)
                            ?.asString
                }
            }
        } catch (ignored: Exception) {
            Log.e(LinkParser::class.java.simpleName, ignored.message)
        }

        return "Title not found :("
    }
}