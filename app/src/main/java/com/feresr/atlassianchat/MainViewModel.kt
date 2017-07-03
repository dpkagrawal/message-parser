package com.feresr.atlassianchat

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.feresr.atlassianchat.utils.MessageParser
import rx.SingleSubscriber
import rx.Subscription
import javax.inject.Inject

/**
 * ViewModel for @see MainActivity.kt
 * Stores the processing state and the output json string
 */
class MainViewModel constructor(application: Application) : AndroidViewModel(application) {

    var outputLiveData = MutableLiveData<String>()
    var isProcessingLiveData = MutableLiveData<Boolean>()
    var subscription: Subscription? = null

    @Inject
    lateinit var messageParser: MessageParser

    init {
        (application as com.feresr.atlassianchat.Application).component.inject(this)
        outputLiveData.value = ""
    }

    /**
     * Builds and subscribes to a new message.parse() Single observable
     * updates outputLiveData & isProcessingLiveData accordingly.
     * It also un-subscribes from any previous unfinished subscriptions
     * @param message String to be parsed
     */
    fun parse(message: String) {
        subscription?.unsubscribe()
        isProcessingLiveData.value = true

        subscription = messageParser.parse(message).subscribe(object : SingleSubscriber<String>() {
            override fun onSuccess(result: String?) {
                outputLiveData.value = result ?: ""
                isProcessingLiveData.value = false
            }

            override fun onError(e: Throwable?) {
                outputLiveData.value = """Hhmm... There was an error parsing your message, stats for nerds:
                 + ${e?.message ?: "No message available"}"""
                isProcessingLiveData.value = false
            }
        })

    }

    override fun onCleared() {
        subscription?.unsubscribe()
        super.onCleared()
    }
}