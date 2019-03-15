package com.jtwaller.tbdforreddit.network

import com.google.gson.annotations.SerializedName
import org.joda.time.Period
import java.net.URL

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
        val author: String,
        val title: String,
        val permalink: String,
        val url: String,
        val thumbnail: String,
        val score: Int,
        val gilded: Int,
        val created_utc: Long,
        val num_comments: Int,
        val over_18: Boolean,
        val pinned: Boolean
) {
    fun getAgePeriod(): Period {
        // reddit time is in s, divide system time by 1000
        val nowInSecs = System.currentTimeMillis() / 1000
        return Period.seconds((nowInSecs - created_utc).toInt())
    }

    fun getUpvoteCount(): String {
        return if (score < 1000) {
            score.toString()
        } else {
            String.format("%.1fk", score.toFloat() / 1000)
        }
    }

    fun getCommentCount(): String {
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