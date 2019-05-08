package com.jtwaller.tbdforreddit.network

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

    companion object {
        const val userAgent = "android:com.jtwaller.tbdforreddit:0.1.0 (by /u/InternetProfessional)"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request()
                .newBuilder()
                .addHeader("User-Agent", userAgent)
                .build()
        )
    }

}