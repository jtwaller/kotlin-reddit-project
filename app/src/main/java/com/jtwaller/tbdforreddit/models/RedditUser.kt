package com.jtwaller.tbdforreddit.models

class RedditUser {

    private var oAuthToken: OAuthToken? = null

    companion object {
        const val TAG = "RedditUser"
        val instance = RedditUser()
    }

    fun isLoggedIn(): Boolean {
        val currToken = oAuthToken
        return (currToken != null && !currToken.isExpired())
    }

    fun setOAuthToken(token: OAuthToken) {
        oAuthToken = token
    }

    fun getTokenString(): String? {
        return oAuthToken?.getAccessToken()
    }

}