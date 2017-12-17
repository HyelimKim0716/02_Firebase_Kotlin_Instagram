package com.example.myapplication.util

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by HyelimKim on 2017-12-16.
 */
class CustomFirebaseInstanceIDService : FirebaseInstanceIdService() {

    val TAG = "InstanceIDService"
    /*
    * InstanceID 토큰 업데이트 시 호출
    * 이전 토큰 보안이 손상될 경우 발생
    * */
    override fun onTokenRefresh() {
        // Get updated Instance ID token
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token : $refreshedToken")

    }

    /**
    * Persist token to third-party servers.
    *
    * Modify this method to associate the user's FCM InstanceID token with any server-side account
    * maintained by your application
    *
    * @param token The new token
    * */
    fun sendRegisterationToServer(token: String) {

    }
}