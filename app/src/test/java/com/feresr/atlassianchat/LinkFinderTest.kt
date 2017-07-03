package com.feresr.atlassianchat

import com.feresr.atlassianchat.finder.LinkFinder
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LinkFinderTest {

    var linkFinder: LinkFinder = LinkFinder()

    @Test
    fun simpleTest() {
        assertEquals("Empty 1", emptySet<String>(), linkFinder.findAll(""))
        assertEquals("Simple case 1", setOf("www.atlassian.com"), linkFinder.findAll("www.atlassian.com"))
        assertEquals("Simple case 2", setOf("atlassian.com"), linkFinder.findAll("atlassian.com"))
        assertEquals("Simple case 3", setOf("http://atlassian.com"), linkFinder.findAll("http://atlassian.com"))
        assertEquals("Simple case 4", setOf("http://atlassian.com/android"), linkFinder.findAll("http://atlassian.com/android"))
        assertEquals("Simple case 5", setOf("http://atlassian.com/android_ios"), linkFinder.findAll("http://atlassian.com/android_ios"))
        assertEquals("Simple case 6", setOf("http://atlassian.com/android_ios"), linkFinder.findAll("http://atlassian.com/android_ios/"))
        assertEquals("Simple case 7", setOf("https://atlassian.com/android_ios"), linkFinder.findAll("https://atlassian.com/android_ios/"))
        assertEquals("Simple case 8", setOf("jira.atlassian.com/secure"), linkFinder.findAll("jira.atlassian.com/secure"))
        assertEquals("Simple case 9", setOf("jira.atlassian.com/secure/Dashboard.jspa"), linkFinder.findAll("jira.atlassian.com/secure/Dashboard.jspa"))
    }

    @Test
    fun mixedTest() {
        assertEquals("Mention is not an url", emptySet<String>(), linkFinder.findAll("@google.com"))
        assertEquals("Within parenthesis", setOf("atlassian.com"), linkFinder.findAll("(atlassian.com)"))
    }
}
