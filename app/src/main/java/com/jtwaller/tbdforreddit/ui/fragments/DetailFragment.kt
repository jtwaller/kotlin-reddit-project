package com.jtwaller.tbdforreddit.ui.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jtwaller.tbdforreddit.GlideApp
import com.jtwaller.tbdforreddit.R
import com.jtwaller.tbdforreddit.models.RedditObjectData
import com.jtwaller.tbdforreddit.printLongestUnit
import com.jtwaller.tbdforreddit.ui.adapters.PostListAdapter.Companion.REDDIT_LINK_DATA
import com.jtwaller.tbdforreddit.viewmodels.RedditCommentFragmentViewModel
import kotlinx.android.synthetic.main.fragment_detail.view.*
import java.lang.RuntimeException

class DetailFragment: Fragment() {

    companion object {
        const val TAG = "DetailFragment"

        fun newInstance(redditObjectData: RedditObjectData): DetailFragment =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(REDDIT_LINK_DATA, redditObjectData)
                    }
                }
    }

    private lateinit var mViewModel: RedditCommentFragmentViewModel
    private lateinit var mParentLinkData: RedditObjectData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mParentLinkData = it.get(REDDIT_LINK_DATA) as RedditObjectData
        } ?: throw RuntimeException("Detail fragment should have link data argument")

        mViewModel = ViewModelProviders.of(this).get(RedditCommentFragmentViewModel::class.java)
        mViewModel.load(mParentLinkData.permalink)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_detail, container, false)

        mView.apply {
            title_text.text = mParentLinkData.title
            subreddit_text.text = mParentLinkData.subreddit
            domain_text.text = mParentLinkData.getDomain()
            author_text.text = mParentLinkData.author
            upvote_count.text = mParentLinkData.getShortFormatScore()
            comment_count.text = mParentLinkData.getShortFormatCommentCount()
            age_text.text = mParentLinkData.getAgePeriod().printLongestUnit(this.context)

            self_text.apply {
                if (mParentLinkData.selftext == "") {
                    visibility = View.GONE
                } else {
                    text = mParentLinkData.selftext
                }
            }
        }

        Log.d(TAG, ": ${mParentLinkData.url}")
        Log.d(TAG, ": ${mParentLinkData.preview?.images?.get(0)?.source?.width}")

        val width = mParentLinkData.preview?.images?.get(0)?.source?.width ?: -1
        val height = mParentLinkData.preview?.images?.get(0)?.source?.height ?: -1

        if (width > 0 && height > 0) {
            GlideApp.with(this)
                    .load(mParentLinkData.url)
                    .placeholder(createPlaceholder(width, height))
                    .into(mView.link_image)

        }

        mViewModel.isLoading.observe(this, Observer {
            if (it == false) {
                // Load comments
            }
        })

        return mView
    }

    private fun createPlaceholder(srcWidth: Int, srcHeight: Int): BitmapDrawable {
        val metrics = DisplayMetrics()
        this.activity!!.windowManager.defaultDisplay.getMetrics(metrics)

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