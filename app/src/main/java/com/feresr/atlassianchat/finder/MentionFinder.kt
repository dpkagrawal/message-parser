package com.feresr.atlassianchat.finder

import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by feresr on 30/6/17.
 */
private val MENTION_CHAR: Char = '@'
private val REGEX: String = "(?<=(\\s|^|[^a-zA-Z:])\\$MENTION_CHAR)" + //first word in line or any word after a whitespace character ( , \n, \t)
        "[a-zA-Z]+" + //matching our mention char followed by an alphanumeric word of any length
        "(?=\\s|$|[^a-zA-Z:])" //that has a whitespace character or a non-alphanumeric character

class MentionFinder @Inject constructor() : RegexContentFinder(Pattern.compile(REGEX))