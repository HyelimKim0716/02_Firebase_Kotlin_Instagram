package com.example.myapplication.model

/**
 * Created by HyelimKim on 2017-08-20.
 */
class Follow constructor() {
    constructor (followerCount: Int = 0, followingCount: Int = 0) : this()
    var followerMap: Map<String, Boolean> = HashMap()
    var followingMap: Map<String, Boolean> = HashMap()
}