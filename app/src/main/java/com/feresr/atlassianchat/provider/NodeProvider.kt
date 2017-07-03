package com.feresr.atlassianchat.provider

import com.feresr.atlassianchat.model.JSONNode
import rx.Single

/**
 * A NodeProvider class must be able to build a Single that emits a @see JSONNode
 */
interface NodeProvider {
    fun from(message: String): Single<JSONNode>
}