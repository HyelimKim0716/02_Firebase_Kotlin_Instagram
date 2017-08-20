package com.example.myapplication.view.tabbar.comment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentRvAdapter
import com.example.myapplication.model.Comment
import com.example.myapplication.view.tabbar.comment.presenter.CommentContract
import com.example.myapplication.view.tabbar.comment.presenter.CommentPresenter
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity(), CommentContract.View {

    lateinit var mPresenter: CommentPresenter
    lateinit var mAdapter: CommentRvAdapter

    var mImageUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        mImageUid = intent.getStringExtra("imageUid")
        btn_send.setOnClickListener {
            mPresenter.sendComment(mImageUid, et_message.text.toString())
        }

        mAdapter = CommentRvAdapter(applicationContext)
        rv_comment.layoutManager = LinearLayoutManager(this)
        rv_comment.adapter = mAdapter

        mPresenter = CommentPresenter(this)
        mPresenter.updateComment(mImageUid)
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


}
