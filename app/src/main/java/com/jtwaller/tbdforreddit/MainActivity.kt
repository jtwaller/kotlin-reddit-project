package com.jtwaller.tbdforreddit

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.navigation.NavigationView
import com.jtwaller.tbdforreddit.models.OAuthToken
import com.jtwaller.tbdforreddit.models.RedditObjectData
import com.jtwaller.tbdforreddit.models.RedditUser
import com.jtwaller.tbdforreddit.services.MainBroadcastReceiver
import com.jtwaller.tbdforreddit.ui.fragments.DetailFragment
import net.danlew.android.joda.JodaTimeAndroid
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    private lateinit var mBroadcastReceiver: MainBroadcastReceiver
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView

    val redditUser = RedditUser.instance

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

        // TODO - why does this create the button to pull out the drawer???
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.main_navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sign_in -> signIn()
                else -> throw RuntimeException("Invalid navigation menu item id")
            }
            true
        }

        intent?.data?.let { uri ->
            // TODO - graceful error handling
            val authFragment = uri.fragment ?: throw RuntimeException("No fragment data in Auth API response")
            redditUser.setOAuthToken(OAuthToken.fromApiFragment(authFragment))
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

    fun signIn() {
        if (redditUser.isLoggedIn()) {
            Log.d(TAG, ": Valid auth token exists")

            // TODO - show username in drawer
            return
        }

        // If not building, please create res/values/secrets.xml with your own client id!
        val clientId = getString(R.string.client_id)
        val responseType = "token"
        val state = "UNIQUE_STATE"
        val redirectUri = "app://open.tbdforreddit"
        val scope = "submit,identity"
        val duration = "temporary"

        val authUrl = "https://m.reddit.com/api/v1/authorize.compact?" +
                "client_id=$clientId" +
                "&response_type=$responseType" +
                "&state=$state" +
                "&duration=$duration" +
                "&redirect_uri=$redirectUri" +
                "&scope=$scope"

        Log.d(TAG, ": $authUrl")

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(authUrl)

        startActivity(intent)
    }

}
