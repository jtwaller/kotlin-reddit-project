package com.jtwaller.tbdforreddit.debug

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.jtwaller.tbdforreddit.MainActivity
import com.jtwaller.tbdforreddit.models.RedditListing
import com.jtwaller.tbdforreddit.network.RedditApiService
import com.jtwaller.tbdforreddit.ui.adapters.PostListAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DebugUtils {

    companion object {
        const val TAG = "DebugUtils"

        fun broadcastLinkData(ctx: Context, permalink: String) {
            GlobalScope.launch {
                val redditService = RedditApiService.instance

                val request = redditService.fetchCommentsPermalink(permalink)
                val response = request.await()

                val jsonElement = response.body() ?: return@launch
                val redditLinkListingObject = Gson().fromJson(jsonElement.asJsonArray.get(0), RedditListing::class.java)

                val mIntent = Intent().apply {
                    component = ComponentName(ctx, MainActivity::class.java)
                    action = MainActivity.BUILD_FRAGMENT_ACTION
                    putExtra(PostListAdapter.REDDIT_LINK_DATA, redditLinkListingObject.data.children.get(0).data)
                }

                LocalBroadcastManager.getInstance(ctx).sendBroadcast(mIntent)
            }
        }
    }

}