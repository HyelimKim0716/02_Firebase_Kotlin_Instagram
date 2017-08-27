package com.example.myapplication.view.tabbar.user.presenter

import com.example.myapplication.model.Follow
import com.google.firebase.database.*

/**
 * Created by HyelimKim on 2017-08-20.
 */
class UserPresenter(override val view: UserContract.View) : UserContract.Presenter {

    override fun requestFollow(destinationUid: String, uid: String) {
        FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(uid)
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(mutableData: MutableData?): Transaction.Result {
                        var follow: Follow? = mutableData?.getValue(Follow::class.java)

                        if (follow == null) {
                            follow = Follow(0, 1)
                            follow.followingMap.plus(mapOf(destinationUid to true))
                            mutableData?.priority = follow
                            return Transaction.success(mutableData)
                        }

                        if (follow?.followingMap.containsKey(destinationUid)) {
                            // Delete one start for the post and self from start
                            follow.followingCount = follow.followingCount--
                            follow.followingMap.minus(destinationUid)
                        } else{
                            // Star the post and add self to starts
                            follow.followingCount--
                            follow.followingMap.plus(mapOf(destinationUid to true))
                        }

                        // Set value and report transaction success
                        mutableData?.value = follow
                        return Transaction.success(mutableData)
                    }

                    override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {

                    }
                })

        FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(destinationUid)?.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData?): Transaction.Result {
                var follow: Follow? = mutableData?.getValue(Follow::class.java)

                if (follow == null) {
                    follow = Follow(1, 0)
                    follow?.followerMap?.plus(mapOf(uid to true))
                    mutableData?.value = follow
                    return Transaction.success(mutableData)
                }

                if (follow?.followerMap?.containsKey(uid)!!) {
                    // Unstar the post and remove self from start
                    follow!!.followerCount--
                    follow?.followerMap?.minus(uid)
                } else {
                    // Start the post and add self to stars
                    follow!!.followerCount++
                    follow?.followerMap?.plus(mapOf(uid to  true))
                }

                // Set value and report transaction success
                mutableData?.value = follow
                return Transaction.success(mutableData)
            }

            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {

            }
        })
    }
}