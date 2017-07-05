package com.feresr.atlassianchat

import android.util.Log
import com.feresr.atlassianchat.networking.MyEndpoints
import com.feresr.parser.interfaces.Mapper
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrieves the title from each link and maps them to a
 * [JsonObject] with the format = { "url": "link.com", "title": "url title" }
 */
@Singleton
class LinkMapper @Inject constructor(private val searchEndpoints: MyEndpoints) : Mapper {

    val KEY_URL: String = "url"
    val KEY_TITLE: String = "title"

    /**
     * @param item the url string
     * @return [JSONObject] with the format { "url": item, "title": getUrtTitle(item) }
     */
    override fun toJsonObject(url: String, title: String): JsonObject {
        val result: JsonObject = JsonObject()
        result.addProperty(KEY_URL, url)
        result.addProperty(KEY_TITLE, title)
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
    private fun getUrlTitle(urls: String): JsonArray? {

        try {
            val response: Response<JsonArray> = searchEndpoints
                    .titleSearch(urls)
                    .execute()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (ignored: Exception) {
            Log.e(LinkMapper::class.java.simpleName, ignored.message)
        }
        return JsonArray()
    }

    override fun bulkAction(items: List<String>?): Observable<String> {
        if (items != null && items.isNotEmpty()) {
            val sb: StringBuilder = StringBuilder()
            sb.append(items.first())
            for (i in 1 until items.size) {
                sb.append(',').append(items[i])
            }

            val titles = ArrayList<String>()
            val titlesJsonArray = getUrlTitle(sb.toString())
            if (titlesJsonArray != null) {
                (0..titlesJsonArray.size() - 1).mapTo(titles) { titlesJsonArray.get(it).asString }
            }
            return Observable.from(titles)
        }

        return Observable.empty()
    }
}