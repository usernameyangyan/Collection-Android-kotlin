package com.youngmanster.collectionkotlin.fragment.mvp

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.mvp.annotation.MvpAnnotation
import com.youngmanster.collection_kotlin.base.stateview.StateView
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.BaseView
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.mvp.WeChatFeaturedAdapter
import com.youngmanster.collectionkotlin.base.BaseFragment
import com.youngmanster.collectionkotlin.bean.WeChatNews
import com.youngmanster.collectionkotlin.mvp.presenter.WeChatWorldNewsDefinitionPresenter
import com.youngmanster.collectionkotlin.mvp.view.IWeChatWorldNewsDefinitionView
import kotlinx.android.synthetic.main.fragment_wechat_news1.*
import kotlinx.android.synthetic.main.layout_state.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */
@MvpAnnotation(
    prefixName = "WeChatWorldNewsDefinition",
    basePresenterClazz = BasePresenter::class,
    baseViewClazz = BaseView::class
)
class FragmentWorldNewsDefinition :BaseFragment<WeChatWorldNewsDefinitionPresenter>(),
    IWeChatWorldNewsDefinitionView, PullToRefreshRecyclerView.OnRefreshAndLoadMoreListener,
    SwipeRefreshLayout.OnRefreshListener{


    private val PAGE_SIZE = 15
    private var pageSize = 1

    private val mDatas = ArrayList<WeChatNews>()
    private var weChatFeaturedAdapter: WeChatFeaturedAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_wechat_news1
    }

    override fun init() {
        state_view.showViewByState(StateView.STATE_LOADING)
        state_view.setOnDisConnectViewListener(object :StateView.OnDisConnectListener{
            override fun onDisConnectViewClick() {
                requestData()
            }

        })


        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        refreshRv.layoutManager = linearLayoutManager
        refreshRv.setPullRefreshEnabled(false)
        refreshRv.setLoadMoreEnabled(true)
        refreshRv.setRefreshAndLoadMoreListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun requestData() {
        mPresenter?.requestWorldNews(activity!!,pageSize, PAGE_SIZE)
    }

    override fun refreshUI(weChatNews: List<WeChatNews>?) {
        if (weChatNews != null) {
            if (pageSize == 1) {
                mDatas.clear()
                mDatas.addAll(weChatNews)
            } else {
                mDatas.addAll(weChatNews)
            }

        }

        if (weChatFeaturedAdapter == null) {
            if (mDatas.size == 0) {
                state_view.showViewByState(StateView.STATE_EMPTY)
            } else {
                state_view.showViewByState(StateView.STATE_NO_DATA)
            }
            weChatFeaturedAdapter = WeChatFeaturedAdapter(activity,R.layout.item_wechat_featured, mDatas, refreshRv)
            refreshRv.adapter = weChatFeaturedAdapter
        } else {

            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isRefreshing = false
                weChatFeaturedAdapter?.notifyDataSetChanged()
            } else if (refreshRv.isLoading) {
                refreshRv.loadMoreComplete()
                if (weChatNews == null || weChatNews.isEmpty()) {
                    refreshRv.setNoMoreDate(true)
                }
            }
        }
    }

    override fun onError(errorMsg: String) {
        ToastUtils.showToast(activity,errorMsg)
        if(mDatas.size == 0){
            state_view.showViewByState(StateView.STATE_DISCONNECT)
        }
    }

    override fun onRecyclerViewRefresh() {
    }

    override fun onRecyclerViewLoadMore() {
        pageSize++
        requestData()
    }

    override fun onRefresh() {
        pageSize = 1
        requestData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshRv.destroy()
    }

}