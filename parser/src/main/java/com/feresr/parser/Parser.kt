package com.feresr.parser

import com.feresr.parser.interfaces.ContentFinder
import com.feresr.parser.interfaces.Mapper
import com.feresr.parser.model.JSONNode
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

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
        return finder.findAll(message)
                .distinct()
                .toJsonElement()
                .collect(::JsonArray, JsonArray::add)
                .map { JSONNode(name, it) }
    }

    /**
     * Converts each [String] into a [JsonElement], JsonObject OR JsonPrimitive
     * depending on weather or not the mapper is set for this instance
     */
    private fun Observable<String>.toJsonElement(): Observable<JsonElement> {
        if (mapper != null) {
            return this.flatMap {
                Observable.fromCallable({ mapper.toJsonObject(it) })
                        .subscribeOn(Schedulers.computation()) //perform each map on a different thread
            }
        } else {
            return this.map { JsonPrimitive(it) }
        }
    }
}


