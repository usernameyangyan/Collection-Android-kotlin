package com.youngmanster.collectionkotlin.fragment.recyclerview

import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.DefaultRecyclerAdapter
import com.youngmanster.collectionkotlin.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_default_refresh.*
import java.util.ArrayList


/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class FragmentDefaultRefreshAndLoading:BaseFragment<BasePresenter<*>>(), PullToRefreshRecyclerView.OnRefreshAndLoadMoreListener{

    private val mDatas = ArrayList<String>()
    private var defaultRefreshAdapter: DefaultRecyclerAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_default_refresh
    }

    override fun init() {
        val layoutManager = GridLayoutManager(activity, 3)
        recycler_rv.layoutManager = layoutManager
        recycler_rv.setPullRefreshEnabled(true)
        recycler_rv.setLoadMoreEnabled(true)
        recycler_rv.setRefreshAndLoadMoreListener(this)
    }

    override fun requestData() {
        for (i in 0..14) {
            mDatas.add("Item" + (mDatas.size + 1))
        }
        refreshUI()


    }

    private fun refreshUI() {
        if (defaultRefreshAdapter == null) {
            defaultRefreshAdapter =
                DefaultRecyclerAdapter(
                    activity!!,
                    R.layout.item_pull_refresh,
                    mDatas,
                    recycler_rv
                )
            recycler_rv.adapter = defaultRefreshAdapter
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