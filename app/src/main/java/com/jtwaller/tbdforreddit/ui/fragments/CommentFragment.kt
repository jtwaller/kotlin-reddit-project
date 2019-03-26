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
import com.jtwaller.tbdforreddit.printLongestUnit
import com.jtwaller.tbdforreddit.viewmodels.RedditCommentFragmentViewModel
import kotlinx.android.synthetic.main.fragment_comments.view.*

class CommentFragment : Fragment() {

    companion object {
        const val TAG = "CommentFragment"
    }

    private lateinit var mViewModel: RedditCommentFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProviders.of(this).get(RedditCommentFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_comments, container, false)

        mViewModel.isLoading.observe(this, Observer {
            if (it == false) {
                mView.apply {
                    title_text.text = mViewModel.linkData.title
                    subreddit_text.text = mViewModel.linkData.subreddit
                    domain_text.text = mViewModel.linkData.getDomain()
                    author_text.text = mViewModel.linkData.author
                    upvote_count.text = mViewModel.linkData.getShortFormatScore()
                    comment_count.text = mViewModel.linkData.getShortFormatCommentCount()
                    age_text.text = mViewModel.linkData.getAgePeriod().printLongestUnit(this.context)
                }

                GlideApp.with(this)
                        .load(mViewModel.linkData.url)
                        .into(mView.link_image)
            }
        })

        return mView
    }

}