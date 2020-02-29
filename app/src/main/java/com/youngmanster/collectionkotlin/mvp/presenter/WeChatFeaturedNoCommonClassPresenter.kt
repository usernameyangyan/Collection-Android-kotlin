package com.youngmanster.collectionkotlin.mvp.presenter

import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collection_kotlin.network.RequestBuilder
import com.youngmanster.collection_kotlin.network.rx.RxObservableListener
import com.youngmanster.collectionkotlin.bean.WeChatNewsResult
import com.youngmanster.collectionkotlin.common.ApiUrl
import com.youngmanster.collectionkotlin.mvp.contract.WeChatFeaturedContract

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
class WeChatFeaturedNoCommonClassPresenter: WeChatFeaturedContract.Presenter(){
    override fun requestFeaturedNews(page: Int, num: Int) {
        val requestBuilder=
            RequestBuilder(object : RxObservableListener<WeChatNewsResult>(mView){
                override fun onNext(result: WeChatNewsResult) {
                    mView?.refreshUI(result.result)
                }
            })

        requestBuilder
            .setUrl(ApiUrl.URL_WETCHAT_FEATURED)
            .setTransformClass(WeChatNewsResult().javaClass)
            .setUserCommonClass(false)
            .setParam("page",page)
            .setParam("type","video")
            .setParam("count",num)

        rxManager.addObserver(DataManager.DataForHttp.httpRequest(requestBuilder))
    }

}