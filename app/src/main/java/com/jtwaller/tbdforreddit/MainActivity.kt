package com.jtwaller.tbdforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mViewModel = ViewModelProviders.of(this).get(RedditResponseViewModel::class.java)
        mViewModel.mRedditLinkLiveData.observe(this, Observer { list ->
            viewAdapter.notifyDataSetChanged()
        })

        viewManager = LinearLayoutManager(this)
        // TODO: Need to set the viewmodel as the data source for the adapter?
        viewAdapter = MyAdapter(mViewModel.mRedditLinkList)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

}
