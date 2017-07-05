package com.feresr.atlassianchat.networking

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by feresr on 3/7/17.
 */
interface MyEndpoints {

    @GET("getTitles")
    fun titleSearch(@Query("urls") url: String): Call<JsonArray>

    companion object {
        const val BASE_URL = "https://us-central1-chat-parse.cloudfunctions.net/"
    }
}