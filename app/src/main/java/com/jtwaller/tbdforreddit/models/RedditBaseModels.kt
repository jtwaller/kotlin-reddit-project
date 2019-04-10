package com.jtwaller.tbdforreddit.models

class RedditListing(
        val kind: String,
        val data: RedditListingData
)

class RedditListingData(
        val after: String,
        val before: String,
        val children: List<RedditLinkObject>
)

class RedditLinkObject(
        val kind: String,
        val data: RedditObjectData
)

