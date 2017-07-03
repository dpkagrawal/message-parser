package com.feresr.atlassianchat.finder

import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by feresr on 30/6/17.
 */
//Tried a couple of URL Regex including Patterns.WEB_URL, and none was good enough.
//I ended up taking this regex from https://stackoverflow.com/questions/6038061/regular-expression-to-find-urls-within-a-string
//and modified it to play nicely with @mentions and (emoticons)

private val REGEX: String = "\\b(?<![@.,%&#-])(\\w{2,10}://)?((?:\\w|&#\\d{3,5};)[.-]?)+\\.([a-zA-Z]{2,15})\\b(?![@])(/)?(?:([\\w\\d?\\-=#:%@&.;])+(?:/(?:([\\w\\d?\\-=#:%@&;.])+))*)?(?<![.,?!-])"

open class LinkFinder @Inject constructor() : RegexContentFinder(Pattern.compile(REGEX))