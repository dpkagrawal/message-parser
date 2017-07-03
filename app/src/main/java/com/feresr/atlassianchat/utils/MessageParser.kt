package com.feresr.atlassianchat.utils

import com.feresr.atlassianchat.parser.Parser
import org.json.JSONObject
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Parses a message and creates a [JSONObject] with the nodes provided by each Parser
 */
@Singleton
class MessageParser @Inject constructor() {

    private val INDENTATIONS = 3

    @Inject
    lateinit var parsers: ArrayList<Parser>

    /**
     * I'm injecting the required [Parser]'s at build time For this sample project
     * Depending on the Use case one could require to add new parsers at runtime.
     */
    fun addParser(parser: Parser) {
        parsers.add(parser)
    }

    /**
     * Creates an [Observable] parse each node provider and builds a [JSONObject] with
     * the values returned by them
     * @param message String to parse
     * @return Single that emits a json formatted string
     */
    fun parse(message: String): Single<String> {
        return Observable
                .from(parsers)
                .flatMapSingle { it.parse(message).subscribeOn(Schedulers.computation()) }
                .collect({ JSONObject() },
                        { json: JSONObject, (key, value) ->
                            json.put(key, value)
                        })
                .map { it.toString(INDENTATIONS).replace("\\", "") }
                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
    }
}