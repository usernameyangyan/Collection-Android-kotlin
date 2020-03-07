package com.youngmanster.collectionkotlin.activity.base

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.activity.data.popup.PopupWindowDemoActivity
import com.youngmanster.collectionkotlin.activity.status.ChangeStatusBarActivity
import com.youngmanster.collectionkotlin.adapter.recyclerview.PullToRecyclerViewAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pull_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class BaseUiActivity:BaseActivity<BasePresenter<*>>(), BaseRecyclerViewAdapter.OnItemClickListener{

    private val mDatas = ArrayList<String>()
    private var pullToRefreshAdapter: PullToRecyclerViewAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_pull_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_base_ui_title))
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager
    }

    override fun requestData() {
        mDatas.add("StateView")
        mDatas.add("Permission")
        mDatas.add("Dialog的使用")
        mDatas.add("PopupWindow的使用")
        mDatas.add("使用DisplayUtils工具类修改状态栏")
        refreshUI()
    }

    private fun refreshUI() {
        pullToRefreshAdapter = PullToRecyclerViewAdapter(this,R.layout.item_pull_refresh, mDatas, recycler_rv)
        recycler_rv.adapter = pullToRefreshAdapter
        pullToRefreshAdapter?.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View, position: Int) {

        when(position){
            0 ->{
                startAc(StateViewActivity::class.java)
            }

            1 ->{
                startAc(PermissionActivity::class.java)
            }

            2 ->{
                startAc(DialogActivity::class.java)
            }

            3 ->{
                startAc(PopupWindowDemoActivity::class.java)
            }

            4 ->{
                startAc(ChangeStatusBarActivity::class.java)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recycler_rv?.destroy()
    }

}