package com.feresr.parser.model

import com.google.gson.JsonArray

/**
 * Represents each node inside the resulting [JSONObject]
 * {
 *  emoticons: [,],     <-- JSONNode
 *  links: [{},{}],     <-- JSONNode
 * }
 * @property key [String] representing the name of the Node
 * @property value [JSONArray] of items in the node
 */
internal data class JSONNode(val key: String, val value: JsonArray)