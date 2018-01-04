package com.example.myapplication.view

import android.app.Fragment
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.util.replaceToFragment
import com.example.myapplication.view.tabbar.alarm.AlarmFragment
import com.example.myapplication.view.tabbar.detail.DetailFragment
import com.example.myapplication.view.tabbar.home.HomeFragment
import com.example.myapplication.view.tabbar.photo.AddPhotoActivity
import com.example.myapplication.view.tabbar.user.UserFragment
import com.example.myapplication.view.tabbar.user.presenter.UserPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val refreshedToken = FirebaseInstanceId.getInstance().token
        println("Refreshed token : $refreshedToken")

        replaceToFragment(R.id.frame_layout, HomeFragment.create())
        Log.d(TAG, "token : ${FirebaseInstanceId.getInstance().token}" )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notiManager = getSystemService(NotificationManager::class.java)
            notiManager.createNotificationChannel(NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW))
        }

        /*
         * If a notification message is tapped,
        * any data accompanying the notification message is available in the intent extras.
        * In this sample the launcher intent is fired when the notification is tapped, so any accompanying data would be handled here.
        * If you want a different intent fired, se the click_action field of the notificatin message to the desired intent.
        * The launcher intent is used when no click_action is specified.
        *
        * Handle possible data accompanying notification message */
        intent.extras?.keySet()?.forEach {
            val value = intent.extras.get(it)
            Log.d("MainActivity", "Key : $it, Value : $value")
        }

        /* Subscribe a topic */
        FirebaseMessaging.getInstance().subscribeToTopic("news")

        val msg = getString(R.string.msg_subscribed)
        Log.d("MainActivity", "message : $msg")

        /* Get Token */
        val token = FirebaseInstanceId.getInstance().token
        val tokenMsg = getString(R.string.msg_token_fmt, token)


        bottom_navigation.setOnNavigationItemSelectedListener(mNavigationItemSelectedListener)
    }

    private val mNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        Log.d("MainActivity", "item.itemId = ${item.itemId}")
        when (item.itemId) {
            R.id.action_home -> replaceToFragment(R.id.frame_layout, HomeFragment.create())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                UserPresenter.PICK_FROM_ALBUM -> {
                    val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                    val cursorLoader: CursorLoader = CursorLoader(this, data?.data, projection, null, null, null)
                    val cursor: Cursor = cursorLoader.loadInBackground()
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()

                    val imagePath = cursor.getString(columnIndex)
                    val uid = FirebaseAuth.getInstance().currentUser?.uid

                    // UploadFile
                    val file: File = File(imagePath)
                    FirebaseStorage.getInstance().reference
                            .child("userProfileImages")
                            .child(uid!!)
                            .putFile(Uri.fromFile(file))
                            .addOnCompleteListener { task ->
                                val url = task.result.downloadUrl.toString()
                                FirebaseDatabase.getInstance().reference.child("profileImages")
                                        .setValue(mapOf(uid to url))
                            }

                }
            }
        }
    }

}
