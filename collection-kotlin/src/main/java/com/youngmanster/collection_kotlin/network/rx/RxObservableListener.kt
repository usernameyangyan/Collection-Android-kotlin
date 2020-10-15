package com.youngmanster.collection_kotlin.network.rx
import com.youngmanster.collection_kotlin.network.NetWorkCodeException

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

abstract class RxObservableListener<T> : ObservableListener<T> {

    private var mErrorMsg: String? = null

    protected constructor(){
    }



    override fun onDownloadProgress(total: Long, currentLength:Long,progress: Float) {
    }

    override fun onUploadProgress(total: Long, progress: Float) {
    }


    override fun onError(e: NetWorkCodeException.ResponseThrowable) {
    }
}
