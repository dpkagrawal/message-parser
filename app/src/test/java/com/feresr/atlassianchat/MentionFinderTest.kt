package com.feresr.atlassianchat

import com.feresr.atlassianchat.finders.MentionFinder
import org.junit.Assert.assertEquals
import org.junit.Test

class MentionFinderTest {

    var mentionFinder: MentionFinder = MentionFinder()

    @Test
    fun simpleTest() {
        assertEquals("Empty 1", emptySet<String>(), mentionFinder.findAll(""))
        assertEquals("Empty 2", emptySet<String>(), mentionFinder.findAll("@"))
        assertEquals("Simple case 1", setOf("me"), mentionFinder.findAll("@me"))
        assertEquals("Simple case 2", setOf("me", "you"), mentionFinder.findAll("@me @you"))
        assertEquals("With normal text", setOf("me", "you"), mentionFinder.findAll("Hey @you! have you seen @me?"))
        assertEquals("Non alphanumeric", emptySet<String>(), mentionFinder.findAll("@! @# @& @- @; @* @= @+ @/ @."))
    }

    @Test
    fun mixedTest() {
        assertEquals("Consecutive mentions", setOf("me"), mentionFinder.findAll("@me@you"))
        assertEquals("Between Parenthesis", setOf("me"), mentionFinder.findAll("(@me)"))
    }
}
