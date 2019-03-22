package com.jtwaller.tbdforreddit.models


class RedditCommentListingObject(
        val kind: String,
        val data: RedditCommentListingData
)

class RedditCommentListingData(
        override val after: String,
        override val before: String,
        override val children: List<RedditCommentObject>
) : RedditListingData

class RedditCommentObject(
        val kind: String,
        val data: RedditCommentData
)

class RedditCommentData(
        override val author: String,
        override val score: Int,
        override val gilded: Int,
        override val created_utc: Long,
        override val permalink: String,
        val body: String,
        val thumbnail: String
//        val replies: Listing......... // TODO
) : RedditDataObject