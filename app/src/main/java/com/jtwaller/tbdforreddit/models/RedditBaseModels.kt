package com.jtwaller.tbdforreddit.models

import org.joda.time.Period

class RedditListing(
        val kind: String,
        val data: RedditListingData
)

class RedditListingData(
        val after: String,
        val before: String,
        val children: List<RedditObject>
)

class RedditObject(
        val kind: String,
        val data: RedditObjectData
)

class RedditComment(
        val author: String,
        val score: Int,
        val age: Period,
        val body: String,
        val depth: Int
)


class JsonConstants {
    companion object {
        const val KIND = "kind"
        const val DATA = "data"
        const val LISTING = "Listing"
        const val COMMENT = "t1"
        const val POST = "t3"
        const val MORE = "more"
        const val REPLIES = "replies"
        const val CHILDREN = "children"
    }
}