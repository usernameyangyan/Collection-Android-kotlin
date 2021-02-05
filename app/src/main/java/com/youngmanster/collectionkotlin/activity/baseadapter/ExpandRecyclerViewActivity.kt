package com.youngmanster.collectionkotlin.activity.baseadapter

import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.tree.TreeItem
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.activity.baseadapter.holder.ExpandItem
import com.youngmanster.collectionkotlin.activity.baseadapter.holder.ExpandItem1
import com.youngmanster.collectionkotlin.adapter.baseadapter.ExpandRecyclerAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.layout_recyclerview.*

/**
 * Created by yangy
 *2021/1/8
 *Describe:
 */
class ExpandRecyclerViewActivity:BaseActivity<BasePresenter<*>>() {
    /**
     * 布局文件加载
     */
    override fun getLayoutId(): Int {
        return R.layout.layout_recyclerview
    }

    /**
     * 初始化参数
     */
    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_expand_title))

        val list=ArrayList<TreeItem  >()

        for(i in 0..5){
            val expandItem=ExpandItem()

            val children=ArrayList<ExpandItem1>()
            for(i in 0..2){
                val expandItem1= ExpandItem1()
                children.add(expandItem1)
            }

            expandItem.children=children

            list.add(expandItem)
        }

        val layoutManager = LinearLayoutManager(this)
        recycler_rv.layoutManager = layoutManager


        val demoRVAdapter = ExpandRecyclerAdapter()
        recycler_rv.adapter = demoRVAdapter
        demoRVAdapter.setDatas(list)

    }

    /**
     * 请求数据
     */
    override fun requestData() {
    }
}