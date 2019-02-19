package com.jtwaller.tbdforreddit.network


class RedditResponse(
        val redditObject: RedditObject
)

class RedditObject(
        val kind: String,
        val redditData: RedditListingData
)

class RedditListingData(
        val children: List<RedditObject>,
        val after: String,
        val before: String
)

class RedditT3Data(

)