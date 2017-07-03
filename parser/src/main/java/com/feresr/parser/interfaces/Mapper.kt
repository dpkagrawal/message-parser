package com.feresr.parser.interfaces

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

interface Mapper {
    /**
     * @param item [String] an item retrieved by [com.feresr.parser]
     * @return the [JsonElement] to be inserted in the [JsonArray]
     */
    fun toJsonObject(item: String): JsonObject?
}