package com.feresr.atlassianchat.model

import org.json.JSONArray
import org.json.JSONObject

/**
 * Represents each node inside the resulting [JSONObject]
 * {
 *  emoticons: [,],     <-- JSONNode
 *  links: [{},{}],     <-- JSONNode
 * }
 * @property key [String] representing the name of the Node
 * @property value [JSONArray] of items in the node
 */
data class JSONNode(val key: String, val value: JSONArray)