package com.jtwaller.tbdforreddit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.jtwaller.tbdforreddit.models.*
import com.jtwaller.tbdforreddit.network.RedditApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RedditCommentFragmentViewModel : ViewModel() {

    companion object {
        const val TAG = "CommentFragmentViewModel"
    }

    val isLoading: MutableLiveData<Boolean>

    lateinit var linkData: RedditLinkData
    lateinit var commentListingData: ArrayList<RedditCommentData>

    init {
        isLoading = MutableLiveData()
        isLoading.value = true

        GlobalScope.async {
            val redditService = RedditApiService.get()

            val permalink = "r/hearthstone/comments/b3vald/i_felt_this_deep_in_my_core/"

            val request = redditService.fetchCommentsPermalink(permalink)
            val response = request.await()

            val jsonElement = response.body() ?: return@async

            val linkList = Gson().fromJson(jsonElement.asJsonArray.get(0), RedditLinkListingObject::class.java)
            val commentList = Gson().fromJson(jsonElement.asJsonArray.get(1), RedditCommentListingObject::class.java)

            linkData = linkList.data.children.get(0).data

            isLoading.postValue(false)
        }

    }

}