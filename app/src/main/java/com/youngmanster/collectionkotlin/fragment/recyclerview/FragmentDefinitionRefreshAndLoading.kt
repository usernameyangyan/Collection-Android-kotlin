package com.youngmanster.collectionkotlin.fragment.recyclerview

import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.DefinitionRecyclerAdapter
import com.youngmanster.collectionkotlin.base.BaseFragment
import com.youngmanster.collectionkotlin.view.DefinitionAnimationLoadMoreView
import com.youngmanster.collectionkotlin.view.DefinitionAnimationRefreshHeaderView
import kotlinx.android.synthetic.main.fragment_default_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class FragmentDefinitionRefreshAndLoading:BaseFragment<BasePresenter<*>>(), PullToRefreshRecyclerView.OnRefreshAndLoadMoreListener{

    private val mDatas = ArrayList<String>()
    private var definitionRefreshAdapter: DefinitionRecyclerAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_default_refresh
    }

    override fun init() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager
        recycler_rv.setPullRefreshEnabled(true)
        recycler_rv.setLoadMoreEnabled(true)
        recycler_rv.setRefreshAndLoadMoreListener(this)
        recycler_rv.setRefreshView(DefinitionAnimationRefreshHeaderView(activity))
        recycler_rv.setLoadMoreView(DefinitionAnimationLoadMoreView(activity))
    }

    override fun requestData() {
        for (i in 0..9) {
            mDatas.add("DefinitionRefreshItem" + (mDatas.size + 1))
        }
        refreshUI()
    }

    private fun refreshUI() {
        if (definitionRefreshAdapter == null) {
            definitionRefreshAdapter =
                DefinitionRecyclerAdapter(
                    activity!!,
                    R.layout.item_pull_refresh,
                    mDatas,
                    recycler_rv
                )
            recycler_rv.adapter = definitionRefreshAdapter
        } else {
            if (recycler_rv != null) {
                if (recycler_rv.isLoading) {
                    recycler_rv.loadMoreComplete()
                } else if (recycler_rv.isRefreshing) {
                    recycler_rv.refreshComplete()
                }
            }
        }
    }

    override fun onRecyclerViewRefresh() {
        Handler().postDelayed({
            mDatas.clear()
            requestData()
        }, 3000)
    }

    override fun onRecyclerViewLoadMore() {
        Handler().postDelayed({ requestData() }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler_rv?.destroy()
    }


}