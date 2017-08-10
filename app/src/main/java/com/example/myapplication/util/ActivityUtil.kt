package com.example.myapplication.util

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by Owner on 2017-08-10.
 */
fun AppCompatActivity.replaceToFragment(resId: Int, fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(resId, fragment)
    transaction.commit()
}