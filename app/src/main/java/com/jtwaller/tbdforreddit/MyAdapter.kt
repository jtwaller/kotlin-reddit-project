package com.jtwaller.tbdforreddit

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jtwaller.tbdforreddit.network.RedditT3
import kotlinx.android.synthetic.main.thumbnail_view.view.*

class MyAdapter(private val context: Context, private val dataSet: ArrayList<RedditT3>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val layout: LinearLayout, val viewType: Int) : RecyclerView.ViewHolder(layout)

//    enum class ItemViewType(val type: Int) {
//        THUMBNAIL(0),
//        NO_THUMBNAIL(1)
//    }

//    override fun getItemViewType(position: Int): Int {
//
//        return when (URLUtil.isValidUrl(mDataSet[position].data.thumbnail)) {
//            true -> ItemViewType.THUMBNAIL.type
//            false -> ItemViewType.NO_THUMBNAIL.type
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val resourceId = when (viewType) {
//            ItemViewType.THUMBNAIL.type -> R.layout.thumbnail_view
//            ItemViewType.NO_THUMBNAIL.type -> R.layout.no_thumbnail_view
//            else -> -1
//        }

        val mView =  LayoutInflater.from(parent.context)
                .inflate(R.layout.thumbnail_view, parent, false) as LinearLayout

        return MyViewHolder(mView, viewType)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mView = holder.layout
        val mData = dataSet[position].data
        mView.apply {
            title_text.text = mData.title

            subreddit_text.text = mData.subreddit
            domain_text.text = mData.getDomain()
            author_text.text = mData.author

            upvote_count.text = mData.getUpvoteCount()
            comment_count.text = mData.getCommentCount()
            age_text.text = mData.getAgePeriod().printLongestUnit(this.context)

        }

        if (mData.over_18) {
            GlideApp.with(context)
                    .load(context.resources.getDrawable(R.drawable.nsfw_thumbnail))
                    .centerCrop()
                    .into(holder.layout.thumbnail)
            return
        } else {
            val thumbnailUrl: String? = dataSet[position].data.thumbnail

            thumbnailUrl?.let {
                GlideApp.with(context)
                        .load(thumbnailUrl)
                        .centerCrop()
                        .into(holder.layout.thumbnail)
            }
        }

//        if (holder.viewType == ItemViewType.THUMBNAIL.type) {
//            GlideApp.with(context)
//                    .load(mDataSet[position].data.thumbnail)
//                    .into(holder.layout.thumbnail)
//        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}