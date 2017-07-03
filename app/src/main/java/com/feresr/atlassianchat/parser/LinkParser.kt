package com.feresr.atlassianchat.parser

import com.feresr.atlassianchat.finder.LinkFinder
import com.feresr.atlassianchat.model.JSONNode
import com.feresr.atlassianchat.utils.TitleRetriever
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides a [rx.Single] that emits a [JSONNode]
 * with a JSONNode.value = JSONArray of [JSONObject]:
 */
@Singleton
class LinkParser @Inject constructor(linkFinder: LinkFinder, val titleRetriever: TitleRetriever) : ObjectParser(linkFinder) {

    private val KEY_URL: String = "url"
    private val KEY_TITLE: String = "title"

    override fun getNodeName(): String {
        return "links"
    }

    override fun createLinkJSON(url: String): JSONObject {
        val result: JSONObject = JSONObject()
        result.put(KEY_URL, url)
        result.put(KEY_TITLE, titleRetriever.getUrlTitle(url))

        return result
    }
}