package com.jtwaller.tbdforreddit.ui.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jtwaller.tbdforreddit.ui.fragments.CommentFragment
import com.jtwaller.tbdforreddit.ui.fragments.PostListFragment

class MainFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        const val TAG = "MainFragmentPagerAdapte"
    }

    override fun getItem(position: Int): Fragment {
        Log.d(TAG, ": getItem position: $position")
        return when (position) {
            0 -> {
                PostListFragment()
            }
            else -> {
                CommentFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

}