package com.example.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.model.Content

/**
 * Created by Owner on 2017-08-10.
 */
class HomeRvImageAdapter(val context: Context) : RecyclerView.Adapter<HomeRvImageAdapter.RvImageAdapterViewHolder>() {
    var mContentItemList: ArrayList<Content> = ArrayList()

    fun clearItemList() {
        mContentItemList.clear()
    }

    fun addItem(content: Content) {
        mContentItemList.add(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RvImageAdapterViewHolder {
        val imageView: ImageView = ImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        return RvImageAdapterViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: RvImageAdapterViewHolder?, position: Int) {
        holder?.onBindView(position)
    }

    override fun getItemCount(): Int = mContentItemList.size

    inner class RvImageAdapterViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView) {

        fun onBindView(position: Int) {
            Glide.with(context)
                    .load(mContentItemList[position].imageUrl)
                    .apply(RequestOptions().centerCrop())
                    .into(imageView)
        }
    }
}