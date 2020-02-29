package com.youngmanster.collection_kotlin.network.rx

import android.text.TextUtils
import com.youngmanster.collection_kotlin.mvp.BaseView
import com.youngmanster.collection_kotlin.network.NetWorkCodeException
import com.youngmanster.collection_kotlin.utils.LogUtils

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

abstract class RxObservableListener<T> : ObservableListener<T> {


    private var mView: BaseView? = null
    private var mErrorMsg: String? = null

    protected constructor(view: BaseView?) {
        this.mView = view
    }

    protected constructor() {}

    protected constructor(view: BaseView, errorMsg: String) {
        this.mView = view
        this.mErrorMsg = errorMsg
    }

    override fun onComplete() {

    }

    override fun onError(e: NetWorkCodeException.ResponseThrowable) {

        if (mView == null) {
            return
        }
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView!!.onError(mErrorMsg!!)
        } else {
            mView!!.onError(e.errorMessage!!)
        }
    }
}
