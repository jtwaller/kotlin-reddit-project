package com.jtwaller.tbdforreddit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jtwaller.tbdforreddit.network.RedditApiService
import com.jtwaller.tbdforreddit.network.RedditLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RedditResponseViewModel : ViewModel() {

    init {
        getLinks()
    }

    val mRedditLinkList = ArrayList<RedditLink>()
    val mRedditLinkListSize = MutableLiveData<Int>()

    var after: String? = null
    var isLoading = false

    fun getLinks() {
        if (isLoading) return

        isLoading = true

        GlobalScope.async (Dispatchers.IO) {
            val redditService = RedditApiService.create()

            val request = redditService.getJson(after)
            val response = request.await()

            // TODO: Error handling
            val body = response.body() ?: return@async

            after = body.listingData.after

            for(child in body.listingData.children) {
                mRedditLinkList.add(child)
            }

            // cannot use setValue() from background thread!!
            mRedditLinkListSize.postValue(mRedditLinkList.size)

            isLoading = false
        }
    }
}
