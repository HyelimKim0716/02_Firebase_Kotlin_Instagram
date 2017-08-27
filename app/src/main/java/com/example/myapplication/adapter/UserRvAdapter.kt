package com.example.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.model.Content
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by HyelimKim on 2017-08-27.
 */
class UserRvAdapter(val context: Context) : RecyclerView.Adapter<UserRvAdapter.UserRvItemViewHolder>() {
    val contents: ArrayList<Content> = ArrayList()
    init {
        FirebaseDatabase.getInstance().reference.child("images")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        contents.clear()

                        dataSnapshot?.children?.forEach {
                            contents.add(it.getValue(Content::class.java))
                        }

                        notifyDataSetChanged()
                    }

                })

    }
    override fun onBindViewHolder(holder: UserRvItemViewHolder?, position: Int) {
        Glide.with(holder?.itemView?.context)
                .load(contents[position].imageUrl)
                .apply(RequestOptions().centerCrop())
                .into(holder?.itemView as ImageView)
    }

    override fun getItemCount(): Int = contents.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserRvItemViewHolder {
        return UserRvItemViewHolder(context)
    }

    inner class UserRvItemViewHolder(context: Context)
        : RecyclerView.ViewHolder(ImageView(context)) {

    }
}