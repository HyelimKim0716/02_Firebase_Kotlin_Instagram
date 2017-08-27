package com.example.myapplication.view.tabbar.user.presenter

/**
 * Created by HyelimKim on 2017-08-20.
 */
interface UserContract {

    interface View {

    }

    interface Presenter {
        val view: View

        fun requestFollow(destinationUid: String, uid: String)

        fun getFollower(destinationUid: String)

//        fun getFollowing()
    }
}