package com.jtwaller.tbdforreddit.models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.jtwaller.tbdforreddit.MainActivity
import com.jtwaller.tbdforreddit.R

class RedditUser {

    private var oAuthToken: OAuthToken? = null

    companion object {
        const val TAG = "RedditUser"
        val instance = RedditUser()
    }

    fun login(context: Context) {
        if (!instance.isLoggedIn()) {
            Log.d(TAG, ": Valid auth token exists")

            // TODO - show username in drawer
            return
        }

        // If not building, please create res/values/secrets.xml with your own client id!
        val clientId = context.getString(R.string.client_id)
        val responseType = "token"
        val state = "UNIQUE_STATE"
        val redirectUri = "app://open.tbdforreddit"
        val scope = "submit,identity"
        val duration = "temporary"

        val authUrl = "https://m.reddit.com/api/v1/authorize.compact?" +
                "client_id=$clientId" +
                "&response_type=$responseType" +
                "&state=$state" +
                "&duration=$duration" +
                "&redirect_uri=$redirectUri" +
                "&scope=$scope"

        Log.d(MainActivity.TAG, ": $authUrl")

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(authUrl)

        context.startActivity(intent)
    }

    fun isLoggedIn(): Boolean {
        val currToken = oAuthToken
        return (currToken != null && !currToken.isExpired())
    }

    fun setOAuthToken(token: OAuthToken) {
        oAuthToken = token
    }

}