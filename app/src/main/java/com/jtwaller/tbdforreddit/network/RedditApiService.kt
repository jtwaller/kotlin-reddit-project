package com.jtwaller.tbdforreddit.network

import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jtwaller.tbdforreddit.BuildConfig
import com.jtwaller.tbdforreddit.models.RedditLinkListingObject
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApiService {

    @GET(".json")
    fun getJson(@Query("after") after: String?): Deferred<Response<RedditLinkListingObject>>

    @GET("{permalink}.json")
    fun fetchCommentsPermalink(@Path("permalink", encoded = true) permalink: String) : Deferred<Response<JsonElement>>

    companion object {
        private var instance: RedditApiService? = null

        fun get(): RedditApiService {
            if (instance == null) {
                val clientBuilder = OkHttpClient.Builder()

                if (BuildConfig.DEBUG) {
                    clientBuilder.addInterceptor(
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
                    )
                }

                val retrofit = Retrofit.Builder()
                        .client(clientBuilder.build())
                        .baseUrl("https://www.reddit.com/")
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                instance = retrofit.create(RedditApiService::class.java)
            }
            return instance!!
        }
    }

}