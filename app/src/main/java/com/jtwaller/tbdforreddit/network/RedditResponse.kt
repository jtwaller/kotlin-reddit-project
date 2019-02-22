package com.jtwaller.tbdforreddit.network

import com.google.gson.annotations.SerializedName

class RedditObject(
        val kind: String,
        @SerializedName("data")
        val redditData: RedditListingData
)

class RedditListingData(
        val children: List<RedditObject>,
        val after: String,
        val before: String,
        val title: String,
        val url: String
)