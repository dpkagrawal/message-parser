package com.feresr.atlassianchat.finder

import java.util.regex.Pattern
import javax.inject.Inject

/**
 * I tried several approaches to retrieve the title from a HTML string / input stream:
 * 1.XML parsers like xPath, SAX and XMLPullParser expect a properly formatted (valid) XML, which
 * is not always the case and hence require to `clean` the html to a valid XML first. "Cleaning" a
 * html string is non trivial or free in space/time complexity
 * 2. Using a library like JSOUP or which works very well but loads the entire DOM into memory.
 * This could be viable alternative.
 *
 * In the end I compromised and used a regex to find the title, which might be slower but also
 * more memory efficient.
 * In a real world application I would suggest having a API that would do this for us
 */
private val REGEX: String = "(?<=<title>)(.|\\s)*?(?=</title>)"

class HTMLTitleFinder @Inject constructor() : RegexContentFinder(Pattern.compile(REGEX))