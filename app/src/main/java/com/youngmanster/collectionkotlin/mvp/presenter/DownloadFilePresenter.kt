package com.youngmanster.collectionkotlin.mvp.presenter

import android.app.Activity
import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.network.RequestBuilder
import com.youngmanster.collection_kotlin.network.download.DownloadInfo
import com.youngmanster.collection_kotlin.network.rx.RxObservableListener
import com.youngmanster.collectionkotlin.common.AppConfig
import com.youngmanster.collectionkotlin.mvp.view.IDownloadFileView
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody

class DownloadFilePresenter : BasePresenter<IDownloadFileView>() {

    private var disposableObserver: DisposableObserver<ResponseBody>?=null

    fun downloadFile(context: Activity) {
        val resultRequestBuilder = RequestBuilder(object :
            RxObservableListener<DownloadInfo>() {
            override fun onNext(result: DownloadInfo) {
            }

            override fun onDownloadProgress(total: Long, currentLength: Long, progress: Float) {
                super.onDownloadProgress(total, currentLength, progress)
                mView?.updateProgress(progress)
            }

        })

        resultRequestBuilder.setUrl("https://dev.indoormap.huatugz.com/api/mapdata/appVersion/app/b6ee010c8ccb64648a3466e812173e88")
            .setSaveDownloadFilePathAndFileName(AppConfig.getStorageDir(context),"1.apk")
            .setTransformClass(DownloadInfo::class.java)
            .setHttpTypeAndReqType(RequestBuilder.HttpType.DOWNLOAD_FILE_GET, RequestBuilder.ReqType.DOWNLOAD_FILE_MODEL)
            .isOpenBreakpointDownloadOrUpload = true
        disposableObserver=DataManager.DataForHttp.httpRequest(resultRequestBuilder)
        rxManager.addObserver(disposableObserver)
    }

    fun stopDownload(){
        disposableObserver?.dispose()
    }
}