package com.example.myapplication.tabbar

import android.Manifest
import android.app.Activity
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.myapplication.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.io.File

class AddPhotoActivity : AppCompatActivity() {

    private val PICK_FROM_ALBUM = 0
    private var imageUrl: String? = null

    private var mFirebaseStorage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        // Request permission
        ActivityCompat.requestPermissions(this@AddPhotoActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        // Open Album
        val photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_FROM_ALBUM)

        mFirebaseStorage = FirebaseStorage.getInstance()
    }

    val btnUploadClickListener = View.OnClickListener {
        val file: File = File(imageUrl)
        val contentUri: Uri = Uri.fromFile(file)
        val storageReference: StorageReference?
                = mFirebaseStorage?.getReferenceFromUrl("자신의 Storage GS 입력")
                ?.child("images")
                ?.child(contentUri.lastPathSegment)

        val uploadTask: UploadTask? = storageReference?.putFile(contentUri)
        uploadTask?.addOnSuccessListener {
            Toast.makeText(this@AddPhotoActivity, "Upload Success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            val mediaData: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
            val cursorLoader: CursorLoader = CursorLoader(this@AddPhotoActivity, data?.data, mediaData, null, null, null)
            val cursor: Cursor = cursorLoader.loadInBackground()
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()

            imageUrl = cursor.getString(columnIndex)
            iv_user.setImageURI(data?.data)

        }
    }
}
