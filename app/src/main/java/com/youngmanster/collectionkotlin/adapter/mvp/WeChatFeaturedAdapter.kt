package com.youngmanster.collectionkotlin.adapter.mvp

import android.content.Context
import android.widget.ImageView
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.recyclerview.BaseViewHolder
import com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
import com.youngmanster.collection_kotlin.utils.GlideUtils
import com.youngmanster.collection_kotlin.utils.LogUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.bean.WeChatNews

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class WeChatFeaturedAdapter(
    mContext: Context?,
    mLayoutResId: Int,
    mDatas: ArrayList<WeChatNews>,
    pullToRefreshRecyclerView: PullToRefreshRecyclerView
) : BaseRecyclerViewAdapter<WeChatNews>(mContext, mLayoutResId, mDatas, pullToRefreshRecyclerView) {

    override fun convert(baseViewHolder: BaseViewHolder, t: WeChatNews) {
        baseViewHolder.setText(R.id.weChatTitleTv, t.name)
            .setText(R.id.weChatNameTv, t.text)
            .setText(R.id.weChatTimeTv, t.passtime)

        val imageView = baseViewHolder.getView<ImageView>(R.id.weChatIv)

        GlideUtils.loadImg(
            mContext,
            t.thumbnail,
            R.mipmap.ic_bttom_loading_01,
            imageView
        )
    }

}