package com.jtwaller.tbdforreddit

import android.content.*
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.navigation.NavigationView
import com.jtwaller.tbdforreddit.debug.DebugUtils
import com.jtwaller.tbdforreddit.models.RedditObjectData
import com.jtwaller.tbdforreddit.services.MainBroadcastReceiver
import com.jtwaller.tbdforreddit.ui.fragments.DetailFragment
import net.danlew.android.joda.JodaTimeAndroid
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    private lateinit var mBroadcastReceiver: MainBroadcastReceiver
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView

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

        drawerLayout = findViewById(R.id.main_layout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        // TODO - why does this setup the home button to pull our the drawer???
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.main_navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sign_in -> Toast.makeText(this, "Sign in tapped", Toast.LENGTH_SHORT).show()
                else -> throw RuntimeException("Invalid navigation menu item id")
            }
            true
        }

//        DebugUtils.broadcastLinkData(this, "r/Frugal/comments/b8wwi4/frugal_tips_why_my_coworkers_make_fun_of_me/")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    fun createDetailFragment(parentObjectData: RedditObjectData) {
        supportFragmentManager
                .beginTransaction()
                .apply {
                    replace(R.id.post_list_fragment, DetailFragment.newInstance(parentObjectData))
                    addToBackStack(null)
                    setCustomAnimations(
                            R.anim.enter_from_right,
                            R.anim.exit_to_right,
                            R.anim.enter_from_right,
                            R.anim.exit_to_right)
                    commit()
                }
    }

}
