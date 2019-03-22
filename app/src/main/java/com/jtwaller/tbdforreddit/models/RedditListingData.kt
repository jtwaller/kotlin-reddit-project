package com.jtwaller.tbdforreddit.models

interface RedditListingData {
    val before: String
    val after: String
    val children: List<*>
}