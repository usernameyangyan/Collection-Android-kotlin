package com.youngmanster.collectionkotlin.mvp.presenter

import android.app.Activity
import android.content.Context
import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collectionkotlin.mvp.view.IWeChatFeaturedNoCommonClassView
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.network.RequestBuilder
import com.youngmanster.collection_kotlin.network.rx.RxObservableListener
import com.youngmanster.collectionkotlin.bean.WeChatNewsResult
import com.youngmanster.collectionkotlin.common.ApiUrl

class WeChatFeaturedNoCommonClassPresenter : BasePresenter<IWeChatFeaturedNoCommonClassView>() {
    fun requestFeaturedNews(context: Activity,page: Int, num: Int) {
        val requestBuilder=
            RequestBuilder(object : RxObservableListener<WeChatNewsResult>(){
                override fun onNext(result: WeChatNewsResult) {
                    mView?.refreshUI(result.result)
                }
            })

        requestBuilder
            .setUrl(ApiUrl.URL_WETCHAT_FEATURED)
            .setTransformClass(WeChatNewsResult().javaClass)
            .setUseCommonClass(false)
            .setHttpTypeAndReqType(RequestBuilder.HttpType.DEFAULT_GET, RequestBuilder.ReqType.DEFAULT_CACHE_LIST)
            .setParam("page",page)
            .setParam("type","video")
            .setParam("count",num)

        rxManager.addObserver(DataManager.DataForHttp.httpRequest(requestBuilder))
    }
}