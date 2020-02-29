package com.youngmanster.collectionkotlin.mvp.contract

import android.content.Context
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.BaseView
import com.youngmanster.collectionkotlin.bean.WeChatNews

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */
interface WeChatChinaNewsContract {
    interface View : BaseView {
        fun refreshUI(weChatNews: List<WeChatNews>?)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun requestChinaNews(context: Context, page: Int, num: Int)
    }
}
