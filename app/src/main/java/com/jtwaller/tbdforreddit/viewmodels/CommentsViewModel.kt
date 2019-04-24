package com.jtwaller.tbdforreddit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.CHILDREN
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.COMMENT
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.DATA
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.KIND
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.LISTING
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.MORE
import com.jtwaller.tbdforreddit.models.JsonConstants.Companion.REPLIES
import com.jtwaller.tbdforreddit.models.RedditComment
import com.jtwaller.tbdforreddit.network.RedditApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.lang.StringBuilder

class CommentsViewModel: ViewModel() {

    companion object {
        const val TAG = "CommentsViewModel"
    }

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val commentList = ArrayList<RedditComment>()
    val gson = Gson()

    lateinit var s: String

    init {
        isLoading.value = true
    }

    fun load(permalink: String) {
        isLoading.value = true

        GlobalScope.launch {
            val redditService = RedditApiService.get()

            val request = redditService.fetchCommentsPermalink(permalink)
            val response = request.await()

            val jsonElement = response.body() ?: return@launch
            val jsonCommentsElement = jsonElement.asJsonArray.get(1)

            populateCommentList(commentList, jsonCommentsElement)

            val sb = StringBuilder()

            for (c in commentList) {
                val indent = "|-" + "--".repeat(c.depth)
                sb.append("$indent${c.author}\n")
            }

            s = sb.toString()

            isLoading.postValue(false)
        }
    }

    fun populateCommentList(commentList: ArrayList<RedditComment>, input: JsonElement) {
        if (input.asJsonObject.get("kind").asString != LISTING) throw RuntimeException("Invalid list input")

        val comments = input.asJsonObject.get("data").asJsonObject.get("children").asJsonArray

        for(c in comments) {
            parseComment(commentList, c)
        }

    }

    fun parseComment(commentList: ArrayList<RedditComment>, comment: JsonElement) {
        when (comment.asJsonObject.get(KIND).asString) {
            COMMENT -> {
                commentList.add(
                        gson.fromJson(comment.asJsonObject.get(DATA), RedditComment::class.java))
                parseReplies(commentList, comment.asJsonObject.get(DATA).asJsonObject.get(REPLIES))
            }
            MORE -> {} // TODO
            else -> throw RuntimeException("Unexpected type found parsing comment: ${comment.asJsonObject.get(KIND)}")
        }
    }

    fun parseReplies(commentList: ArrayList<RedditComment>, replies: JsonElement) {
        /*
         *  When comment has no replies, json response will contain replies as "replies": ""
         *  When comment has replies, json response will be an object with a kind & listing fields
         */
        if (!replies.isJsonObject && replies.asString == "") return

        val repliesObject = replies.asJsonObject

        if (repliesObject.get(KIND).asString != LISTING) throw RuntimeException("Invalid reply list")

        for (reply in repliesObject.get(DATA).asJsonObject.get(CHILDREN).asJsonArray) {
            parseComment(commentList, reply)
        }
    }

}