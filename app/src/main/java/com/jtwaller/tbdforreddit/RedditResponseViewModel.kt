package com.jtwaller.tbdforreddit

import android.util.Log
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
                Log.d("tag", ": ${child.data.thumbnail}")
            }

            // cannot use setValue() from background thread!!
            mRedditLinkListSize.postValue(mRedditLinkList.size)

            isLoading = false
        }
    }
}
