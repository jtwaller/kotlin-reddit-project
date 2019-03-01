package com.jtwaller.tbdforreddit.network

import com.google.gson.annotations.SerializedName

class RedditObject(
        val kind: String,
        @SerializedName("data")
        val listingData: RedditListingData
)

class RedditListingData(
        val children: List<RedditT3>,
        val after: String,
        val before: String
)

class RedditT3(
        val kind: String,
        val data: RedditT3Data
)

class RedditT3Data(
        val subreddit: String,
        val title: String,
        val permalink: String,
        val url: String,
        val thumbnail: String
)