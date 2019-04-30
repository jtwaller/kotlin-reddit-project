package com.jtwaller.tbdforreddit.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.jtwaller.tbdforreddit.MainActivity
import com.jtwaller.tbdforreddit.R
import com.jtwaller.tbdforreddit.models.RedditObjectData
import com.jtwaller.tbdforreddit.ui.adapters.PostListAdapter
import java.lang.ClassCastException

class MainBroadcastReceiver(private val parent: MainActivity) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val parentObjectData = intent?.extras?.get(PostListAdapter.REDDIT_LINK_DATA) as? RedditObjectData
                    ?: throw RuntimeException("No parent object available to create Detail Fragment")
            parent.createDetailFragment(parentObjectData)
        } catch (e: ClassCastException) {
            AlertDialog.Builder(parent)
                    .setMessage(R.string.invalid_linkdata_broadcast)
                    .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
        }
    }

}