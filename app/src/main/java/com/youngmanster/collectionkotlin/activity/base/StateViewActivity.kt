package com.youngmanster.collectionkotlin.activity.base

import android.os.Handler
import androidx.recyclerview.widget.GridLayoutManager
import com.youngmanster.collection_kotlin.base.stateview.StateView
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.LogUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.DefaultRecyclerAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pull_refresh.*
import kotlinx.android.synthetic.main.layout_state.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class StateViewActivity :BaseActivity<BasePresenter<*>>(), StateView.OnEmptyViewListener{

    private var defaultRefreshAdapter: DefaultRecyclerAdapter? = null
    private val mDatas = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_state_view
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_state_view))
        state_view.showViewByState(StateView.STATE_LOADING)
        state_view.setOnEmptyViewListener(this)
        val layoutManager = GridLayoutManager(this, 3)
        recycler_rv.layoutManager = layoutManager

    }

    override fun requestData() {
        Handler().postDelayed({ refreshUI() }, 3000)
    }

    private fun refreshUI() {
        if (state_view != null) {
            state_view.showViewByState(StateView.STATE_EMPTY)
        }
    }


    override fun onEmptyViewClick() {
        state_view.showViewByState(StateView.STATE_LOADING)

        Handler().postDelayed({
            for (i in 0..14) {
                mDatas.add("Item" + (mDatas.size + 1))
            }
            setData()
        }, 3000)
    }

    private fun setData() {
        defaultRefreshAdapter = DefaultRecyclerAdapter(this,R.layout.item_pull_refresh, mDatas, recycler_rv)
        recycler_rv?.adapter = defaultRefreshAdapter
        state_view.showViewByState(StateView.STATE_NO_DATA)
    }
}