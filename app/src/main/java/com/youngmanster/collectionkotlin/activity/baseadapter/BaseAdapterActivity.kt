package com.youngmanster.collectionkotlin.activity.baseadapter

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.PullToRecyclerViewAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pull_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
class BaseAdapterActivity:BaseActivity<BasePresenter<*>>(), BaseRecyclerViewAdapter.OnItemClickListener{

    private val mDatas = ArrayList<String>()
    private var pullToRefreshAdapter: PullToRecyclerViewAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_pull_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_base_adapter_title))

        val header = LayoutInflater.from(this).inflate(R.layout.layout_base_adapter_header, null)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager
        recycler_rv.addHeaderView(header)

        mDatas.add("ItemClick/ItemLongClick")
        mDatas.add("多布局")
        mDatas.add("item拖拽/item滑动删除")
        mDatas.add(getString(R.string.activity_expand_title))
        refreshUI()
    }

    private fun refreshUI() {
        pullToRefreshAdapter = PullToRecyclerViewAdapter(this,R.layout.item_pull_refresh ,mDatas, recycler_rv)
        recycler_rv.adapter = pullToRefreshAdapter
        pullToRefreshAdapter?.setOnItemClickListener(this)
    }

    override fun requestData() {
    }



    override fun onDestroy() {
        super.onDestroy()
        recycler_rv.destroy()
    }

    override fun onItemClick(view: View, position: Int) {
        when(position) {
            0 -> {
                startAc(ItemClickActivity::class.java)
            }

            1 -> {
                startAc(MultipleItemActivity::class.java)
            }

            2 -> {
                startAc(DragAndDeleteActivity::class.java)
            }

            3 -> {
                startAc(ExpandRecyclerViewActivity::class.java)
            }
        }
    }

}
