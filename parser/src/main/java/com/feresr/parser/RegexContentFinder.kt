package com.feresr.parser

import com.feresr.parser.interfaces.ContentFinder
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Utility calls that uses Regular Expressions to find content within input text
 */
open class RegexContentFinder(val pattern: Pattern) : ContentFinder {

    /**
     * @return Set of matches within text, or empty set if none
     */
    override fun findAll(message: String): List<String> {

        var set = emptySet<String>()
        val matcher: Matcher = pattern.matcher(message)
        while (matcher.find()) {
            set = set.plus(matcher.group())
        }

        return set.toList()
    }
}