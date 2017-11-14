package com.example.myapplication.view.tabbar.comment.presenter

import com.example.myapplication.model.Alarm
import com.example.myapplication.model.Comment
import com.example.myapplication.model.Follow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Owner on 2017-08-17.
 */

class CommentPresenter(override val view: CommentContract.View) : CommentContract.Presenter {

    override fun sendComment(destinationUid: String, imageUid: String?) {
        val userId: String? = FirebaseAuth.getInstance().currentUser?.email
        val uid: String? = FirebaseAuth.getInstance().currentUser?.uid

        var one: Comment = Comment(uid, userId, view.getMessage())

        FirebaseDatabase.getInstance().getReference("images")
                .child(imageUid)
                .child("comments")
                .push()
                .setValue(one)

        alarmComment(destinationUid, view.getMessage())
    }

    override fun updateComment(imageUid: String?) {
        FirebaseDatabase.getInstance()
                .reference
                .child("images")
                .child(imageUid)
                .child("comments")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        view.clearComments()

                        dataSnapshot?.children?.forEach {
                            view.addComment(it.getValue(Comment::class.java)!!)
                        }

                        view.notifyDataSetChanged()
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    override fun getFollower(destinationUid: String, uid: String?) {
        FirebaseDatabase.getInstance()
                .reference
                .child("uers")
                .child(destinationUid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val follow: Follow? = dataSnapshot?.getValue(Follow::class.java)
                        view.setFollowerCount(follow?.followerCount.toString())
                        if (follow?.followerMap?.containsKey(uid)!!)
                            view.setFollowerButton(true)
                        else
                            view.setFollowerButton(false)
                    }

                })
    }

    override fun getFollowing(destinationUid: String) {
        FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(destinationUid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val follow : Follow? = dataSnapshot?.getValue(Follow::class.java)
                        view.setFollowingCount(follow?.followingCount.toString())
                    }

                })
    }

    fun alarmComment(destinationUid: String, message: String) {
        val alarm = Alarm(
                destinationUid,
                FirebaseAuth.getInstance().currentUser?.email,
                FirebaseAuth.getInstance().currentUser?.uid,
                1,
                message)
        FirebaseDatabase.getInstance()
                .reference
                .child("alarms")
                .push()
                .setValue(alarm)
    }
}