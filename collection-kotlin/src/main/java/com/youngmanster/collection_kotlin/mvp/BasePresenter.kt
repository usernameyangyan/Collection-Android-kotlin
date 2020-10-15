package com.youngmanster.collection_kotlin.mvp
import com.youngmanster.collection_kotlin.network.rx.RxManager
import com.youngmanster.collection_kotlin.utils.LogUtils

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<T>{
    var mView: T?=null
    var rxManager: RxManager= RxManager()

    fun setV(v: Any){
        this.mView=v as T
    }

    fun onDestroy(){
        this.mView=null
        rxManager.clear()
    }
}