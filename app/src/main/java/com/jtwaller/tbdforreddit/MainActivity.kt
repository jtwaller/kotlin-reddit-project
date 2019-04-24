package com.jtwaller.tbdforreddit

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jtwaller.tbdforreddit.debug.DebugUtils
import com.jtwaller.tbdforreddit.models.RedditObjectData
import com.jtwaller.tbdforreddit.ui.adapters.PostListAdapter.Companion.REDDIT_LINK_DATA
import com.jtwaller.tbdforreddit.ui.fragments.DetailFragment
import net.danlew.android.joda.JodaTimeAndroid
import java.lang.ClassCastException

class MainActivity : AppCompatActivity() {

    private lateinit var mBroadcastReceiver: MainBroadcastReceiver

    companion object {
        const val TAG = "MainActivity"
        const val BUILD_FRAGMENT_ACTION = "BUILD_FRAGMENT_ACTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JodaTimeAndroid.init(this)
        mBroadcastReceiver = MainBroadcastReceiver(this)

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(mBroadcastReceiver, IntentFilter(BUILD_FRAGMENT_ACTION))

        DebugUtils.broadcastLinkData(this, "r/Frugal/comments/b8wwi4/frugal_tips_why_my_coworkers_make_fun_of_me/")
    }

    fun createDetailFragment(redditObjectData: RedditObjectData) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val commentFragment = DetailFragment.newInstance(redditObjectData)

        fragmentTransaction.apply {
            setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_right,
                    R.anim.enter_from_right,
                    R.anim.exit_to_right)
            replace(R.id.post_list_fragment, commentFragment)
            addToBackStack(null)
            commit()
        }
    }

    class MainBroadcastReceiver(private val parent: MainActivity) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                val mRedditLinkData = intent?.extras?.get(REDDIT_LINK_DATA) as RedditObjectData
                parent.createDetailFragment(mRedditLinkData)
            } catch (e: ClassCastException) {
                AlertDialog.Builder(parent)
                        .setMessage(R.string.invalid_linkdata_broadcast)
                        .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
            }
        }
    }

}
