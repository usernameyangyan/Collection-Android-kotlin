package com.youngmanster.collectionkotlin.adapter.baseadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.youngmanster.collection_kotlin.recyclerview.tree.TreeItem
import com.youngmanster.collection_kotlin.recyclerview.tree.base.BaseTreeAdapter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.activity.baseadapter.holder.DiscountFirstVH
import com.youngmanster.collectionkotlin.activity.baseadapter.holder.DiscountSecondVH

/**
 * Created by yangy
 *2021/1/8
 *Describe:
 */
class ExpandRecyclerAdapter:BaseTreeAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        var vh: RecyclerView.ViewHolder?=null

        when (viewType) {
            0 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_discount_first, parent, false)
                vh = DiscountFirstVH(view)
            }
            1 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_discount_second, parent, false)
                vh = DiscountSecondVH(view)
            }
        }

        return vh!!
    }

    override fun onBindViewHolder(type: Int, tree: TreeItem?, holder: RecyclerView.ViewHolder?) {
        when (type) {
            0 -> {
                val fHolder = holder as DiscountFirstVH
                fHolder.tvName!!.text = "第一级数据"
                fHolder.itemView.setOnClickListener {
                    tree!!.isOpen = !tree.isExpand
                    notifyDataSetChanged()
                }
            }
            1 -> {
                val sHolder = holder as DiscountSecondVH
                sHolder.tvName!!.text = "第二级数据"
            }
        }
    }
}