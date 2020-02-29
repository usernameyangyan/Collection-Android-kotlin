package com.youngmanster.collectionkotlin.mvp.presenter

import android.content.Context
import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collection_kotlin.network.RequestBuilder
import com.youngmanster.collection_kotlin.network.rx.RxObservableListener
import com.youngmanster.collectionkotlin.bean.HttpResult
import com.youngmanster.collectionkotlin.bean.WeChatNews
import com.youngmanster.collectionkotlin.bean.WeChatNewsResult
import com.youngmanster.collectionkotlin.common.ApiUrl
import com.youngmanster.collectionkotlin.common.AppConfig
import com.youngmanster.collectionkotlin.mvp.contract.WeChatWorldNewsContract

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */
class WeChatWorldNewsDefinitionPresenter :WeChatWorldNewsContract.Presenter(){
    override fun requestWorldNews(context:Context,page: Int, num: Int) {
        val filePath = AppConfig.getStorageDir(context) + "wechat/world"
        val fileName = "$page.t"

        val requestBuilder=RequestBuilder(object :RxObservableListener<HttpResult<List<WeChatNews>>>(mView){
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
            .setHttpTypeAndReqType(RequestBuilder.HttpType.DEFAULT_GET,RequestBuilder.ReqType.DISK_CACHE_NO_NETWORK_LIST)

        rxManager.addObserver(DataManager.DataForHttp.httpRequest(requestBuilder))
    }

}