package com.feresr.atlassianchat.finder

import java.util.regex.Pattern
import javax.inject.Inject

val EMOTICON_START_CHAR: Char = '('
val EMOTICON_END_CHAR: Char = ')'
val EMOTICON_MIN_LENGTH: Int = 1
val EMOTICON_MAX_LENGTH: Int = 15

private val REGEX: String = "(?<=\\$EMOTICON_START_CHAR)" +
        "[a-zA-Z0-9]{$EMOTICON_MIN_LENGTH,$EMOTICON_MAX_LENGTH}?" +
        "(?=\\$EMOTICON_END_CHAR)"

class EmoticonFinder @Inject constructor() : RegexContentFinder(Pattern.compile(REGEX))

