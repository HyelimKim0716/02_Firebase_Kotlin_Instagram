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

        fun setFollowerCount(followerCount: String)

        fun setFollowerButton(hasUid: Boolean)

        fun setFollowingCount(followerCount: String)

        fun getMessage() : String

        fun setMessage(message: String)
    }

    interface Presenter {
        val view: View

        fun sendComment(destinationUid: String, imageUid: String?)

        fun getFollower(destinationUid: String, uid: String?)

        fun getFollowing(destinationUid: String)

        fun updateComment(imageUid: String?)

    }
}