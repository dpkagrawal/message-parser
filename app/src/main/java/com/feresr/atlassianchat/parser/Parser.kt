package com.feresr.atlassianchat.parser

import com.feresr.atlassianchat.model.JSONNode
import rx.Single

/**
 * A Parser class must be able to build a Single that emits a [JSONNode]
 */
interface Parser {
    fun parse(message: String): Single<JSONNode>
}