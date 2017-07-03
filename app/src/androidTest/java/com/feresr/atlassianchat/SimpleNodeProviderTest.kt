package com.feresr.atlassianchat

import android.support.test.runner.AndroidJUnit4
import com.feresr.atlassianchat.finder.ContentFinder
import com.feresr.atlassianchat.model.JSONNode
import com.feresr.atlassianchat.provider.SimpleNodeProvider
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
class SimpleNodeProviderTest {

    lateinit var simpleNodeProvider: SimpleNodeProvider
    var finder: ContentFinder = mock(ContentFinder::class.java)
    val NODE_NAME = "name"

    @Before
    fun setUp() {
        simpleNodeProvider = SimpleNodeProvider(finder, NODE_NAME)
    }

    @Test
    fun emptyTest() {
        val input = "Empty Test"

        Mockito.`when`(finder.findAll(input)).thenReturn(emptySet<String>())
        val single = simpleNodeProvider.from(input)
        val testSubscriber = TestSubscriber<JSONNode>()
        single.subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)

        val node = testSubscriber.onNextEvents.first()
        assertEquals("Node name is not equal", NODE_NAME, node.key)
        assertTrue("JSONArray is not empty", node.value.length() == 0)
    }

    @Test
    fun simpleTest() {
        val input = "Simple (emoticon) Test"
        val output = "emoticon"

        Mockito.`when`(finder.findAll(input)).thenReturn(setOf(output))
        val single = simpleNodeProvider.from(input)
        val testSubscriber = TestSubscriber<JSONNode>()
        single.subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)

        val node = testSubscriber.onNextEvents.first()
        assertEquals("Node name is not equal", NODE_NAME, node.key)
        assertTrue("JSONArray.length != 1", node.value.length() == 1)
        assertEquals("Incorrect outputLiveData", output, node.value[0])
    }
}
