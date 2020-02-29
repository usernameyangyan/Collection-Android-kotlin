package com.youngmanster.collectionkotlin.mvp.contract

import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.BaseView
import com.youngmanster.collectionkotlin.bean.WeChatNews

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
interface WeChatFeaturedContract {
    interface View : BaseView {
        fun refreshUI(newsList: List<WeChatNews>?)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun requestFeaturedNews(page: Int, num: Int)
    }
}