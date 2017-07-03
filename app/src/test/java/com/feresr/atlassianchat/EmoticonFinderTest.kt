package com.feresr.atlassianchat

import com.feresr.atlassianchat.finders.EmoticonFinder
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class EmoticonFinderTest {

    var emoticonFinder: EmoticonFinder = EmoticonFinder()

    @Test
    fun simpleTest() {
        assertEquals("Empty 1", emptySet<String>(), emoticonFinder.findAll(""))
        assertEquals("Empty 2", emptySet<String>(), emoticonFinder.findAll("()"))
        assertEquals("Simple case 1", setOf("beer"), emoticonFinder.findAll("(beer)"))
        assertEquals("Simple case 2", setOf("beer", "cake"), emoticonFinder.findAll("(beer) (cake)"))
        assertEquals("With normal text", setOf("beer", "cake"), emoticonFinder.findAll("Random (beer) text (cake)"))
        assertEquals("Non alphanumeric", emptySet<String>(), emoticonFinder.findAll("(.) (_) ($) (@) (*) (<) ( ) (!) (-)"))
    }

    @Test
    fun nestedTest() {
        assertEquals("Empty 1", emptySet<String>(), emoticonFinder.findAll("(())"))
        assertEquals("Nested 1", setOf("beer"), emoticonFinder.findAll("((beer))"))
        assertEquals("Nested 2", setOf("beer", "cake"), emoticonFinder.findAll("((beer)(cake))"))
        assertEquals("Nested 3", setOf("beer", "cake"), emoticonFinder.findAll("(((beer)(cake)))"))
        assertEquals("Nested 4", setOf("beer", "cake"), emoticonFinder.findAll("()(((beer)((cake)() party)"))
        assertEquals("Nested unbalanced", setOf("beer", "cake"), emoticonFinder.findAll("((((beer)((cake)"))
        assertEquals("Nested with text", setOf("party", "beer"), emoticonFinder.findAll("Pss.. It's friday(party) (Let's have a (beer))"))
    }

    @Test
    fun mixedTest() {
        assertEquals("Link is not an emoticonParser 1", emptySet<String>(), emoticonFinder.findAll("(www.atlassian.com)"))
        assertEquals("Link is not an emoticonParser 2", emptySet<String>(), emoticonFinder.findAll("(atlassian.com)"))
        assertEquals("Mention is not an emoticonParser", emptySet<String>(), emoticonFinder.findAll("(@notAnEmoticon)"))
    }
}
