package com.youngmanster.collectionkotlin.fragment.recyclerview

import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.DefinitionRecyclerAdapter
import com.youngmanster.collectionkotlin.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_nomore_auto.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class FragmentNoMoreData:BaseFragment<BasePresenter<*>>(), PullToRefreshRecyclerView.OnRefreshAndLoadMoreListener{
    private var definitionRefreshAdapter: DefinitionRecyclerAdapter? = null
    private val mDatas = ArrayList<String>()
    private var times = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_nomore_auto
    }

    override fun init() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager
        recycler_rv.setPullRefreshEnabled(true)
        recycler_rv.setLoadMoreEnabled(true)
        recycler_rv.setRefreshAndLoadMoreListener(this)
    }

    override fun requestData() {
        for (i in 0..9) {
            mDatas.add("Item" + (mDatas.size + 1))
        }
        refreshUI()
    }

    private fun refreshUI() {
        if (definitionRefreshAdapter == null) {
            definitionRefreshAdapter = DefinitionRecyclerAdapter(activity!!,R.layout.item_pull_refresh, mDatas, recycler_rv)
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
        times = 0
        Handler().postDelayed({
            mDatas.clear()
            requestData()
        }, 3000)
    }

    override fun onRecyclerViewLoadMore() {
        if (times < 1) {
            Handler().postDelayed({ requestData() }, 3000)
            times++
        } else {
            recycler_rv?.setNoMoreDate(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler_rv?.destroy()
    }

}