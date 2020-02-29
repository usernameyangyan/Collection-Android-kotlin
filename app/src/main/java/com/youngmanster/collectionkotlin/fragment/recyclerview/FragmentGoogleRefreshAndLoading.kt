package com.youngmanster.collectionkotlin.fragment.recyclerview

import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.GoogleRefreshAdapter
import com.youngmanster.collectionkotlin.base.BaseFragment
import com.youngmanster.collectionkotlin.view.DefinitionAnimationLoadMoreView
import kotlinx.android.synthetic.main.fragment_google_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class FragmentGoogleRefreshAndLoading:BaseFragment<BasePresenter<*>>(), SwipeRefreshLayout.OnRefreshListener,
    PullToRefreshRecyclerView.OnRefreshAndLoadMoreListener{

    private val mDatas = ArrayList<String>()
    private var googleRefreshAdapter: GoogleRefreshAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_google_refresh
    }

    override fun init() {
        val layoutManager = GridLayoutManager(activity, 3)
        recycler_rv.layoutManager = layoutManager
        recycler_rv.setLoadMoreEnabled(true)
        recycler_rv.setRefreshAndLoadMoreListener(this)
        recycler_rv.setLoadMoreView(DefinitionAnimationLoadMoreView(activity))
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun requestData() {
        for (i in 0..14) {
            mDatas.add("Item" + (mDatas.size + 1))
        }
        refreshUI()
    }

    private fun refreshUI() {
        if (googleRefreshAdapter == null) {
            googleRefreshAdapter =
                GoogleRefreshAdapter(
                    activity!!,
                    R.layout.item_pull_refresh,
                    mDatas,
                    recycler_rv
                )
            recycler_rv.adapter = googleRefreshAdapter
        } else {
            if (swipeRefreshLayout!=null&&swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isRefreshing = false
                googleRefreshAdapter?.notifyDataSetChanged()
            }

            if (recycler_rv!=null&&recycler_rv.isLoading) {
                recycler_rv?.loadMoreComplete()
            }
        }
    }

    override fun onRefresh() {

        if (recycler_rv.isLoading) {
            swipeRefreshLayout.isRefreshing = false
        } else {
            Handler().postDelayed({
                mDatas.clear()
                requestData()
            }, 3000)
        }

    }

    override fun onRecyclerViewRefresh() {

    }

    override fun onRecyclerViewLoadMore() {

        if (swipeRefreshLayout.isRefreshing) {
            recycler_rv?.loadMoreComplete()
        } else {
            Handler().postDelayed({ requestData() }, 3000)
        }

    }

}