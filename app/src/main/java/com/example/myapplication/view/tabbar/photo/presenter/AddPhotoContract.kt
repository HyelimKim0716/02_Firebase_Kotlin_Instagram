package com.example.myapplication.view.tabbar.photo.presenter

import android.app.Activity
import android.content.Intent

/**
 * Created by Owner on 2017-08-10.
 */
interface AddPhotoContract {

    interface View {
        fun getExplain() : String
    }

    interface Presenter {
        val view: View

        fun requestPermission(activity: Activity)

        fun openAlbum()

        fun uploadToFirebaseServer()

        fun changeImageUrlToPath(data: Intent?)
    }
}