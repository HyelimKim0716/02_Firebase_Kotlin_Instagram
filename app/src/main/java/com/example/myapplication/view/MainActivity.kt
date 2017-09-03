package com.example.myapplication.view

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.util.replaceToFragment
import com.example.myapplication.view.tabbar.alarm.AlarmFragment
import com.example.myapplication.view.tabbar.detail.DetailFragment
import com.example.myapplication.view.tabbar.home.HomeFragment
import com.example.myapplication.view.tabbar.photo.AddPhotoActivity
import com.example.myapplication.view.tabbar.user.UserFragment
import com.google.firebase.auth.FirebaseAuth
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
            R.id.action_search -> replaceToFragment(R.id.frame_layout, DetailFragment.getInstance())
            R.id.action_add_photo -> startActivity(Intent(this@MainActivity, AddPhotoActivity::class.java))
            R.id.action_account -> {
                val fragment: Fragment = UserFragment()
                val bundle: Bundle = Bundle()
                bundle.putString("destinationUid", FirebaseAuth.getInstance().currentUser?.uid)
                fragment.arguments = bundle
                replaceToFragment(R.id.frame_layout, fragment)
            }
            R.id.action_favorite_alarm -> replaceToFragment(R.id.frame_layout, AlarmFragment.create())
        }

        false
    }

}
