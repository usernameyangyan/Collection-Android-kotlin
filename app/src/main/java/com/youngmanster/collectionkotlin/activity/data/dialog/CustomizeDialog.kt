package com.youngmanster.collectionkotlin.activity.data.dialog

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youngmanster.collection_kotlin.base.dialog.BaseDialogFragment
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.data.ItemRecycleAdapter
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class CustomizeDialog() : BaseDialogFragment(),
    BaseRecyclerViewAdapter.OnItemClickListener {


    private var itemRecycleAdapter: ItemRecycleAdapter? = null
    private val itemStr = ArrayList<String>()

    private var rv_Item: RecyclerView? = null


    override fun setContentView(): Int {
        return R.layout.dialog_list
    }

    override fun onViewCreated() {
        rv_Item = mainView?.findViewById(R.id.rv_Item)
        rv_Item?.layoutManager = LinearLayoutManager(context)
        for (i in 0..9) {
            itemStr.add("这是第" + (i + 1) + "个item")
        }
        itemRecycleAdapter = ItemRecycleAdapter(context!!, R.layout.item_layout, itemStr)
        rv_Item?.adapter = itemRecycleAdapter
        itemRecycleAdapter?.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View, position: Int) {
        ToastUtils.showToast(context, itemStr[position])
    }

}