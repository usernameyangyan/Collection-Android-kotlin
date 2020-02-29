package com.youngmanster.collectionkotlin.adapter.baseadapter

import android.content.Context
import androidx.cardview.widget.CardView
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewMultiItemAdapter
import com.youngmanster.collection_kotlin.recyclerview.BaseViewHolder
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.bean.MultiItem

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class MultipleAdapter(mContext: Context, mDatas: ArrayList<MultiItem>) :
    BaseRecyclerViewMultiItemAdapter<MultiItem>(mContext, mDatas) {

    private var mHeight: Int=0

    init {
        mHeight = DisplayUtils.dip2px(mContext, 100f)
        addItemType(MultiItem.TYPE_TEXT, R.layout.item_main)
        addItemType(MultiItem.TYPE_IMG, R.layout.item_img)
        addItemType(MultiItem.TYPE_TEXT_IMG, R.layout.item_click)
    }

    override fun convert(baseViewHolder: BaseViewHolder, t: MultiItem) {

        when (baseViewHolder.itemViewType) {
            MultiItem.TYPE_TEXT -> {
                baseViewHolder.getView<CardView>(R.id.card_view).layoutParams.height = mHeight
                baseViewHolder.setText(R.id.title, t.title)
            }
            MultiItem.TYPE_IMG -> baseViewHolder.setImageResource(R.id.ivImg, t.res)
            MultiItem.TYPE_TEXT_IMG -> {
                baseViewHolder.setImageResource(R.id.ivImg, t.res)
                baseViewHolder.setText(R.id.titleTv, t.title)
            }
        }
    }

}