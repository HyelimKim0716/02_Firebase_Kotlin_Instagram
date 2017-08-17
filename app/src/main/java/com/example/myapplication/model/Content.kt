package com.example.myapplication.model

/**
 * Created by Owner on 2017-08-10.
 */
data class Content(
        var explain: String,
        var imageUrl: String,
        var uid: String?,
        var userId: String?,
        var timestamp: String) {

    var favoriteCount: Int = 0
    var favoriteMap: Map<String, Boolean> = HashMap()
    var commentMap: Map<String, Comment> = HashMap()
}

data class Comment(var uid: String?, var userId: String?, var comment: String?)