package com.example.myapplication.view.tabbar.comment.presenter

import com.example.myapplication.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Owner on 2017-08-17.
 */

class CommentPresenter(override val view: CommentContract.View) : CommentContract.Presenter {

    override fun sendComment(imageUid: String?, comment: String) {
        val userId: String? = FirebaseAuth.getInstance().currentUser?.email
        val uid: String? = FirebaseAuth.getInstance().currentUser?.uid

        var one: Comment = Comment(uid, userId, comment)

        FirebaseDatabase.getInstance().getReference("images")
                .child(imageUid)
                .child("comments")
                .push()
                .setValue(one)
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
                            view.addComment(it.getValue(Comment::class.java))
                        }

                        view.notifyDataSetChanged()
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }
}