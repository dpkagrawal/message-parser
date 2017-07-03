package com.feresr.atlassianchat

import android.support.test.runner.AndroidJUnit4
import com.feresr.atlassianchat.finder.LinkFinder
import com.feresr.atlassianchat.model.JSONNode
import com.feresr.atlassianchat.parser.LinkParser
import com.feresr.atlassianchat.utils.TitleRetriever
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import rx.observers.TestSubscriber

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LinkParserTest {

    var titleRetriever: TitleRetriever = mock(TitleRetriever::class.java)
    var finder: LinkFinder = mock(LinkFinder::class.java)

    lateinit var linkNodeProvider: LinkParser

    @Before
    fun setUp() {
        linkNodeProvider = LinkParser(finder, titleRetriever)
    }

    @Test
    fun simpleTest() {
        val input = ""
        Mockito.`when`(finder.findAll(input)).thenReturn(emptySet<String>())

        val single = linkNodeProvider.parse(input)
        val testSubscriber = TestSubscriber<JSONNode>()
        single.subscribe(testSubscriber)

        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)

        val node = testSubscriber.onNextEvents.first()
        assertEquals("Node name is not equal", "links", node.key)
        assertTrue("Node value is not empty", node.value.length() == 0)
    }

    @Test
    fun singleTest() {
        val input = "Simple www.link.com Test"
        val link = "www.link.com"
        val output = "Website Title"

        Mockito.`when`(finder.findAll(input)).thenReturn(setOf(link))
        Mockito.`when`(titleRetriever.getUrlTitle(link)).thenReturn(output)

        val single = linkNodeProvider.parse(input)
        val testSubscriber = TestSubscriber<JSONNode>()
        single.subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)

        val node = testSubscriber.onNextEvents.first()
        assertEquals("Node name is not equal", "links", node.key)
        assertTrue("Node value is not 1", node.value.length() == 1)
        assertEquals("Wrong url", link, (node.value[0] as JSONObject).get("url"))
        assertEquals("Wrong title", output, (node.value[0] as JSONObject).get("title"))
    }
}
