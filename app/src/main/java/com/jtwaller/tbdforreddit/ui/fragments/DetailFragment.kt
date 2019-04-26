package com.jtwaller.tbdforreddit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jtwaller.tbdforreddit.R
import com.jtwaller.tbdforreddit.models.RedditObjectData
import com.jtwaller.tbdforreddit.ui.adapters.DetailFragmentAdapter
import com.jtwaller.tbdforreddit.ui.adapters.PostListAdapter.Companion.REDDIT_LINK_DATA
import com.jtwaller.tbdforreddit.viewmodels.CommentsViewModel
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

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: DetailFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parentObject = arguments?.get(REDDIT_LINK_DATA) as? RedditObjectData
                ?: throw RuntimeException("Detail fragment cannot be created without parent object")

        val mCommentsViewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
        mAdapter = DetailFragmentAdapter(this.context!!, parentObject, mCommentsViewModel.commentList)

        mCommentsViewModel.isLoading.observe(this, Observer {
            if (it == false) {
                mAdapter.notifyDataSetChanged()
            }
        })

        mCommentsViewModel.load(parentObject.permalink)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val mLayoutManager = LinearLayoutManager(this.context)

        mRecyclerView = view.findViewById(R.id.detail_recycler_view)
        mRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mAdapter
            addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
        }

        return view
    }

}