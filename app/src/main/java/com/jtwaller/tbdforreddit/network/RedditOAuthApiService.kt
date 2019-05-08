package com.jtwaller.tbdforreddit.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jtwaller.tbdforreddit.BuildConfig
import com.jtwaller.tbdforreddit.models.RedditMe
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface RedditOAuthApiService {

    @GET("api/v1/me")
    fun getIdentity(@Header("Authorization") token: String) : Deferred<Response<RedditMe>>

    companion object {
        val instance: RedditOAuthApiService by lazy {
            val clientBuilder = OkHttpClient.Builder()
            clientBuilder.apply {
                addInterceptor(UserAgentInterceptor())
                if (BuildConfig.DEBUG) {
                    addNetworkInterceptor(
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }

            val retrofit = Retrofit.Builder()
                    .client(clientBuilder.build())
                    .baseUrl("https://oauth.reddit.com/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            retrofit.create(RedditOAuthApiService::class.java)
        }
    }

}