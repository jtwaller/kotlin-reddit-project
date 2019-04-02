package com.jtwaller.tbdforreddit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jtwaller.tbdforreddit.network.RedditApiService
import com.jtwaller.tbdforreddit.models.RedditLinkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RedditLinkListViewModel : ViewModel() {

    init {
        getLinks()
    }

    val redditLinkList = ArrayList<RedditLinkObject>()
    val redditLinkListSize = MutableLiveData<Int>()

    var mAfter: String? = null
    var mIsLoading = false

    fun getLinks() {
        if (mIsLoading) return

        mIsLoading = true

        GlobalScope.async (Dispatchers.IO) {
            val redditService = RedditApiService.get()

            val request = redditService.getJson(mAfter)
            val response = request.await()

            // TODO: Error handling
            val body = response.body() ?: return@async

            mAfter = body.data.after

            for(child in body.data.children) {
                redditLinkList.add(child)
            }

            // cannot use setValue() from background thread
            redditLinkListSize.postValue(redditLinkList.size)

            mIsLoading = false
        }
    }
}
