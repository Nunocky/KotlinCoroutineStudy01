package org.nunocky.kotlincoroutinestudy01

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainViewModel : ViewModel() {
    val executing = MutableLiveData(false)
    val text = MutableLiveData("")

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun execTask() {
        if (true) {
            // 方法 1
            executing.value = true
            text.value = ""

            viewModelScope.launch(context = Dispatchers.IO) {
                val strs = getStrings()
                text.postValue(strs.joinToString(" "))
            }
        } else {
            // 方法 2
            viewModelScope.launch(context = Dispatchers.IO) {
                val callback = object : TaskX.Callback {
                    override fun onStart() {
                        executing.postValue(true)
                        text.postValue("processing...")
                    }

                    override fun onCompleted(strs: Array<out String>) {
                        executing.postValue(false)
                        text.postValue(strs.joinToString(" "))
                    }
                }

                val taskX = TaskX()
                taskX.setCallback(callback)
                taskX.execute()
            }
        }
    }

    private suspend fun getStrings(): List<String> = suspendCoroutine { continuation ->
        val callback = object : TaskX.Callback {
            override fun onStart() {
            }

            override fun onCompleted(strs: Array<out String>) {
                continuation.resume(strs.toList())
            }
        }

        val taskX = TaskX()
        taskX.setCallback(callback)
        taskX.execute()
        return@suspendCoroutine
    }
}