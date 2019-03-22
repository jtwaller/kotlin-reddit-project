package com.jtwaller.tbdforreddit.models

import org.joda.time.Period

interface RedditDataObject {

    val author: String
    val score: Int
    val gilded: Int
    val created_utc: Long
    val permalink: String

    fun getAgePeriod(): Period {
        // reddit time is in s, divide system time by 1000
        val nowInSecs = System.currentTimeMillis() / 1000
        return Period.seconds((nowInSecs - created_utc).toInt())
    }

    fun getShortFormatScore(): String {
        return if (score < 1000) {
            score.toString()
        } else {
            String.format("%.1fk", score.toFloat() / 1000)
        }
    }

}