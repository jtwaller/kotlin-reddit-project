package com.jtwaller.tbdforreddit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.textView1)

        tv.text = "Loading"

        val mViewModel = ViewModelProviders.of(this).get(RedditResponseViewModel::class.java)
        mViewModel.mRedditLinkLiveData.observe(this, Observer { list ->
            val sb = StringBuilder()
            for (l in list) {
                sb.append("${l.data.title}\n")
            }
            tv.text = sb
        })
    }

}
