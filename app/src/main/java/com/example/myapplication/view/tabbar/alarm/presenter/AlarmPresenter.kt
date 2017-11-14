package com.example.myapplication.view.tabbar.alarm.presenter

import com.example.myapplication.model.Alarm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by HyelimKim on 2017-08-27.
 */
class AlarmPresenter(override val view: AlarmContract.View) : AlarmContract.Presenter {

    override fun loadAlarm() {
        FirebaseDatabase.getInstance()
                .reference
                .child("alarms")
                .orderByChild("destinationUid")
                .equalTo(FirebaseAuth.getInstance().currentUser?.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        dataSnapshot?.children?.forEach {
                            view.addAlarm(it.getValue(Alarm::class.java)!!)
                        }


                    }

                })
    }

}