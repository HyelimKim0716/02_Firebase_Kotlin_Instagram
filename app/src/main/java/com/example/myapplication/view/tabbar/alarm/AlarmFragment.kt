package com.example.myapplication.view.tabbar.alarm

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.adapter.AlarmRvAdapter
import com.example.myapplication.model.Alarm
import com.example.myapplication.view.tabbar.alarm.presenter.AlarmContract
import com.example.myapplication.view.tabbar.alarm.presenter.AlarmPresenter
import kotlinx.android.synthetic.main.fragment_alarm.*

/**
 * Created by HyelimKim on 2017-08-27.
 */

class AlarmFragment : Fragment(), AlarmContract.View {

    companion object {
        fun create() = AlarmFragment()
    }
    val mPresenter = AlarmPresenter(this)
    lateinit var mAlarmAdapter: AlarmRvAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater?.inflate(R.layout.fragment_alarm, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        rv_alarm.layoutManager = LinearLayoutManager(context)
        mAlarmAdapter = AlarmRvAdapter(context)
        rv_alarm.adapter = mAlarmAdapter

        mPresenter.loadAlarm()

    }

    override fun clearAlarm() {
        mAlarmAdapter.clearList()
    }

    override fun addAlarm(alarm: Alarm) {
        mAlarmAdapter.addAlarm(alarm)
    }

    override fun notifyDataSetChanged() {
        mAlarmAdapter.notifyDataSetChanged()
    }
}