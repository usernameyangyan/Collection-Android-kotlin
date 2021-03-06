package com.youngmanster.collectionkotlin.mvp.presenter

import android.app.Activity
import android.content.Context
import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collectionkotlin.mvp.view.IWeChatChinaNewsDefinitionView
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.network.RequestBuilder
import com.youngmanster.collection_kotlin.network.rx.RxObservableListener
import com.youngmanster.collectionkotlin.bean.HttpResult
import com.youngmanster.collectionkotlin.bean.WeChatNews
import com.youngmanster.collectionkotlin.common.ApiUrl
import com.youngmanster.collectionkotlin.common.AppConfig

class WeChatChinaNewsDefinitionPresenter : BasePresenter<IWeChatChinaNewsDefinitionView>() {
    fun requestChinaNews(context: Activity, page: Int, num: Int) {

        val filePath = AppConfig.getStorageDir(context) + "wechat/china"
        val fileName = "limttime.t"

        val requestBuilder=
            RequestBuilder(object : RxObservableListener<HttpResult<List<WeChatNews>>>(){
                override fun onNext(result: HttpResult<List<WeChatNews>>) {
                    mView?.refreshUI(result.result)
                }
            })

        requestBuilder
            .setUrl(ApiUrl.URL_WETCHAT_FEATURED)
            .setTransformClass(WeChatNews().javaClass)
            .setParam("page",page)
            .setParam("type","video")
            .setParam("count",num)
            .setFilePathAndFileName(filePath,fileName)
            .setHttpTypeAndReqType(RequestBuilder.HttpType.DEFAULT_GET, RequestBuilder.ReqType.DISK_CACHE_LIST_LIMIT_TIME)

        rxManager.addObserver(DataManager.DataForHttp.httpRequest(requestBuilder))
    }

}