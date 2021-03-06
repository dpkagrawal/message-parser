package com.feresr.parser.interfaces

import io.reactivex.Observable

/**
 * A ContentFinder must be able to extract string snippets parse a string using criteria dependant on
 * each implementation.
 * @see RegexContentFinder
 */
interface ContentFinder {
    /**
     * @return Set<String> containing all extracts of message that conform
     * with the implementation
     */
    fun findAll(message: String): Observable<String>
}