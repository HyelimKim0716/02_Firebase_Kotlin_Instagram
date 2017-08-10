package com.example.myapplication.view.tabbar.photo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.view.tabbar.photo.presenter.AddPhotoContract
import com.example.myapplication.view.tabbar.photo.presenter.AddPhotoPresenter
import kotlinx.android.synthetic.main.activity_add_photo.*

class AddPhotoActivity : AppCompatActivity(), AddPhotoContract.View {
    private var mPresenter: AddPhotoPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        mPresenter = AddPhotoPresenter(this, this)
        mPresenter?.requestPermission(this)

        btn_upload.setOnClickListener(btnUploadClickListener)

        mPresenter?.openAlbum()

    }

    override fun getExplain() : String = et_explain.text.toString()

    val btnUploadClickListener = View.OnClickListener {
        mPresenter?.uploadToFirebaseServer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // After selecting photo from gallery, change url to file path
        if (requestCode == mPresenter?.PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            mPresenter?.changeImageUrlToPath(data)
            iv_user.setImageURI(data?.data)

        }
    }
}
