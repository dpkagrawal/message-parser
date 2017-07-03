package com.feresr.atlassianchat.parser

import com.feresr.atlassianchat.finder.ContentFinder
import com.feresr.atlassianchat.model.JSONNode
import org.json.JSONArray
import rx.Observable
import rx.Single

/**
 * Provides a Single that emit a [JSONNode] with a [JSONArray] of [String]
 * For each match on its finder, it will generate a String and insert it on the Node
 */
open class SimpleParser(private val contentFinder: ContentFinder, private val name: String) : Parser {

    override fun parse(message: String): Single<JSONNode> {
        return Observable
                .fromCallable { JSONNode(name, JSONArray(contentFinder.findAll(message))) }
                .toSingle()
    }
}