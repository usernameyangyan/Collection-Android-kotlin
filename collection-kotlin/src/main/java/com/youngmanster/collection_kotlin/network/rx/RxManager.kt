package com.youngmanster.collection_kotlin.network.rx

import com.youngmanster.collection_kotlin.utils.LogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class RxManager{
    private val compositeDisposable = CompositeDisposable()

    /**
     * 添加observer
     * @param observer
     */
    fun addObserver(observer: DisposableObserver<ResponseBody>?){
        if(observer != null){
            compositeDisposable.add(observer)
        }

    }

    /**
     * 添加observer
     * @param observer
     */
    fun addObserver(disposable: Disposable?){
        if(disposable != null){
            compositeDisposable.add(disposable)
        }

    }

    fun clear(){
        compositeDisposable.clear()
    }
}