package com.jtwaller.tbdforreddit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.reddit.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

        GlobalScope.launch (Dispatchers.Main) {

        }

    }
}
