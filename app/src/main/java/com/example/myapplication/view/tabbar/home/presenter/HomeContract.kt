package com.example.myapplication.view.tabbar.home.presenter

import com.example.myapplication.model.Content

/**
 * Created by Owner on 2017-08-10.
 */
interface HomeContract {

    interface View {
        fun showToast(message: String)

        fun addItems(item: Content)

        fun clearItems()

        fun notifyDataSetChanged()
    }

    interface Presenter {
        val view: View

        fun loadImages()
    }
}