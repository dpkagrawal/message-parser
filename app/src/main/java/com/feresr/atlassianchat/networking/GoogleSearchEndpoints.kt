package com.feresr.atlassianchat.networking

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by feresr on 3/7/17.
 */
interface GoogleSearchEndpoints {

    @GET("customsearch/v1?fields=items(title)&prettyPrint=false")
    fun titleSearch(@Query("q") url: String): Call<JsonObject>

    companion object {
        const val ITEMS: String = "items"
        const val TITLE: String = "title"
        const val BASE_URL = "https://www.googleapis.com/"

        const val PARAM_KEY = "key"
        const val PARAM_CX = "cx"
    }
}