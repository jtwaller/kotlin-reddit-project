package com.jtwaller.tbdforreddit.models

import java.net.URL

class RedditLinkListingObject(
        val kind: String,
        val data: RedditLinkListingData
)

class RedditLinkListingData(
        override val after: String,
        override val before: String,
        override val children: List<RedditLinkObject>
) : RedditListingData

class RedditLinkObject(
        val kind: String,
        val data: RedditLinkData
)

class RedditLinkData(
        override val author: String,
        override val score: Int,
        override val gilded: Int,
        override val created_utc: Long,
        override val permalink: String,
        val subreddit: String,
        val title: String,
        val url: String,
        val thumbnail: String,
        val num_comments: Int,
        val over_18: Boolean,
        val pinned: Boolean
) : RedditDataObject {
    fun getShortFormatCommentCount(): String {
        return if (num_comments < 1000) {
            num_comments.toString()
        } else {
            String.format("%.1fk", num_comments.toFloat() / 1000)
        }
    }

    fun getDomain(): String {
        val host = URL(url).host
        return if (host.startsWith("www.")) host.substring(4) else host
    }
}