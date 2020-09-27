package com.youngmanster.collectionkotlin.mvp.presenter

import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collectionkotlin.mvp.view.IWeChatFeaturedView
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.network.RequestBuilder
import com.youngmanster.collection_kotlin.network.rx.RxObservableListener
import com.youngmanster.collectionkotlin.bean.HttpResult
import com.youngmanster.collectionkotlin.bean.WeChatNews
import com.youngmanster.collectionkotlin.common.ApiUrl

class WeChatFeaturedPresenter : BasePresenter<IWeChatFeaturedView>() {
    fun requestFeaturedNews(page: Int, num: Int) {

        val requestBuilder=
            RequestBuilder(object : RxObservableListener<HttpResult<List<WeChatNews>>>(mView){
            override fun onNext(result: HttpResult<List<WeChatNews>>) {
                mView?.refreshUI(result.result)
            }
        })

        requestBuilder
            .setUrl(ApiUrl.URL_WETCHAT_FEATURED)
            .setTransformClass(WeChatNews().javaClass)
            .setHttpTypeAndReqType(RequestBuilder.HttpType.DEFAULT_GET, RequestBuilder.ReqType.DEFAULT_CACHE_LIST)
            .setParam("page",page)
            .setParam("type","video")
            .setParam("count",num)

        rxManager.addObserver(DataManager.DataForHttp.httpRequest(requestBuilder))
    }

}