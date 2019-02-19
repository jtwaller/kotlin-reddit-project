package com.jtwaller.tbdforreddit.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET


interface RedditApi {

    @GET("")
    fun getJson(): Deferred<Response<RedditResponse>>

}