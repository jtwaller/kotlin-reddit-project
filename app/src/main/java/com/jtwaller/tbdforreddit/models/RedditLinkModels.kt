package com.jtwaller.tbdforreddit.models

import android.os.Parcel
import android.os.Parcelable
import java.lang.IllegalStateException
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
) : RedditDataObject, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readString() ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readString()  ?: throw IllegalStateException("Invalid RedditLinkData"),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte())

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeInt(score)
        parcel.writeInt(gilded)
        parcel.writeLong(created_utc)
        parcel.writeString(permalink)
        parcel.writeString(subreddit)
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeString(thumbnail)
        parcel.writeInt(num_comments)
        parcel.writeByte(if (over_18) 1 else 0)
        parcel.writeByte(if (pinned) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RedditLinkData> {
        override fun createFromParcel(parcel: Parcel): RedditLinkData {
            return RedditLinkData(parcel)
        }

        override fun newArray(size: Int): Array<RedditLinkData?> {
            return arrayOfNulls(size)
        }
    }
}