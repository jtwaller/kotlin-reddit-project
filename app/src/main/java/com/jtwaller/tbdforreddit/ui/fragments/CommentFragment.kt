package com.jtwaller.tbdforreddit.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.jtwaller.tbdforreddit.R
import com.jtwaller.tbdforreddit.models.RedditCommentListingObject
import com.jtwaller.tbdforreddit.models.RedditLinkListingObject
import com.jtwaller.tbdforreddit.network.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class CommentFragment : Fragment() {

    companion object {
        const val TAG = "CommentFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        GlobalScope.async {
            val redditService = RedditApiService.get()

            val permalink = "r/hearthstone/comments/b3vald/i_felt_this_deep_in_my_core/"

            val request = redditService.fetchCommentsPermalink(permalink)
            val response = request.await()

            val jsonElement = response.body() ?: return@async

            val linkList = Gson().fromJson(jsonElement.asJsonArray.get(0), RedditLinkListingObject::class.java)
            val commentList = Gson().fromJson(jsonElement.asJsonArray.get(1), RedditCommentListingObject::class.java)

            Log.d(TAG, ": ${linkList.data.children.get(0).data.title}")
            Log.d(TAG, ": ${commentList.data.children.get(0).data.body}")
        }

        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

}