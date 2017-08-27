package com.example.myapplication.view.tabbar.detail.presenter

import com.example.myapplication.model.Content
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Owner on 2017-08-10.
 */
class DetailPresenter(override val view: DetailContract.View) : DetailContract.Presenter {

    override fun loadImages() {
        FirebaseDatabase.getInstance().reference.child("images")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        view.clearContentList()
                        view.clearContentUidList()

                        dataSnapshot?.children?.forEach {
                            view.addContent(it.getValue(Content::class.java))
                            view.addContentUid(it.key)
                        }

                        view.notifyDataSetChanged()
                    }

                })
    }



}