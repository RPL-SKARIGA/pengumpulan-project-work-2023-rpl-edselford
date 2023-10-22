package com.edselmustapa.mywallet.lib

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edselmustapa.mywallet.service.Answer
import com.edselmustapa.mywallet.service.Discussion
import com.edselmustapa.mywallet.service.DiscussionService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class DiscussionViewModel : ViewModel() {
    private val discussionService = DiscussionService()

    private val _discussions = MutableStateFlow(emptyList<Discussion>())
    val discussion = _discussions.asStateFlow()

    private val _answers = MutableStateFlow(emptyList<Answer>())
    val answers = _answers.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _sendLoading = MutableStateFlow(false)
    val sendLoading = _sendLoading.asStateFlow()

    init {
        println("hello")
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _loading.value = true
            _discussions.value = try {
                discussionService.getDiscussion()
            } catch (e: Exception) {
                println(e.message)
                emptyList()
            }
            delay(1000)
            _loading.value = false
        }
    }

    fun getAnswer(questionId: String, withLoading: Boolean = true) {
        viewModelScope.launch {
            if (withLoading) _loading.value = true
            _answers.value = try {
                discussionService.getAnswer(questionId)
            } catch (e: Exception) {
                println(e.message)
                emptyList()
            }
            if (withLoading) _loading.value = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun sendAnswer(
        questionId: String,
        owner: String,
        content: String,
        email: String,
        callback: suspend () -> Unit
    ) {
        viewModelScope.launch {
            _sendLoading.value = true
            val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            currentDate.timeZone = TimeZone.getTimeZone("GMT+7")
            discussionService.sendAnswer(
                questionId, owner, currentDate.format(Date()), content, email
            )
            getAnswer(questionId, withLoading = false)
            callback()
            _sendLoading.value = false
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun createDiscussion(
        topic: String,
        content: String,
        email: String,
        callback: suspend () -> Unit
    ) {
        viewModelScope.launch {
            _sendLoading.value = true
            val dateFormat = SimpleDateFormat("dd MMMM yyyy 'at' hh:mm")
            dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")

            discussionService.createDiscussion(topic, content, email, dateFormat.format(Date()))
            refresh()
            callback()
            _sendLoading.value = false
        }
    }
}