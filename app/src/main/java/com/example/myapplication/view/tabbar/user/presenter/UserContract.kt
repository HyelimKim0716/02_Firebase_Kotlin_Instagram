package com.example.myapplication.view.tabbar.user.presenter

import android.app.Activity

/**
 * Created by HyelimKim on 2017-08-20.
 */
interface UserContract {

    interface View {
        fun changeImageProfile(url: String)
    }

    interface Presenter {
        val view: View

        fun requestFollow(destinationUid: String, uid: String)

        fun getFollower(destinationUid: String)

//        fun getFollowing()

        fun openGallery(activity: Activity)


        fun getProfileImage(destinationUid: String?)
    }
}