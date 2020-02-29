package com.youngmanster.collectionkotlin.adapter.baseadapter

import android.content.Context
import androidx.cardview.widget.CardView
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.recyclerview.BaseViewHolder
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collectionkotlin.R

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
class DragAndDeleteAdapter(mContext: Context, mLayoutResId: Int, mDatas: ArrayList<String>) :
    BaseRecyclerViewAdapter<String>(mContext, mLayoutResId, mDatas) {

    private var mHeight: Int=0

    init {
        mHeight = DisplayUtils.dip2px(mContext, 100f)
    }

    override fun convert(baseViewHolder: BaseViewHolder, t: String) {
        baseViewHolder.setText(R.id.title, t)
        baseViewHolder.getView<CardView>(R.id.card_view).layoutParams.height = mHeight
    }

}