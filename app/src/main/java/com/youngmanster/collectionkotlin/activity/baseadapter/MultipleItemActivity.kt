package com.youngmanster.collectionkotlin.activity.baseadapter

import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.baseadapter.MultipleAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.bean.MultiItem
import kotlinx.android.synthetic.main.layout_recyclerview.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class MultipleItemActivity:BaseActivity<BasePresenter<*>>(){

    private var multiAdapter: MultipleAdapter? = null
    private val mDatas = ArrayList<MultiItem>()

    override fun getLayoutId(): Int {
        return R.layout.layout_recyclerview
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_multiple_title))
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = layoutManager
    }

    override fun requestData() {
        for (i in 0..19) {
            val multiItem = MultiItem()
            multiItem.title="第" + i + "个item"
            multiItem.res=R.mipmap.header

            if (i < 10) {
                when {
                    i % 3 == 0 -> multiItem.type=MultiItem.TYPE_TEXT
                    i % 3 == 1 -> multiItem.type=MultiItem.TYPE_IMG
                    else -> multiItem.type=MultiItem.TYPE_TEXT_IMG
                }
            } else {
                if (i % 3 == 1) {
                    multiItem.type=MultiItem.TYPE_TEXT
                } else if (i % 3 == 0) {
                    multiItem.type=MultiItem.TYPE_IMG
                } else {
                    multiItem.type=MultiItem.TYPE_TEXT_IMG
                }
            }
            mDatas.add(multiItem)
        }


        multiAdapter = MultipleAdapter(this, mDatas)
        recycler_rv.adapter = multiAdapter

    }

}