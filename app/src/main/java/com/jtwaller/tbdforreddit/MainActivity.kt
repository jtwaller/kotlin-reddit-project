package com.jtwaller.tbdforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.danlew.android.joda.JodaTimeAndroid

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JodaTimeAndroid.init(this)
    }

}
