package com.example.myapplication.view.tabbar.detail


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.adapter.DetailRvContentAdapter
import com.example.myapplication.model.Content
import com.example.myapplication.view.tabbar.detail.presenter.DetailContract
import com.example.myapplication.view.tabbar.detail.presenter.DetailPresenter
import kotlinx.android.synthetic.main.fragment_detail.*


/**
 * Created by Owner on 2017-08-10.
 */
class DetailFragment : Fragment(), DetailContract.View {

    private lateinit var mAdapter: DetailRvContentAdapter
    private lateinit var mPresenter: DetailPresenter

    companion object {
        fun getInstance() = DetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater?.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = DetailPresenter(this)

        rv_detail_content.layoutManager = LinearLayoutManager(context)
        mAdapter = DetailRvContentAdapter(context)
        rv_detail_content.adapter = mAdapter

    }

    override fun clearContentList() {
        mAdapter.clearContentList()
    }

    override fun clearContentUidList() {
        mAdapter.clearContentUidList()
    }

    override fun addContent(content: Content) {
        mAdapter.addContent(content)
    }

    override fun addContentUid(key: String) {
        mAdapter.addContentUid(key)
    }

    override fun notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged()
    }


}