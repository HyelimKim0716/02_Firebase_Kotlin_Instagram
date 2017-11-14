package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Owner on 2017-08-10.
 */
class Content constructor(){

    constructor(explain: String,
                imageUrl: String,
                uid: String?,
                userId: String?,
                timestamp: String) : this()

    var favoriteCount: Int = 0
    var favoriteMap: Map<String, Boolean> = HashMap()
    var commentMap: Map<String, Comment> = HashMap()

}

data class Comment(var uid: String?, var userId: String?, var comment: String?)