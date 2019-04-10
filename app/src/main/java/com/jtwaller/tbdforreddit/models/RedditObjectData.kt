package com.jtwaller.tbdforreddit.models

import android.os.Parcel
import android.os.Parcelable
import org.joda.time.Period
import java.lang.IllegalStateException
import java.net.URL

class RedditObjectData (
        val author: String,
        val score: Int,
        val gilded: Int,
        val created_utc: Long,
        val permalink: String,
        val subreddit: String,
        val title: String,
        val selftext: String,
        val url: String,
        val preview: RedditObjectPreview?,
        val thumbnail: String,
        val num_comments: Int,
        val over_18: Boolean,
        val pinned: Boolean
) : Parcelable {

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

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readParcelable<RedditObjectPreview>(RedditObjectPreview::class.java.classLoader),
            parcel.readString()  ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeInt(score)
        parcel.writeInt(gilded)
        parcel.writeLong(created_utc)
        parcel.writeString(permalink)
        parcel.writeString(subreddit)
        parcel.writeString(title)
        parcel.writeString(selftext)
        parcel.writeString(url)
        parcel.writeParcelable(preview, flags)
        parcel.writeString(thumbnail)
        parcel.writeInt(num_comments)
        parcel.writeByte(if (over_18) 1 else 0)
        parcel.writeByte(if (pinned) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RedditObjectData> {
        override fun createFromParcel(parcel: Parcel): RedditObjectData {
            return RedditObjectData(parcel)
        }

        override fun newArray(size: Int): Array<RedditObjectData?> {
            return arrayOfNulls(size)
        }
    }

}