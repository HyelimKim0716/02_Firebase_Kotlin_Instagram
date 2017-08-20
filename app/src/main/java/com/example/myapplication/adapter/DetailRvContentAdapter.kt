package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.Content
import com.example.myapplication.view.tabbar.comment.CommentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.item_detail.view.*


/**
 * Created by Owner on 2017-08-17.
 */
class DetailRvContentAdapter(val context: Context) : RecyclerView.Adapter<DetailRvContentAdapter.DetailContentViewHolder>() {

    private var mContentList: ArrayList<Content> = ArrayList()
    private var mContentUidList: ArrayList<String> = ArrayList()

    fun clearContentList() {
        mContentList.clear()
    }

    fun clearContentUidList() {
        mContentUidList.clear()
    }

    fun addContent(content: Content) {
        mContentList.add(content)
    }

    fun addContentUid(key: String) {
        mContentUidList.add(key)
    }

    override fun onBindViewHolder(holder: DetailContentViewHolder?, position: Int) {
        holder?.onBindView(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DetailContentViewHolder
            = DetailContentViewHolder(context, parent)

    override fun getItemCount(): Int = mContentList.size

    inner class DetailContentViewHolder(context: Context, container: ViewGroup?)
        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_detail, container, false)) {

        fun onBindView(position: Int) {
            with(itemView) {
                tv_profile.text = mContentList[position].userId
                Glide.with(itemView.context)
                        .load(mContentList[position].imageUrl)
                        .into(iv_content)
                tv_explain.text = mContentList[position].explain

                iv_favorite.setOnClickListener({
                    favorite(position)
                })

                if (mContentList[position].favoriteMap.containsKey(FirebaseAuth.getInstance().currentUser?.uid)) {
                    iv_favorite.setImageResource(R.drawable.ic_favorite_red_500_24dp)
                } else {
                    iv_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                }

                tv_favorite_counter.text = "좋아요 ${mContentList[position].favoriteCount} 개"

                val intent: Intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("mImageUid", mContentUidList[position])
                context.startActivity(intent)
            }
        }

        fun favorite(position: Int) {
            FirebaseDatabase.getInstance().getReference("images")
                    .child(mContentUidList[position])
                    .runTransaction(object : Transaction.Handler {
                        override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun doTransaction(mutableData: MutableData?): Transaction.Result {
                            val content: Content? = mutableData?.getValue(Content::class.java)
                            val uid: String? = FirebaseAuth.getInstance().currentUser?.uid

                            if (content == null) return Transaction.success(mutableData)

                            if (content.favoriteMap.containsKey(uid)) {
                                // Unstar the post and remove self from start
                                content.favoriteCount--
                                content.favoriteMap.minus(uid)
                                //content.favoriteMap.remove(uid)
                            } else {
                                // Start the post and add self to start
                                content.favoriteCount++
                                content.favoriteMap.plus(uid to true)
                                // likeButtonAlarm(imageDTOs.get(i).uid);
                            }

                            // Set value and report transaction success
                            mutableData.value = content
                            return Transaction.success(mutableData)

                        }

                    })
        }

    }
}
