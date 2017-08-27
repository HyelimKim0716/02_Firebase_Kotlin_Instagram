package com.example.myapplication.view.tabbar.detail.presenter

import com.example.myapplication.model.Content

/**
 * Created by Owner on 2017-08-10.
 */
interface DetailContract {

    interface View {
        fun clearContentList()

        fun clearContentUidList()

        fun addContent(content: Content)

        fun addContentUid(key: String)

        fun notifyDataSetChanged()

    }

    interface Presenter {
        val view: View

        fun loadImages()

    }
}