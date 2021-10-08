package com.youngmanster.collectionkotlin.adapter

import android.content.Context
import androidx.cardview.widget.CardView
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collection_kotlin.recyclerview.BaseViewHolder
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collection_kotlin.utils.DisplayUtils

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class MainViewAdapter(
    mContext: Context,
    mLayoutResId: Int,
    mDatas: ArrayList<String>,
    pullToRefreshRecyclerView: PullToRefreshRecyclerView
) : BaseRecyclerViewAdapter<String>(mContext, mLayoutResId, mDatas, pullToRefreshRecyclerView) {


    private var mScreenWidth: Int=0
    private var mItemWidth: Int=0

    init {
        mScreenWidth = DisplayUtils.getScreenWidthPixels()
        mItemWidth = (mScreenWidth - DisplayUtils.dip2px(20f)) / 2
    }

    override fun convert(baseViewHolder: BaseViewHolder, t: String) {
        baseViewHolder.setText(R.id.title, t)
        baseViewHolder.getView<CardView>(R.id.card_view)?.layoutParams?.height = mItemWidth
        baseViewHolder.getView<CardView>(R.id.card_view)?.layoutParams?.width = mItemWidth
    }
}