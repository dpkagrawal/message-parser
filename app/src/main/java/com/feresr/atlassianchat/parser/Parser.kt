package com.feresr.atlassianchat.parser

import com.feresr.atlassianchat.model.JSONNode
import rx.Single

/**
 * A Parser class must be able to build a Single that emits a [JSONNode]
 * Direct known subclasses [ObjectParser], [SimpleParser]
 */
interface Parser {
    fun parse(message: String): Single<JSONNode>
}