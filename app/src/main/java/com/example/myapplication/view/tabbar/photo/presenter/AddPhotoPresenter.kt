package com.example.myapplication.view.tabbar.photo.presenter

import android.app.Activity
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.example.myapplication.model.Content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Owner on 2017-08-10.
 */
class AddPhotoPresenter(override val view: AddPhotoContract.View, val activity: Activity) : AddPhotoContract.Presenter {
    val PICK_FROM_ALBUM = 0

    private val mStorage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val mDatabase: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var mImageUrl: String? = null

    /** Request permission */
    override fun requestPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
    }

    override fun openAlbum() {
        val photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        activity.startActivityForResult(photoPickerIntent, PICK_FROM_ALBUM)
    }

    override fun uploadToFirebaseServer() {
        Log.d("AddPhotoPresenter", "btnUploadClickListener, uploadToFirebaseServer")

        val file: File = File(mImageUrl)
        val contentUri: Uri = Uri.fromFile(file)
        val storageReference: StorageReference?
                = mStorage.getReferenceFromUrl("gs://instagram-71f48.appspot.com")
                .child("images")
                .child(contentUri.lastPathSegment)

        val uploadTask: UploadTask? = storageReference?.putFile(contentUri)
        uploadTask?.addOnSuccessListener {
            takeSnapshot ->
            val uri: Uri? = takeSnapshot.downloadUrl

            // Create a database location which is binding and data to collection(table)
            val images = mDatabase.reference.child("images").push()
            val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val content: Content = Content(
                    view.getExplain(),
                    uri.toString(),
                    mAuth.currentUser?.uid,
                    mAuth.currentUser?.email,
                    sdf.format(Date()))


            images?.setValue(content)

            Toast.makeText(activity, "Upload Success", Toast.LENGTH_SHORT).show()

            activity.finish()
        }
    }

    override fun changeImageUrlToPath(data: Intent?) {
        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                                                    // context, uri, projection, selection, selectionArgs, sortOrder
        val cursorLoader: CursorLoader = CursorLoader(activity, data?.data, projection, null, null, null)
        val cursor: Cursor = cursorLoader.loadInBackground()
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()

        mImageUrl = cursor.getString(columnIndex)
    }
}