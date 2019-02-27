package com.jtwaller.tbdforreddit

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jtwaller.tbdforreddit.network.RedditLink

class MyAdapter(private val mDataSet: ArrayList<RedditLink>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView =  LayoutInflater.from(parent.context)
                .inflate(R.layout.text_view, parent, false) as TextView

        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = mDataSet[position].data.title
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

}