package com.jtwaller.tbdforreddit.network

import com.google.gson.annotations.SerializedName

class RedditObject(
        val kind: String,
        @SerializedName("data")
        val listingData: RedditListingData
)

class RedditListingData(
        val children: List<RedditLink>,
        val after: String,
        val before: String,
        val title: String,
        val url: String
)

class RedditLink(
        val kind: String,
        val data: RedditLinkData
)

class RedditLinkData(
        val subreddit: String,
        val title: String,
        val permalink: String,
        val url: String
)