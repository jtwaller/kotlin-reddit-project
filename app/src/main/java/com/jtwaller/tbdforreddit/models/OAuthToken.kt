package com.jtwaller.tbdforreddit.models

class OAuthToken(
        val access_token: String,
        val token_type: String,
        val expires_in: Int,
        val scope: String,
        val refresh_token: String
)