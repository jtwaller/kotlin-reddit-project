package com.jtwaller.tbdforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.danlew.android.joda.JodaTimeAndroid

class MainActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewAdapter: RecyclerView.Adapter<*>
    private lateinit var mViewManager: LinearLayoutManager

    private var mCurrLinkListSize = 0

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JodaTimeAndroid.init(this)

        val mViewModel = ViewModelProviders.of(this).get(RedditResponseViewModel::class.java)
        mViewModel.mRedditLinkListSize.observe(this, Observer { listSize ->
            mViewAdapter.notifyItemRangeInserted(mCurrLinkListSize, listSize)
            mCurrLinkListSize = listSize
        })

        mViewManager = LinearLayoutManager(this)
        mViewAdapter = MyAdapter(this, mViewModel.mRedditLinkList)

        val mItemDividerItemDecoration = DividerItemDecoration(this, mViewManager.orientation)

        mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = mViewManager
            adapter = mViewAdapter
            addItemDecoration(mItemDividerItemDecoration)
        }

        mRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(mCurrLinkListSize - mViewManager.findLastVisibleItemPosition() < 5) {
                    mViewModel.getLinks()
                }
            }
        })
    }

}
