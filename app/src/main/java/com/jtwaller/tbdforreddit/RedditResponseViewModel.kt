package com.jtwaller.tbdforreddit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jtwaller.tbdforreddit.network.RedditApiService
import com.jtwaller.tbdforreddit.network.RedditT3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RedditResponseViewModel : ViewModel() {

    init {
        getLinks()
    }

    val mRedditLinkList = ArrayList<RedditT3>()
    val mRedditLinkListSize = MutableLiveData<Int>()

    var mAfter: String? = null
    var mIsLoading = false

    fun getLinks() {
        if (mIsLoading) return

        mIsLoading = true

        GlobalScope.async (Dispatchers.IO) {
            val redditService = RedditApiService.create()

            val request = redditService.getJson(mAfter)
            val response = request.await()

            // TODO: Error handling
            val body = response.body() ?: return@async

            mAfter = body.listingData.after

            for(child in body.listingData.children) {
                mRedditLinkList.add(child)
            }

            // cannot use setValue() from background thread!!
            mRedditLinkListSize.postValue(mRedditLinkList.size)

            mIsLoading = false
        }
    }
}
