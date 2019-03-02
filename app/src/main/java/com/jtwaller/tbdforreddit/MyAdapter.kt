package com.jtwaller.tbdforreddit

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jtwaller.tbdforreddit.network.RedditT3
import kotlinx.android.synthetic.main.thumbnail_view.view.*

class MyAdapter(private val context: Context, private val mDataSet: ArrayList<RedditT3>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val thumbnailView: ConstraintLayout) : RecyclerView.ViewHolder(thumbnailView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val thumbnailView =  LayoutInflater.from(parent.context)
                .inflate(R.layout.thumbnail_view, parent, false) as ConstraintLayout

        return MyViewHolder(thumbnailView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.thumbnailView.title_text.text = mDataSet[position].data.title
        GlideApp.with(context)
                .load(mDataSet[position].data.thumbnail)
                .into(holder.thumbnailView.thumbnail)
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

}