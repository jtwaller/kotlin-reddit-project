package com.jtwaller.tbdforreddit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.jtwaller.tbdforreddit.ui.adapters.MainFragmentPagerAdapter
import net.danlew.android.joda.JodaTimeAndroid

class MainActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerAdapter: MainFragmentPagerAdapter
    private lateinit var mBroadcastReceiver: MainBroadcastReceiver

    companion object {
        const val TAG = "MainActivity"
        const val BUILD_FRAGMENT_ACTION = "BUILD_FRAGMENT_ACTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JodaTimeAndroid.init(this)
        mBroadcastReceiver = MainBroadcastReceiver()

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(mBroadcastReceiver, IntentFilter(BUILD_FRAGMENT_ACTION))

        mPagerAdapter = MainFragmentPagerAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.pager)
        mViewPager.adapter = mPagerAdapter

        mViewPager.addOnPageChangeListener(
                object : ViewPager.SimpleOnPageChangeListener() {
                    override fun onPageSelected(position: Int) {
                        Log.d(TAG, ": onPageSelected $position")
                        mViewPager.currentItem = position
                    }
                }
        )
    }

    class MainBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, ": $intent")
        }
    }

}
