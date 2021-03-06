package com.feresr.atlassianchat

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : LifecycleActivity() {

    lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.outputLiveData.observe(this, Observer<String> { outputString ->
            messageResultTextView.text = outputString
        })

        viewModel.isProcessingLiveData.observe(this, Observer<Boolean> { processing ->
            if (processing!!) { // should never be null
                progressBar.visibility = View.VISIBLE
                messageResultTextView.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                messageResultTextView.visibility = View.VISIBLE
            }
        })

        parseBtn.setOnClickListener { onParseBtnClicked() }
    }

    private fun onParseBtnClicked() {
        val msg = messageEditText.text.toString()
        messageTextView.text = msg
        messageEditText.text.clear()
        hideKeyboard()
        viewModel.parse(msg)
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
