package com.jtwaller.tbdforreddit.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jtwaller.tbdforreddit.BuildConfig
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RedditApiService {

    @GET(".json")
    fun getJson(): Deferred<Response<RedditObject>>

    companion object Factory {
            fun create(): RedditApiService {
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

                return retrofit.create(RedditApiService::class.java)
            }
    }

}