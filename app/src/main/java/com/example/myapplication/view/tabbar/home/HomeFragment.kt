package com.example.myapplication.view.tabbar.home

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.adapter.HomeRvImageAdapter
import com.example.myapplication.model.Content
import com.example.myapplication.view.tabbar.home.presenter.HomeContract
import com.example.myapplication.view.tabbar.home.presenter.HomePresenter
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Owner on 2017-08-10.
 */

class HomeFragment : Fragment(), HomeContract.View {
    val TAG = "HomeFragment"

    val mPresenter: HomeContract.Presenter by lazy {
        HomePresenter(this)
    }

    var mAdapter: HomeRvImageAdapter? = null

    companion object {
        private var instance: HomeFragment? = null

        fun create(): HomeFragment {
            if (instance == null)
                return HomeFragment()
            else
                return instance!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater?.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        mAdapter = HomeRvImageAdapter(context)

        rv_image.layoutManager = GridLayoutManager(context, 3)
        rv_image.setHasFixedSize(true)
        rv_image.adapter = mAdapter

        mPresenter.loadImages()
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun addItems(item: Content) {
        Log.d("HomeFragment", "addItems " + item.explain)
        mAdapter?.addItem(item)
    }

    override fun clearItems() {
        mAdapter?.clearItemList()
    }

    override fun notifyDataSetChanged() {
        mAdapter?.notifyDataSetChanged()
    }


}