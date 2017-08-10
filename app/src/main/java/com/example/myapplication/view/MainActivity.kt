package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.util.replaceToFragment
import com.example.myapplication.view.tabbar.AddPhotoActivity
import com.example.myapplication.view.tabbar.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener(mNavigationItemSelectedListener)
    }

    val mNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_home -> replaceToFragment(R.id.frame_layout, HomeFragment.getInstance())
            R.id.action_add_photo -> startActivity(Intent(this@MainActivity, AddPhotoActivity::class.java))
        }

        false
    }

}
