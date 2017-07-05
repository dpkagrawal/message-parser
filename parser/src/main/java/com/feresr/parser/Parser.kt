package com.feresr.parser

import com.feresr.parser.interfaces.ContentFinder
import com.feresr.parser.interfaces.Mapper
import com.feresr.parser.model.JSONNode
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import rx.Observable
import rx.Single

/**
 * Constructs a [Single] that emit a [JSONNode]
 * If a [Mapper] is provided, each item found by the ContentFinder will be mapped into a
 * [JsonObject] using a different computation() thread.
 */
class Parser constructor(private val finder: ContentFinder,
                         private val name: String, private val mapper: Mapper? = null) {

    /**
     * @return Single that emits a [JSONNode] containing an array of [JsonObject]'s
     * If a [Mapper] is passed in, each map will be performed on a computation thread
     */
    internal fun parse(message: String): Single<JSONNode> {

        return Observable.fromCallable({ finder.findAll(message) })
                .doOnNext { mapper?.bulkAction(it.toList()) }
                .toJsonElement()
                .collect({ JsonArray() },
                        { array: JsonArray, value ->
                            array.add(value)
                        })
                .map { JSONNode(name, it) }.toSingle()
    }

    /**
     * Converts each [String] into a [JsonElement], JsonObject OR JsonPrimitive
     * depending on weather or not the mapper is set for this instance
     */
    private fun Observable<List<String>>.toJsonElement(): Observable<JsonElement> {
        if (mapper != null) {
            return this.flatMap {
                Observable.zip(rx.Observable.from(it),
                        mapper.bulkAction(it), { url, title -> mapper.toJsonObject(url, title) })
            }
        } else {
            return this.flatMapIterable { it -> it }.map { JsonPrimitive(it) }
        }
    }
}