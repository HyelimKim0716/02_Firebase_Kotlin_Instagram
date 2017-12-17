package com.example.myapplication.model

/**
 * Created by HyelimKim on 2017-08-20.
 */
class Follow (var followerCount: Int = 0, var followingCount: Int = 0) {
    var followerMap: Map<String, Boolean> = HashMap()
    var followingMap: Map<String, Boolean> = HashMap()
}