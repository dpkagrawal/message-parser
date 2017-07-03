package com.feresr.atlassianchat.finder

import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Uses Regular Expressions to find content within input text
 */
open class RegexContentFinder @Inject constructor(val pattern: Pattern) : ContentFinder {

    /**
     * @return Set of matches within text, or empty set if none
     */
    override fun findAll(message: String): Set<String> {

        var set = emptySet<String>()
        val matcher: Matcher = pattern.matcher(message)
        while (matcher.find()) {
            set = set.plus(matcher.group())
        }

        return set
    }

    /**
     * Like [RegexContentFinder::findAll(String)] but only finds and return the first value
     * @see findAll
     * @return String first match in text, or null if none
     */
    override fun findFirst(message: String): String? {
        val matcher: Matcher = pattern.matcher(message)
        while (matcher.find()) {
            return matcher.group()
        }
        return null
    }
}