package com.example.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.model.Content

/**
 * Created by Owner on 2017-08-10.
 */
class HomeRvImageAdapter(val context: Context) : RecyclerView.Adapter<HomeRvImageAdapter.RvImageAdapterViewHolder>() {

    private val mWidth by lazy { context.resources.displayMetrics.widthPixels.div(3) }
    var mContentItemList: ArrayList<Content> = ArrayList()

    fun clearItemList() {
        mContentItemList.clear()
    }

    fun addItem(content: Content) {
        mContentItemList.add(content)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RvImageAdapterViewHolder {
        val imageView = ImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(mWidth, mWidth)
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        return RvImageAdapterViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: RvImageAdapterViewHolder?, position: Int) {
        holder?.onBindView(position)
    }

    override fun getItemCount(): Int = mContentItemList.size

    inner class RvImageAdapterViewHolder(private val imageView: ImageView) : RecyclerView.ViewHolder(imageView) {

        fun onBindView(position: Int) {
            Log.d("HomeRvImageAdapter", "image url = ${mContentItemList[position].imageUrl}")
            Glide.with(context)
                    .load(mContentItemList[position].imageUrl)
                    .apply(RequestOptions().centerCrop())
                    .into(imageView)
        }
    }
}