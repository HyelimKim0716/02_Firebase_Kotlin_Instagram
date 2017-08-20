package com.example.myapplication.view.tabbar.comment.presenter

import com.example.myapplication.model.Comment

/**
 * Created by Owner on 2017-08-17.
 */
interface CommentContract {

    interface View {
        fun clearComments()

        fun addComment(comment: Comment)

        fun notifyDataSetChanged()

    }

    interface Presenter {
        val view: View

        fun sendComment(imageUid: String?, comment: String)

        fun updateComment(imageUid: String?)
    }
}