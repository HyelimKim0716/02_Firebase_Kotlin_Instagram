package com.example.myapplication.view.tabbar.user

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.adapter.UserRvAdapter
import com.example.myapplication.view.tabbar.user.presenter.UserContract
import com.example.myapplication.view.tabbar.user.presenter.UserPresenter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * Created by HyelimKim on 2017-08-20.
 */
class UserFragment : Fragment(), UserContract.View {

    lateinit var mPresenter : UserPresenter
    val mDestinationUid by lazy {
        arguments?.getString("destinationUid")
    }

    val uid by lazy {
        FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater?.inflate(R.layout.fragment_user, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_user.layoutManager = GridLayoutManager(context, 3)
        rv_user.adapter = UserRvAdapter(context)
        btn_follow.setOnClickListener {
            mPresenter.requestFollow(mDestinationUid!!, uid!!)
        }



        if (mDestinationUid == uid)
            btn_follow.isEnabled = false
    }



}