package com.youngmanster.collection_kotlin.network.rx

import com.youngmanster.collection_kotlin.network.NetWorkCodeException

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

interface ObservableListener<T> {
    fun onNext(result: T)
    fun onError(e: NetWorkCodeException.ResponseThrowable)
    fun onDownloadProgress(total:Long,progress:Float)
    fun onUploadProgress(total: Long, progress: Float)
}