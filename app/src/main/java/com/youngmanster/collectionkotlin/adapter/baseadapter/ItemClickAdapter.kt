package com.youngmanster.collectionkotlin.adapter.baseadapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.recyclerview.BaseViewHolder
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.bean.ClickItem

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class ItemClickAdapter(mContext: Context, mLayoutResId: Int, mDatas: ArrayList<ClickItem>,onClickListener: View.OnClickListener) :
    BaseRecyclerViewAdapter<ClickItem>(mContext, mLayoutResId, mDatas) {

    private var onClickListener: View.OnClickListener?=null

    init {
        this.onClickListener = onClickListener
    }

    override fun convert(baseViewHolder: BaseViewHolder, t: ClickItem) {
        baseViewHolder.setText(R.id.titleTv, t.title)
            .setImageResource(R.id.ivImg, t.res)
            .setOnClickListener(R.id.clickTv, onClickListener)

        baseViewHolder.getView<TextView>(R.id.clickTv).visibility = View.VISIBLE
    }

}