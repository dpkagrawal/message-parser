package com.feresr.atlassianchat.provider

import com.feresr.atlassianchat.finder.ContentFinder
import com.feresr.atlassianchat.model.JSONNode
import org.json.JSONArray
import org.json.JSONObject
import rx.Observable
import rx.Single
import rx.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Provides a <Single> that emit a JSONNode with a JSONArray of JSONObjects
 * It uses a different a thread to process the creation each JSONObject
 * For each match on its finder, it will generate a JSONObject and insert it on the JSONNode
 */
@Singleton
abstract class ObjectNodeProvider constructor(val finder: ContentFinder) : NodeProvider {

    /**
     * @return Single that emits a JSONNode containing an array of JSONObjects
     * Uses a computation thread pull to builds each JsonObject in parallel
     */
    final override fun from(message: String): Single<JSONNode> {
        return Observable.fromCallable({ finder.findAll(message) })
                .flatMapIterable { it -> it }
                .flatMap {
                    Observable.fromCallable({ createLinkJSON(it) })
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
     * Builds a JSONObject inside
     * Called in a computation thread
     * @return hte JSONObject to be inserted in the JSONArray
     * */
    abstract fun createLinkJSON(url: String): JSONObject
}