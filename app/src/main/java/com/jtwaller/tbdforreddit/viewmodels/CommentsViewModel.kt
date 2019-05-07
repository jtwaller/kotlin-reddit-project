package com.jtwaller.tbdforreddit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jtwaller.tbdforreddit.data
import com.jtwaller.tbdforreddit.getChildren
import com.jtwaller.tbdforreddit.getReplies
import com.jtwaller.tbdforreddit.kind
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.COMMENT
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.LISTING
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.MORE
import com.jtwaller.tbdforreddit.models.RedditComment
import com.jtwaller.tbdforreddit.network.RedditApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class CommentsViewModel: ViewModel() {

    companion object {
        const val TAG = "CommentsViewModel"
    }

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val commentList = ArrayList<RedditComment>()
    val gson = Gson()

    init {
        isLoading.value = true
    }

    fun load(permalink: String) {
        isLoading.value = true

        GlobalScope.launch {
            val redditService = RedditApiService.instance

            val request = redditService.fetchCommentsPermalink(permalink)
            val response = request.await()

            val jsonElement = response.body() ?: return@launch
            val jsonCommentsElement = jsonElement.asJsonArray.get(1)

            populateCommentList(commentList, jsonCommentsElement)

            isLoading.postValue(false)
        }
    }

    fun populateCommentList(commentList: ArrayList<RedditComment>, input: JsonElement) {
        for(comment in input.getChildren()) {
            parseComment(commentList, comment)
        }
    }

    fun parseComment(commentList: ArrayList<RedditComment>, comment: JsonElement) {
        when (comment.kind) {
            COMMENT -> {
                commentList.add(gson.fromJson(comment.data, RedditComment::class.java))
                parseReplies(commentList, comment.getReplies())
            }
            MORE -> {} // TODO
            else -> throw RuntimeException("Unexpected type found parsing comment: ${comment.kind}")
        }
    }

    fun parseReplies(commentList: ArrayList<RedditComment>, replies: JsonElement) {
         // "replies" field is empty string when comment has no replies
        if (replies.isJsonObject) {
            if (replies.kind != LISTING) throw RuntimeException("Invalid reply list")
            for (reply in replies.getChildren()) {
                parseComment(commentList, reply)
            }
        }
    }

}