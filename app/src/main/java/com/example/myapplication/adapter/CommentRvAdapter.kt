package com.example.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.model.Comment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_comment.view.*

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

                FirebaseDatabase.getInstance().reference
                        .child("profileImages")
                        .child(mCommentList[position].uid)
                        .addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError?) {

                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                Glide.with(context)
                                        .load(dataSnapshot?.value.toString())
                                        .apply(RequestOptions().circleCrop())
                                        .into(iv_profile)

                            }

                        })
            }
        }
    }
}