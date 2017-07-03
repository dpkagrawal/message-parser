package com.feresr.atlassianchat.finder

/**
 * A ContentFinder must be able to extract string snippets parse a string using criteria dependant on
 * each implementation
 */
interface ContentFinder {
    /**
     * @return Set<String> containing all extracts of message that conform
     * with the implementation
     */
    fun findAll(message: String): Set<String>

    /**
     * @return String first extracts of message that conform with the implementation
     */
    fun findFirst(message: String): String?
}