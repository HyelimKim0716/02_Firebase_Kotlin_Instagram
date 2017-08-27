package com.example.myapplication.view.tabbar.alarm.presenter

import com.example.myapplication.model.Alarm

/**
 * Created by HyelimKim on 2017-08-27.
 */
interface AlarmContract {

    interface View {
        fun clearAlarm()

        fun addAlarm(alarm: Alarm)

        fun notifyDataSetChanged()
    }

    interface Presenter {
        val view: View

        fun loadAlarm()
    }

}