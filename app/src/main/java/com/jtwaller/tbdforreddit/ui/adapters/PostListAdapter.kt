package com.jtwaller.tbdforreddit.ui.adapters

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.jtwaller.tbdforreddit.GlideApp
import com.jtwaller.tbdforreddit.MainActivity
import com.jtwaller.tbdforreddit.R
import com.jtwaller.tbdforreddit.models.RedditLinkObject
import com.jtwaller.tbdforreddit.printLongestUnit
import kotlinx.android.synthetic.main.thumbnail_view.view.*

class PostListAdapter(private val context: Context, private val dataSet: ArrayList<RedditLinkObject>) : RecyclerView.Adapter<PostListAdapter.PostViewHolder>() {

    companion object {
        const val TAG = "PostListAdapter"
        const val REDDIT_LINK_DATA = "REDDIT_LINK_DATA"
    }

    class PostViewHolder(val layout: LinearLayout, val viewType: Int) : RecyclerView.ViewHolder(layout)

    enum class ItemViewType(val type: Int) {
        THUMBNAIL(0),
        NO_THUMBNAIL(1)
    }

    override fun getItemViewType(position: Int): Int {
        return when (URLUtil.isValidUrl(dataSet[position].data.thumbnail)) {
            true -> ItemViewType.THUMBNAIL.type
            false -> ItemViewType.NO_THUMBNAIL.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val mResourceId = when (viewType) {
            ItemViewType.NO_THUMBNAIL.type -> R.layout.no_thumbnail_view
            else -> R.layout.thumbnail_view
        }

        val view = LayoutInflater.from(parent.context)
                .inflate(mResourceId, parent, false) as LinearLayout

        return PostViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val mData = dataSet[position].data

        val mView = holder.layout
        mView.apply {
            title_text.text = mData.title
            subreddit_text.text = mData.subreddit
            domain_text.text = mData.getDomain()
            author_text.text = mData.author
            upvote_count.text = mData.getShortFormatScore()
            comment_count.text = mData.getShortFormatCommentCount()
            age_text.text = mData.getAgePeriod().printLongestUnit(this.context)

            setOnClickListener {
                val mIntent = Intent().apply {
                    component = ComponentName(context, MainActivity::class.java)
                    action = MainActivity.BUILD_FRAGMENT_ACTION
                    putExtra(REDDIT_LINK_DATA, mData)
                }

                LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent)
            }
        }

        if (holder.viewType == ItemViewType.NO_THUMBNAIL.type) return

        if (mData.over_18) {
            GlideApp.with(context)
                    .load(ResourcesCompat.getDrawable(context.resources, R.drawable.nsfw_thumbnail, null))
                    .centerCrop()
                    .into(holder.layout.thumbnail)
        } else {
            val thumbnailUrl: String? = dataSet[position].data.thumbnail

            thumbnailUrl?.let {
                GlideApp.with(context)
                        .load(thumbnailUrl)
                        .centerCrop()
                        .into(holder.layout.thumbnail)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}