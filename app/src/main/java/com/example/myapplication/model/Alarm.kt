package com.example.myapplication.model

/**
 * Created by HyelimKim on 2017-08-27.
 */
data class Alarm constructor (var destinationUid: String = "",
                              var userId: String?  = "",
                              var uid: String? = null,
                              var kind: Int = -1,
                              var message: String)