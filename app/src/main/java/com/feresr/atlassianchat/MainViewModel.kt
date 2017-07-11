package com.feresr.atlassianchat

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.feresr.parser.MessageParser
import com.feresr.parser.Parser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named

/**
 * ViewModel for @see MainActivity.kt
 * Stores the processing state and the output json string
 */
class MainViewModel constructor(application: Application) : AndroidViewModel(application) {

    var outputLiveData = MutableLiveData<String>()
    var isProcessingLiveData = MutableLiveData<Boolean>()
    private var disposable: Disposable? = null

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
        disposable?.dispose()
        isProcessingLiveData.value = true

        disposable = messageParser.parse(message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    outputLiveData.value = gson.toJson(result) ?: ""
                    isProcessingLiveData.value = false
                }, {
                    e ->
                    isProcessingLiveData.value = false
                    outputLiveData.value = """There was an error parsing your message:
                        + ${e.message ?: "No message available"}"""
                })
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}