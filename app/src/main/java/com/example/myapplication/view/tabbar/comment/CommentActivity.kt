package com.example.myapplication.view.tabbar.comment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.example.myapplication.R
import com.example.myapplication.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {

    var imageUid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        imageUid = intent.getStringExtra("imageUid")
        btn_send.setOnClickListener {
            val userId: String? = FirebaseAuth.getInstance().currentUser?.email
            val comment: String? = et_message.text.toString()
            val uid: String? = FirebaseAuth.getInstance().currentUser?.uid

            var one: Comment = Comment(uid, userId, comment)

            FirebaseDatabase.getInstance().getReference("images")
                    .child(imageUid)
                    .child("comments")
                    .push()
                    .setValue(one)

        }

    }
}
