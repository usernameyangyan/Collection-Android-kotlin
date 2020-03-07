package com.youngmanster.collectionkotlin.activity.recyclerview

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.PullToRecyclerViewAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_pull_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class RecyclerViewActivity:BaseActivity<BasePresenter<*>>(), BaseRecyclerViewAdapter.OnItemClickListener{


    private val mDatas = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_pull_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_pull_refresh_title))

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager
    }

    override fun requestData() {
        mDatas.add("Refresh/LoadingMore")
        mDatas.add("AddHeader/EmptyView")
        mDatas.add("NoMoreData/AutoRefresh")

        val pullToRefreshAdapter =
            PullToRecyclerViewAdapter(
                this,
                R.layout.item_pull_refresh,
                mDatas,
                recycler_rv
            )
        recycler_rv.adapter = pullToRefreshAdapter
        pullToRefreshAdapter.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View, position: Int) {
        when(position){
            0 -> {
                startAc(RefreshActivity::class.java)
            }

            1 -> {
                startAc(HeaderAndEmptyViewActivity::class.java)
            }

            2 -> {
                startAc(NoMoreDateAndAutoRefreshActivity::class.java)
            }
        }
    }

}