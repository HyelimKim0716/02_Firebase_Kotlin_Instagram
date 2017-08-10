package com.example.myapplication.view.tabbar

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
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.model.Content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    private val PICK_FROM_ALBUM = 0
    private var imageUrl: String? = null

    private var mStorageRef: StorageReference? = null

    private var mDatabaseRef: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        // Request permission
        ActivityCompat.requestPermissions(this@AddPhotoActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        btn_upload.setOnClickListener(btnUploadClickListener)

        // Open Album
        val photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_FROM_ALBUM)

        mStorageRef = FirebaseStorage.getInstance().reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("message")

    }

    val btnUploadClickListener = View.OnClickListener {
        Log.d("AddPhotoActivity", "btnUploadClickListener")
        val file: File = File(imageUrl)
        val contentUri: Uri = Uri.fromFile(file)
//        val storageReference: StorageReference?
//                = mStorageRef?.getReferenceFromUrl("gs://instagram-71f48.appspot.com")
//                ?.child("images")
//                ?.child(contentUri.lastPathSegment)

        val storageRefChild: StorageReference? = mStorageRef?.child("images")
        val uploadTask: UploadTask? = storageRefChild?.putFile(contentUri)
        uploadTask?.addOnSuccessListener {
            takeSnapshot ->
            val uri: Uri? = takeSnapshot.downloadUrl

            // Create a database location which is binding and data to collection(table)
            val images = mDatabaseRef?.child("images")?.push()
            val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val content: Content = Content(
                    et_explain.text.toString(),
                    uri.toString(),
                    mAuth?.currentUser?.uid,
                    mAuth?.currentUser?.email,
                    sdf.format(Date()))

            images?.setValue(content)

            Toast.makeText(this@AddPhotoActivity, "Upload Success", Toast.LENGTH_SHORT).show()

            this@AddPhotoActivity.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // After selecting photo from gallery, change url to file path
        if (requestCode == PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                                                        // context, uri, projection, selection, selectionArgs, sortOrder
            val cursorLoader: CursorLoader = CursorLoader(this@AddPhotoActivity, data?.data, projection, null, null, null)
            val cursor: Cursor = cursorLoader.loadInBackground()
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()

            imageUrl = cursor.getString(columnIndex)
            iv_user.setImageURI(data?.data)

        }
    }
}
