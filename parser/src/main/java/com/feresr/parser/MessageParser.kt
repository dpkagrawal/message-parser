package com.feresr.parser

import com.google.gson.JsonObject
import rx.Observable
import rx.Single
import rx.schedulers.Schedulers

/**
 * Parses a message and creates a [JsonObject] with the nodes provided by each [Parser]
 * Sample usage:
 * var parser: MessageParser = MessageParser()
 * parser.addParser()
 * parser.parse("some message")
 */
class MessageParser {

    private val parsers: ArrayList<Parser> = ArrayList()

    fun addParser(parser: Parser): MessageParser {
        parsers.add(parser)
        return this
    }

    fun removeParser(parser: Parser): MessageParser {
        parsers.remove(parser)
        return this
    }

    /**
     * Creates an [Observable] that emits a single [JsonObject] built using the provided parsers
     * Note: Each [Parser] runs on its own computation thread
     * @param message String to parse
     * @return Single that emits a json formatted string
     */
    fun parse(message: String): Single<JsonObject> {
        return Observable
                .from(parsers)
                .flatMapSingle { it.parse(message).subscribeOn(Schedulers.computation()) }
                .collect({ JsonObject() },
                        { json: JsonObject, (key, value) ->
                            json.add(key, value)
                        }).toSingle()
    }
}