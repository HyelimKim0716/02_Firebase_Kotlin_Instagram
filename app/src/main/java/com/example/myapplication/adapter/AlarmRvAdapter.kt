package com.example.myapplication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.model.Alarm
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_comment.view.*

/**
 * Created by HyelimKim on 2017-08-27.
 */
class AlarmRvAdapter(val context: Context) : RecyclerView.Adapter<AlarmRvAdapter.AlarmRvItemViewHolder>() {
    val mAlarmList: ArrayList<Alarm> = ArrayList()

    fun clearList() {
        mAlarmList.clear()
    }

    fun addAlarm(alarm: Alarm) {
        mAlarmList.add(alarm)
    }

    override fun getItemCount(): Int = mAlarmList.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlarmRvItemViewHolder
        = AlarmRvItemViewHolder(parent)

    override fun onBindViewHolder(holder: AlarmRvItemViewHolder?, position: Int) {
        holder?.onBindView(position)
    }


    inner class AlarmRvItemViewHolder(parent: ViewGroup?) :
            RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)) {

        fun onBindView(position: Int) {
            with(itemView) {
                when (mAlarmList[position].kind) {
                    0 -> tv_profile.text = "${mAlarmList[position].userId} 님이 좋아요를 눌렀습니다."
                    1 -> tv_profile.text = "${mAlarmList[position].userId} 님이 ${mAlarmList[position].message} 메시지를 남겼습니다."
                    2 -> tv_profile.text = "${mAlarmList[position].userId} 님이 당신의 계정에 팔로워하기 시작했습니다."
                }

                FirebaseDatabase.getInstance().reference
                        .child("profileImages")
                        .child(mAlarmList[position].uid)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError?) {
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                val url = dataSnapshot?.value.toString()
                                Glide.with(context)
                                        .load(url)
                                        .apply(RequestOptions().circleCrop())
                                        .into(iv_profile)
                            }

                        })
            }
        }
    }
}