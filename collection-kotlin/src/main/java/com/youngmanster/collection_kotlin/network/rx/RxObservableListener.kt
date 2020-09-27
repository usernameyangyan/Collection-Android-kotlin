package com.youngmanster.collection_kotlin.network.rx

import android.text.TextUtils
import com.youngmanster.collection_kotlin.mvp.BaseView
import com.youngmanster.collection_kotlin.network.NetWorkCodeException
import java.lang.ref.WeakReference

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

abstract class RxObservableListener<T> : ObservableListener<T> {

    private var mErrorMsg: String? = null
    private var re: WeakReference<BaseView>?=null

    protected constructor(view: BaseView?) {
        if(view!=null){
            re=WeakReference(view)
        }
    }

    protected constructor()

    protected constructor(view: BaseView?, errorMsg: String) {
        this.mErrorMsg = errorMsg
        if(view!=null){
            re=WeakReference(view)
        }
    }

    override fun onDownloadProgress(total: Long, currentLength:Long,progress: Float) {
    }

    override fun onUploadProgress(total: Long, progress: Float) {
    }


    override fun onError(e: NetWorkCodeException.ResponseThrowable) {

        if (re == null) {
            return
        }

        var view: BaseView? = re!!.get()
        if(view!=null){
            if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
                view.onError(mErrorMsg!!)
            } else {
                view.onError(e.errorMessage!!)
            }
        }

    }
}
