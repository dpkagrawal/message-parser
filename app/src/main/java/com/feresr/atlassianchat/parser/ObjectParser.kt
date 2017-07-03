package com.feresr.atlassianchat.parser

import com.feresr.atlassianchat.finder.ContentFinder
import com.feresr.atlassianchat.model.JSONNode
import org.json.JSONArray
import org.json.JSONObject
import rx.Observable
import rx.Single
import rx.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Provides a [Single] that emit a [JSONNode] with a [JSONArray] of [JSONObject]s
 * It uses a different a thread to process the creation each [JSONObject]
 * For each match on its finder, it will generate a JSONObject and insert it on the [JSONNode]
 */
@Singleton
abstract class ObjectParser constructor(val finder: ContentFinder) : Parser {

    /**
     * @return Single that emits a [JSONNode] containing an array of [JSONObject]'s
     * Uses a computation thread pull to builds each [JSONObject] in parallel
     */
    final override fun parse(message: String): Single<JSONNode> {
        return Observable.fromCallable({ finder.findAll(message) })
                .flatMapIterable { it -> it }
                .flatMap {
                    Observable.fromCallable({ createJSONObject(it) })
                            .filter({ it != null })
                            .subscribeOn(Schedulers.computation())
                }
                .toList()
                .map { JSONNode(getNodeName(), JSONArray(it)) }.toSingle()
    }

    /**
     * @return String the name of the node
     */
    abstract fun getNodeName(): String

    /**
     * Called in a computation thread
     * @return the [JSONObject] to be inserted in the [JSONArray]
     * or null if a json object must not be created for this string
     * */
    abstract fun createJSONObject(item: String): JSONObject?
}