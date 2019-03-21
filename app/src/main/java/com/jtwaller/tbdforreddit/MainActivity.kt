package com.jtwaller.tbdforreddit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.jtwaller.tbdforreddit.ui.adapters.MainFragmentPagerAdapter
import net.danlew.android.joda.JodaTimeAndroid

class MainActivity : FragmentActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerAdapter: MainFragmentPagerAdapter

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JodaTimeAndroid.init(this)

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

}
