package com.example.myapplication.view.tabbar.home.presenter

import com.example.myapplication.model.Content
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Owner on 2017-08-10.
 */

class HomePresenter(override val view: HomeContract.View) : HomeContract.Presenter {


    override fun loadImages() {
        // Get images from Firebase and show them in list
        FirebaseDatabase.getInstance().reference.child("images")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError?) {
                        view.showToast("Loading Images is canceled. Error : ${dataSnapshot.toString()}")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        view.clearItems()

                        dataSnapshot?.children?.forEach {
                            view.addItems(it.getValue(Content::class.java))
                        }

                        view.notifyDataSetChanged()
                    }

                })
    }
}