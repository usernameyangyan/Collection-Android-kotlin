package com.youngmanster.collectionkotlin.adapter.recyclerview

import android.content.Context
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collection_kotlin.recyclerview.BaseViewHolder
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class PullToRecyclerViewAdapter(
    mContext: Context,
    mLayoutResId: Int,
    mDatas: ArrayList<String>,
    pullToRefreshRecyclerView: PullToRefreshRecyclerView
) : BaseRecyclerViewAdapter<String>(mContext, mLayoutResId, mDatas, pullToRefreshRecyclerView) {


    override fun convert(baseViewHolder: BaseViewHolder, t: String) {
        baseViewHolder.setText(R.id.title, t)
    }

}