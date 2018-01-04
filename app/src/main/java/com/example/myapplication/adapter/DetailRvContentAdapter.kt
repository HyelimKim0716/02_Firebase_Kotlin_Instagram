package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.model.Alarm
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



                iv_content.setOnClickListener {
                    val intent: Intent = Intent(context, CommentActivity::class.java)
                    intent.putExtra("imageUid", mContentUidList[position])
                    intent.putExtra("destinationUid", mContentList[position].uid)
                    context.startActivity(intent)
                }

                FirebaseDatabase.getInstance().reference
                        .child("profileImages")
                        .child(mContentList[position].uid)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
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

        fun favorite(position: Int) {
            FirebaseDatabase.getInstance().getReference("images")
                    .child(mContentUidList[position])
                    .runTransaction(object : Transaction.Handler {
                        override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                            Log.d("DetailRvContentAdapter", "onCompleted!")
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
                                content.favoriteMap.plus(mapOf(uid to true))
                                // likeButtonAlarm(imageDTOs.get(i).uid);
                            }

                            // Set value and report transaction success
                            mutableData.value = content
                            return Transaction.success(mutableData)

                        }

                    })
        }

        fun alarmFavorite(destinationUid: String) {
            val alarm: Alarm = Alarm(destinationUid,
                    FirebaseAuth.getInstance().currentUser?.email,
                    FirebaseAuth.getInstance().currentUser?.uid,
                    0,
                    "")
            FirebaseDatabase.getInstance().reference.child("alarms").push().setValue(alarm)

        }

    }
}
