package com.youngmanster.collectionkotlin.activity.baseadapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.baseadapter.ItemClickAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.bean.ClickItem
import kotlinx.android.synthetic.main.layout_recyclerview.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class ItemClickActivity:BaseActivity<BasePresenter<*>>(), BaseRecyclerViewAdapter.OnItemClickListener,BaseRecyclerViewAdapter.onItemLongClickListener,
    View.OnClickListener{

    private val mDatas = ArrayList<ClickItem>()
    private var itemClickAdapter: ItemClickAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.layout_recyclerview
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_item_click_title))

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        recycler_rv.layoutManager = layoutManager
        for (i in 0..19) {
            val clickItem = ClickItem()
            clickItem.title="第" + (i + 1) + "个item"
            clickItem.res=R.mipmap.header
            mDatas.add(clickItem)
        }
        itemClickAdapter = ItemClickAdapter(this,R.layout.item_click, mDatas, this)

        recycler_rv.adapter = itemClickAdapter
        itemClickAdapter?.setOnItemClickListener(this)
        itemClickAdapter?.setOnItemLongClickListener(this)
    }

    override fun requestData() {
    }



    override fun onClick(v: View?) {
        ToastUtils.showToast(this, "其实点击我没有奖")

    }

    override fun onItemClick(view: View, position: Int) {
        ToastUtils.showToast(this, mDatas[position].title!!)

    }

    override fun onItemLongClick(view: View, position: Int): Boolean {
        ToastUtils.showToast(this, "进行长按操作")
        return true
    }

}