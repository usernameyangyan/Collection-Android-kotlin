package com.youngmanster.collectionkotlin.adapter.data

import android.content.Context
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.recyclerview.BaseViewHolder
import com.youngmanster.collectionkotlin.R

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class ItemRecycleAdapter(mContext: Context, mLayoutResId: Int, mDatas: ArrayList<String>) :
    BaseRecyclerViewAdapter<String>(mContext, mLayoutResId, mDatas) {

    override fun convert(baseViewHolder: BaseViewHolder, t: String) {
        baseViewHolder.setText(R.id.tv_Label, t)
    }

}