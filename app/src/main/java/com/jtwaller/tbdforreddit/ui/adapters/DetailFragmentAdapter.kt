package com.jtwaller.tbdforreddit.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jtwaller.tbdforreddit.GlideApp
import com.jtwaller.tbdforreddit.R
import com.jtwaller.tbdforreddit.abbreviateThousands
import com.jtwaller.tbdforreddit.models.RedditComment
import com.jtwaller.tbdforreddit.models.RedditObjectData
import com.jtwaller.tbdforreddit.printLongestUnit
import kotlinx.android.synthetic.main.detail_comment.view.*
import kotlinx.android.synthetic.main.detail_link.view.*
import java.lang.RuntimeException

class DetailFragmentAdapter(val context: Context, val parentLink: RedditObjectData, val comments: ArrayList<RedditComment>)
    : RecyclerView.Adapter<DetailFragmentAdapter.DetailViewHolder>() {

    companion object {
        const val TAG = "DetailFragmentAdapter"
        const val PARENT_ITEM = 0
        const val COMMENT_ITEM = 1
    }

    class DetailViewHolder(view: LinearLayout) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return PARENT_ITEM
            else -> return COMMENT_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val mResourceId = when (viewType) {
            PARENT_ITEM -> R.layout.detail_link
            COMMENT_ITEM -> R.layout.detail_comment
            else -> throw RuntimeException("Invalid viewtype")
        }

        return DetailViewHolder(LayoutInflater
                .from(parent.context)
                .inflate(mResourceId, parent, false) as LinearLayout)
    }

    override fun getItemCount(): Int {
        return comments.size + 1 // Add 1 for parent link at position 0
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        when (holder.itemViewType) {
            PARENT_ITEM -> {
                val mView = holder.itemView

                mView.apply {
                    title_text.text = parentLink.title
                    subreddit_text.text = parentLink.subreddit
                    domain_text.text = parentLink.getDomain()
                    author_text.text = parentLink.author
                    upvote_count.text = parentLink.score.abbreviateThousands(this.context)
                    comment_count.text = parentLink.num_comments.abbreviateThousands(this.context)
                    age_text.text = parentLink.getAgePeriod().printLongestUnit(this.context)

                    self_text.apply {
                        if (parentLink.selftext == "") {
                            visibility = View.GONE
                        } else {
                            text = parentLink.selftext
                        }
                    }
                }

                val width = parentLink.preview?.images?.get(0)?.source?.width ?: -1
                val height = parentLink.preview?.images?.get(0)?.source?.height ?: -1

                if (width > 0 && height > 0) {
                    GlideApp.with(context)
                            .load(parentLink.url)
                            .placeholder(createPlaceholder(width, height))
                            .into(mView.link_image)
                }
            }
            COMMENT_ITEM -> {
                val mView = holder.itemView
                val comment = comments[position - 1] // Subtract 1 for parent link data

                mView.apply {
                    comment_author.text = comment.author
                    comment_upvotes.text = this.context.resources.getString(R.string.points_abbrev, comment.score.abbreviateThousands(this.context))
                    comment_age.text = comment.getAgePeriod().printLongestUnit(this.context)
                    comment_body.text = comment.body
                    setPaddingRelative(8 + comment.depth * 24, 8, 8, 8)
                }
            }
            else -> throw RuntimeException("Invalid viewtype")
        }
    }

    private fun createPlaceholder(srcWidth: Int, srcHeight: Int): BitmapDrawable {
        val metrics = DisplayMetrics()

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)

        val deviceWidth = metrics.widthPixels

        val resizeRatio: Float = deviceWidth.toFloat() / srcWidth
        val placeholderWidth = resizeRatio * srcWidth
        val placeholderHeight = resizeRatio * srcHeight

        val bitmap = Bitmap.createBitmap(
                placeholderWidth.toInt(),
                placeholderHeight.toInt(),
                Bitmap.Config.ARGB_8888)

        bitmap.eraseColor(android.graphics.Color.GRAY)

        return BitmapDrawable(context?.resources, bitmap)
    }

}