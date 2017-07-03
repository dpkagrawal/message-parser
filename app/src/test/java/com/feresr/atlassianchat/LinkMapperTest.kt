package com.feresr.atlassianchat

import com.feresr.atlassianchat.networking.GoogleSearchEndpoints
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import retrofit2.mock.Calls

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LinkMapperTest {

    var searchEndpoints: GoogleSearchEndpoints = mock(GoogleSearchEndpoints::class.java)

    lateinit var linkMapper: LinkMapper

    @Before
    fun setUp() {
        linkMapper = LinkMapper(searchEndpoints)
    }

    @Test
    fun simpleTest() {
        val link = "www.link.com"
        val output = "Website Title"

        Mockito.`when`(searchEndpoints.titleSearch(anyString()))
                .thenReturn(Calls.response(Gson().fromJson("""{ "items": [{ "${linkMapper.KEY_TITLE}": "$output" }] }""", JsonObject::class.java)))

        val json = linkMapper.toJsonObject(link)

        assertTrue("Missing `url` entry", json.has("url"))
        assertTrue("Missing `title` entry", json.has("title"))
        assertEquals("Wrong url", link, json.getAsJsonPrimitive("url").asString)
        assertEquals("Wrong title", output, json.getAsJsonPrimitive("title").asString)
    }
}
