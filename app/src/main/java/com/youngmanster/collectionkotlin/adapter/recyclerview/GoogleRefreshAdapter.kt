package com.youngmanster.collectionkotlin.adapter.recyclerview

import android.content.Context
import androidx.cardview.widget.CardView
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.recyclerview.BaseViewHolder
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collectionkotlin.R

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */
class GoogleRefreshAdapter(
    mContext: Context,
    mLayoutResId: Int,
    mDatas: ArrayList<String>,
    pullToRefreshRecyclerView: PullToRefreshRecyclerView
) : BaseRecyclerViewAdapter<String>(mContext, mLayoutResId, mDatas, pullToRefreshRecyclerView) {

    var mScreenWidth: Int=0
    var mItemWidth: Int=0

    init {
        mScreenWidth = DisplayUtils.getScreenWidthPixels()
        mItemWidth = (mScreenWidth - DisplayUtils.dip2px(30f)) / 3
    }

    override fun convert(baseViewHolder: BaseViewHolder, t: String) {
        baseViewHolder.setText(R.id.title, t)
        baseViewHolder.getView<CardView>(R.id.card_view).layoutParams?.height = mItemWidth
        baseViewHolder.getView<CardView>(R.id.card_view).layoutParams?.width = mItemWidth
    }

}