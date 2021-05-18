package com.example.japaneselanguageappfinal.ui.draw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is draw Fragment"
    }
    val text: LiveData<String> = _text
}