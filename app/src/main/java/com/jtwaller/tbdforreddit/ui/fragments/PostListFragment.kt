package com.jtwaller.tbdforreddit.ui.fragments

import android.content.Context
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
import com.jtwaller.tbdforreddit.ui.adapters.PostListAdapter
import com.jtwaller.tbdforreddit.R
import com.jtwaller.tbdforreddit.viewmodels.RedditLinkListViewModel
import java.lang.RuntimeException

class PostListFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewAdapter: RecyclerView.Adapter<*>
    private lateinit var mViewModel: RedditLinkListViewModel

    private lateinit var fragmentContext: Context
    private lateinit var fragmentView: View

    private var mCurrLinkListSize = 0

    companion object {
        const val TAG = "PostListFragment"
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        fragmentContext = context ?: throw RuntimeException("No context to attach fragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProviders.of(this).get(RedditLinkListViewModel::class.java)
        mViewAdapter = PostListAdapter(fragmentContext, mViewModel.redditLinkList)

        mViewModel.redditLinkListSize.observe(this, Observer { listSize ->
            mViewAdapter.notifyItemRangeInserted(mCurrLinkListSize, listSize)
            mCurrLinkListSize = listSize
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mViewManager = LinearLayoutManager(fragmentContext)

        fragmentView = inflater.inflate(R.layout.fragment_post_list, container, false) ?:
                throw RuntimeException("Unable to inflate view")

        mRecyclerView = fragmentView.findViewById(R.id.recycler_view)

        mRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mViewManager
            adapter = mViewAdapter
            addItemDecoration(DividerItemDecoration(fragmentContext, mViewManager.orientation))
        }

        mRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(mCurrLinkListSize - mViewManager.findLastVisibleItemPosition() < 5) {
                    mViewModel.getLinks()
                }
            }
        })

        return fragmentView
    }

}