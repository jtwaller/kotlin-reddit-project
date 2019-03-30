package com.jtwaller.tbdforreddit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jtwaller.tbdforreddit.GlideApp
import com.jtwaller.tbdforreddit.R
import com.jtwaller.tbdforreddit.models.RedditLinkData
import com.jtwaller.tbdforreddit.printLongestUnit
import com.jtwaller.tbdforreddit.ui.adapters.PostListAdapter.Companion.REDDIT_LINK_DATA
import com.jtwaller.tbdforreddit.viewmodels.RedditCommentFragmentViewModel
import kotlinx.android.synthetic.main.fragment_comments.view.*

class CommentFragment: Fragment() {

    companion object {
        const val TAG = "CommentFragment"

        fun newInstance(redditLinkData: RedditLinkData): CommentFragment {
            val args = Bundle()
            args.putParcelable(REDDIT_LINK_DATA, redditLinkData)

            val frag = CommentFragment()
            frag.arguments = args

            return frag
        }
    }

    private lateinit var mViewModel: RedditCommentFragmentViewModel
    private lateinit var mParentLinkData: RedditLinkData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mParentLinkData = arguments!!.get(REDDIT_LINK_DATA) as RedditLinkData

        mViewModel = ViewModelProviders.of(this).get(RedditCommentFragmentViewModel::class.java)
        mViewModel.load(mParentLinkData.permalink)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_comments, container, false)

        mView.apply {
            title_text.text = mParentLinkData.title
            subreddit_text.text = mParentLinkData.subreddit
            domain_text.text = mParentLinkData.getDomain()
            author_text.text = mParentLinkData.author
            upvote_count.text = mParentLinkData.getShortFormatScore()
            comment_count.text = mParentLinkData.getShortFormatCommentCount()
            age_text.text = mParentLinkData.getAgePeriod().printLongestUnit(this.context)
        }

        mViewModel.isLoading.observe(this, Observer {
            if (it == false) {
                GlideApp.with(this)
                        .load(mViewModel.linkData.url)
                        .into(mView.link_image)
            }
        })

        return mView
    }

}