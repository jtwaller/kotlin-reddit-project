package com.jtwaller.tbdforreddit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jtwaller.tbdforreddit.network.RedditApiService
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        GlobalScope.async (Dispatchers.IO) {
            val redditService = RedditApiService.create()

            Log.d(TAG, "Fetching json")
            val request = redditService.getJson()
            val response = request.await()

            Log.d(TAG, ": json received... kind = " + response.body()?.kind ?: "null" )
        }
    }

}
