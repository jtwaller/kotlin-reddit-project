package com.jtwaller.tbdforreddit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jtwaller.tbdforreddit.network.RedditApiService
import com.jtwaller.tbdforreddit.models.RedditObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RedditLinkListViewModel : ViewModel() {

    init {
        getLinks()
    }

    val redditLinkList = ArrayList<RedditObject>()
    val redditLinkListSize = MutableLiveData<Int>()

    var mAfter: String? = null
    var mIsLoading = false

    fun getLinks() {
        if (mIsLoading) return

        mIsLoading = true

        GlobalScope.launch (Dispatchers.IO) {
            val redditService = RedditApiService.get()

            val request = redditService.getJson(mAfter)
            val response = request.await()

            // TODO: Error handling
            val body = response.body() ?: return@launch

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
