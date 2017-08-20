package com.example.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.model.Comment
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

/**
 * Created by HyelimKim on 2017-08-20.
 */
class CommentRvAdapter(val context: Context) : RecyclerView.Adapter<CommentRvAdapter.CommentRvAdapterViewHolder>() {
    val mCommentList: ArrayList<Comment> = ArrayList()

    fun addComment(comment: Comment) {
        mCommentList.add(comment)
    }

    fun clearCommentList() {
        mCommentList.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentRvAdapterViewHolder = CommentRvAdapterViewHolder(context, parent)

    override fun onBindViewHolder(holder: CommentRvAdapterViewHolder?, position: Int) {
        holder?.onBindView(position)
    }

    override fun getItemCount(): Int = mCommentList.size

    inner class CommentRvAdapterViewHolder(val context: Context, container: ViewGroup?)
        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, container, false)) {

        fun onBindView(position: Int) {
            with(itemView) {
                tv_profile.text = mCommentList[position].userId
                tv_comment.text = mCommentList[position].comment
            }
        }
    }
}