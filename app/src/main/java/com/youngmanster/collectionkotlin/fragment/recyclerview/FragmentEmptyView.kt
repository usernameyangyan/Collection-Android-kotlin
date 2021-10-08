package com.youngmanster.collectionkotlin.fragment.recyclerview

import android.os.Handler
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.IBaseFragment
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.DefinitionRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_addheader_empty.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class FragmentEmptyView: IBaseFragment<BasePresenter<*>>(), PullToRefreshRecyclerView.OnRefreshAndLoadMoreListener{

    private val mDatas = ArrayList<String>()
    private var isFirst = true
    private var definitionRefreshAdapter: DefinitionRecyclerAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_addheader_empty
    }

    override fun init() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager
        recycler_rv.setPullRefreshEnabled(true)
        recycler_rv.setLoadMoreEnabled(true)
        recycler_rv.setRefreshAndLoadMoreListener(this)

        val emptyView = LayoutInflater.from(activity).inflate(R.layout.layout_empty, null)
        recycler_rv.emptyView = emptyView
        emptyView.setOnClickListener {
            for (i in 0..9) {
                mDatas.add("item$i")
            }
            definitionRefreshAdapter?.notifyDataSetChanged()
        }

    }

    override fun requestData() {
        if (!isFirst) {
            for (i in 0..14) {
                mDatas.add("Item" + (mDatas.size + 1))
            }
        }
        isFirst = false
        refreshUI()
    }

    private fun refreshUI() {
        if (definitionRefreshAdapter == null) {
            definitionRefreshAdapter =
                DefinitionRecyclerAdapter(
                    requireContext(),
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

}