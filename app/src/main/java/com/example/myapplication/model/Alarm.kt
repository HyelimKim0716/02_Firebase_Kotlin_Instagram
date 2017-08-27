package com.example.myapplication.model

/**
 * Created by HyelimKim on 2017-08-27.
 */
data class Alarm (
        var destinationUid: String,
        var userId: String?,
        var uid: String?,
        var kind: Int,
        var message: String
)