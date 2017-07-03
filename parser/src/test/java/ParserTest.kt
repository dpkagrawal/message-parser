package com.feresr.parser.interfaces

import com.feresr.parser.Parser
import com.feresr.parser.model.JSONNode
import com.google.gson.JsonObject
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import rx.observers.TestSubscriber

class ParserTest {

    val NODE_NAME = "name"
    var finder: ContentFinder = Mockito.mock(ContentFinder::class.java)
    var mapper: Mapper = Mockito.mock(Mapper::class.java)


    @Test
    fun emptyTest() {
        val parser: Parser = Parser(finder, NODE_NAME)

        val input: String = "hello world"
        Mockito.`when`(finder.findAll(input)).thenReturn(emptySet<String>())
        val single = parser.parse(input)
        val testSubscriber = TestSubscriber<JSONNode>()
        single.subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)

        val node = testSubscriber.onNextEvents.first()
        Assert.assertEquals("Node name is not equal", NODE_NAME, node.key)
        Assert.assertTrue("JSONArray is not empty", node.value.size() == 0)
    }

    @Test
    fun simpleTest() {
        val input = "Simple (emoticonParser) Test"
        val output = "emoticonParser"
        val parser: Parser = Parser(finder, NODE_NAME)

        Mockito.`when`(finder.findAll(input)).thenReturn(setOf(output))
        val single = parser.parse(input)
        val testSubscriber = TestSubscriber<JSONNode>()
        single.subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)

        val node = testSubscriber.onNextEvents.first()
        Assert.assertEquals("Node name is not equal", NODE_NAME, node.key)
        Assert.assertTrue("JSONArray.length != 1", node.value.size() == 1)
        Assert.assertEquals("Incorrect outputLiveData", output, node.value[0].asString)
    }

    @Test
    fun mapperTest() {
        val input = "Simple www.link.com Test"
        val finderOutput = "emoticonParser"
        val mapperOutput = JsonObject()
        mapperOutput.addProperty("url", "url")
        mapperOutput.addProperty("title", "title")

        val parser: Parser = Parser(finder, NODE_NAME, mapper)

        Mockito.`when`(finder.findAll(input)).thenReturn(setOf(finderOutput))
        Mockito.`when`(mapper.toJsonObject(finderOutput)).thenReturn(mapperOutput)

        val single = parser.parse(input)
        val testSubscriber = TestSubscriber<JSONNode>()
        single.subscribe(testSubscriber)

        testSubscriber.awaitTerminalEvent()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueCount(1)

        val node = testSubscriber.onNextEvents.first()
        Assert.assertEquals("Node name is not equal", NODE_NAME, node.key)
        Assert.assertTrue("JSONArray.length != 1", node.value.size() == 1)
        Assert.assertTrue("Node value != mapperOutput", node.value[0] == mapperOutput)
    }

}
