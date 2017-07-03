package com.feresr.parser

import com.feresr.parser.interfaces.ContentFinder
import com.feresr.parser.interfaces.Mapper
import com.feresr.parser.model.JSONNode
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import rx.Observable
import rx.Single
import rx.schedulers.Schedulers

/**
 * Constructs a [Single] that emit a [JSONNode]
 * If a [Mapper] is provided, each item found by the ContentFinder will be mapped into a
 * JsonObject using a different computation() thread.
 */
class Parser constructor(private val finder: ContentFinder,
                         private val name: String, private val mapper: Mapper? = null) {

    /**
     * @return Single that emits a [JSONNode] containing an array of [JsonObject]'s
     * If a [Mapper] is passed in, each map will be performed on a computation thread
     */
    internal fun parse(message: String): Single<JSONNode> {
        return Observable.fromCallable({ finder.findAll(message) })
                .flatMapIterable { it -> it }
                .flatMap {
                    if (mapper != null) {
                        Observable.fromCallable({ mapper.toJsonObject(it) })
                                .filter({ it != null })
                                .subscribeOn(Schedulers.computation())
                    } else {
                        //I believe this can be avoided with a conditional .compose() call
                        Observable.just(it)
                    }
                }
                .collect({ JsonArray() },
                        { array: JsonArray, value ->
                            when (value) {
                                is JsonObject -> array.add(value)
                                is String -> array.add(value)
                            }
                        })
                .map { JSONNode(name, it) }.toSingle()
    }
}
