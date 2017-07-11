package com.feresr.parser

import com.feresr.parser.interfaces.ContentFinder
import io.reactivex.Observable
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Utility calls that uses Regular Expressions to find content within input text
 */
open class RegexContentFinder(val pattern: Pattern) : ContentFinder {

    /**
     * @return Set of matches within text, or empty set if none
     */
    override fun findAll(message: String): Observable<String> {
        return Observable.create<String> {
            try {
                val matcher: Matcher = pattern.matcher(message)
                while (matcher.find()) {
                    it.onNext(matcher.group())
                }
                it.onComplete()
            } catch (error: Exception) {
                it.onError(error)
            }
        }
    }
}