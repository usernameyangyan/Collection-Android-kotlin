package com.youngmanster.collectionkotlin.mvp.presenter

import android.content.Context
import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collectionkotlin.mvp.view.IDownloadFileView
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.network.RequestBuilder
import com.youngmanster.collection_kotlin.network.download.DownloadInfo
import com.youngmanster.collection_kotlin.network.rx.RxObservableListener
import com.youngmanster.collection_kotlin.utils.LogUtils
import com.youngmanster.collectionkotlin.common.AppConfig

class DownloadFilePresenter : BasePresenter<IDownloadFileView>() {
    fun downloadFile(context: Context) {
        val resultRequestBuilder = RequestBuilder(object :
            RxObservableListener<DownloadInfo>(mView) {
            override fun onNext(result: DownloadInfo) {
            }

            override fun onDownloadProgress(total: Long, currentLength: Long, progress: Float) {
                super.onDownloadProgress(total, currentLength, progress)

                LogUtils.info("100000","内容为："+progress)
            }
        })

        resultRequestBuilder.setUrl("http://acj6.0098118.com/pc6_soure/2020-8-20/8e52be6ad1a369aDOC1IU99ueAbq6.apk")
            .setSaveDownloadFilePathAndFileName(AppConfig.getStorageDir(context),"1.apk")
            .setTransformClass(DownloadInfo::class.java)
            .setHttpTypeAndReqType(RequestBuilder.HttpType.DOWNLOAD_FILE_GET, RequestBuilder.ReqType.DOWNLOAD_FILE_MODEL)
            .isOpenBreakpointDownloadOrUpload = true
        rxManager.addObserver(DataManager.DataForHttp.httpRequest(resultRequestBuilder))
    }
}