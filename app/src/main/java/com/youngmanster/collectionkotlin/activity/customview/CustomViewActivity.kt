package com.youngmanster.collectionkotlin.activity.customview

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.PullToRecyclerViewAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class CustomViewActivity:BaseActivity<BasePresenter<*>>(), BaseRecyclerViewAdapter.OnItemClickListener{

    private val mDatas = ArrayList<String>()
    private var pullToRefreshAdapter: PullToRecyclerViewAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_pull_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.custom_view_title))

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager

    }

    override fun requestData() {
        mDatas.add("CommonTabLayout")
        mDatas.add("OutSideFrameTabLayout")
        mDatas.add("AutoLineLayout")
        mDatas.add("TagView")
        refreshUI()
    }

    private fun refreshUI() {
        pullToRefreshAdapter = PullToRecyclerViewAdapter(this,R.layout.item_pull_refresh, mDatas, recycler_rv)
        recycler_rv.adapter = pullToRefreshAdapter
        pullToRefreshAdapter?.setOnItemClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        recycler_rv.destroy()
    }

    override fun onItemClick(view: View, position: Int) {
        when (position) {
            0 -> {
                startAc(CommonTabLayoutActivity().javaClass)
            }
            1 -> {
               startAc(OutSideFrameTabLayoutActivity().javaClass)
            }
            2 -> {
                startAc(WrapLinearLayoutActivity().javaClass)
            }

            3 ->{
                startAc(TagViewActivity().javaClass)
            }
        }
    }

}