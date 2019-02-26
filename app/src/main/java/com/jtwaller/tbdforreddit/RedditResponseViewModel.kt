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
    val mRedditLinkLiveData = MutableLiveData<ArrayList<RedditLink>>()

    fun getLinks() {
        GlobalScope.async (Dispatchers.IO) {
            val redditService = RedditApiService.create()

            val request = redditService.getJson()
            val response = request.await()

            // TODO: Error handling
            val body = response.body() ?: return@async

            for(child in body.listingData.children) {
                mRedditLinkList.add(child)
            }

            // cannot use setValue() from background thread!!
            mRedditLinkLiveData.postValue(mRedditLinkList)
        }
    }
}
