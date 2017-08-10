package com.example.myapplication.view

/**
 * Created by Owner on 2017-08-10.
 */
interface BaseContract {

    interface View {

    }

    interface Presenter {
        val view: View
    }
}