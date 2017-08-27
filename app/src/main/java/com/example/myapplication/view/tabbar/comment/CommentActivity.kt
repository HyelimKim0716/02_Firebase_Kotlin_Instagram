package com.example.myapplication.view.tabbar.comment

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentRvAdapter
import com.example.myapplication.model.Comment
import com.example.myapplication.view.tabbar.comment.presenter.CommentContract
import com.example.myapplication.view.tabbar.comment.presenter.CommentPresenter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.item_comment.*

class CommentActivity : AppCompatActivity(), CommentContract.View {

    lateinit var mPresenter: CommentPresenter
    lateinit var mAdapter: CommentRvAdapter

    var mImageUid: String? = null
    val mDestinationUid: String by lazy {
        intent.getStringExtra("destinationUid")
    }

    val mUid: String? by lazy {
        FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        mImageUid = intent.getStringExtra("imageUid")
        btn_send.setOnClickListener {
            mPresenter.sendComment(mDestinationUid, mImageUid)
        }

        mAdapter = CommentRvAdapter(applicationContext)
        rv_comment.layoutManager = LinearLayoutManager(this)
        rv_comment.adapter = mAdapter

        mPresenter = CommentPresenter(this)
        mPresenter.updateComment(mImageUid)

        btn_send.setOnClickListener {

        }

        mPresenter.getFollower(mDestinationUid, mUid)
        mPresenter.getFollowing(mDestinationUid)
    }

    override fun clearComments() {
        mAdapter.clearCommentList()
    }

    override fun notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged()
    }

    override fun addComment(comment: Comment) {
        mAdapter.addComment(comment)
    }

    override fun setFollowerCount(followerCount: String) {
        tv_follower_counter.text = followerCount
    }

    override fun setFollowerButton(hasUid: Boolean) {
        if (hasUid)
            btn_follow.background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        else
            btn_follow.background.colorFilter = null
    }

    override fun setFollowingCount(followerCount: String) {
        tv_follower_counter.text = followerCount
    }

    override fun getMessage(): String = tv_comment.text.toString()

    override fun setMessage(message: String) {
        tv_comment.text = message
    }

}
