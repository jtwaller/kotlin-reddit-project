package com.jtwaller.tbdforreddit.models

import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class OAuthToken(
        val access_token: String,
        val token_type: String,
        val expiresInSeconds: Long,
        val scope: String,
        val refresh_token: String
) {

    private val expireTimeUtc: Long

    init {
        expireTimeUtc = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expiresInSeconds)
    }

    companion object {
        fun fromApiFragment(authFragment: String): OAuthToken {
            try {
                val parameters = authFragment.split('&')

                // Couldn't find an easy way to parse uri fragment parameters?
                // E.g., uri.getQueryParameter(someKey) vs uri.getFragmentParameter(someKey)
                val accessToken = parameters[0].split('=')[1]
                val tokenType = parameters[1].split('=')[1]
                val state = parameters[2].split('=')[1]
                val expiresInSeconds = parameters[3].split('=')[1]
                val scope = parameters[4].split('=')[1]

                if (tokenType != "bearer") {
                    throw RuntimeException("Invalid token type - ${tokenType}")
                }

                return OAuthToken(
                        accessToken,
                        tokenType,
                        expiresInSeconds.toLong(),
                        state,
                        scope)
            } catch (e: Exception) {
                // TODO - validate auth fragment and handle errors

                e.printStackTrace()
                throw RuntimeException("Invalid auth fragment - $authFragment")
            }
        }
    }

    fun getAccessToken(): String {
        return access_token
    }

    fun isExpired(): Boolean {
        return (expireTimeUtc > System.currentTimeMillis())
    }
}