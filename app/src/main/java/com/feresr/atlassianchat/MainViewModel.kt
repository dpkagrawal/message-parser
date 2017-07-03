package com.feresr.atlassianchat

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.feresr.parser.MessageParser
import com.feresr.parser.Parser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import rx.SingleSubscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Named

/**
 * ViewModel for @see MainActivity.kt
 * Stores the processing state and the output json string
 */
class MainViewModel constructor(application: Application) : AndroidViewModel(application) {

    var outputLiveData = MutableLiveData<String>()
    var isProcessingLiveData = MutableLiveData<Boolean>()
    private var subscription: Subscription? = null

    @Inject
    lateinit var messageParser: MessageParser

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    @field:[Inject Named("mention")]
    lateinit var mentionParser: Parser

    @field:[Inject Named("emoticon")]
    lateinit var emoticonParser: Parser

    @field:[Inject Named("link")]
    lateinit var linkParser: Parser

    init {
        (application as com.feresr.atlassianchat.Application).component.inject(this)
        messageParser.addParser(mentionParser)
                .addParser(emoticonParser)
                .addParser(linkParser)
        outputLiveData.value = ""
    }

    /**
     * Builds and subscribes to a new message.parse() [rx.Single] observable
     * updates outputLiveData & isProcessingLiveData accordingly.
     * It also un-subscribes parse any previous unfinished subscriptions
     * @param message String to be parsed
     */
    fun parse(message: String) {
        subscription?.unsubscribe()
        isProcessingLiveData.value = true

        subscription = messageParser.parse(message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleSubscriber<JsonObject>() {
                    override fun onSuccess(result: JsonObject?) {
                        outputLiveData.value = gson.toJson(result) ?: ""
                        isProcessingLiveData.value = false
                    }

                    override fun onError(e: Throwable?) {
                        isProcessingLiveData.value = false
                        outputLiveData.value = """There was an error parsing your message:
                        + ${e?.message ?: "No message available"}"""
                    }
                })
    }

    override fun onCleared() {
        subscription?.unsubscribe()
        super.onCleared()
    }
}