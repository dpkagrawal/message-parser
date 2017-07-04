package com.feresr.parser.interfaces

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/**
 * Classes implementing this interface perform modifications and actions upon Message contents
 * extracted by a [ContentFinder]
 */
interface Mapper {

    /**
     * @param item [String] an item retrieved by [ContentFinder]
     * @return the [JsonElement] to be inserted in a [JsonArray]
     */
    fun toJsonObject(item: String): JsonObject?

    /**
     * Perform an action to ALL items retrieved by a [ContentFinder]
     */
    fun bulkAction(items: Set<String>?)
}