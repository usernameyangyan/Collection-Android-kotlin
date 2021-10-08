package com.youngmanster.collection_kotlin.mvvm.viewmode

import android.app.Application
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.youngmanster.collection_kotlin.mvp.ClassGetUtil
import com.youngmanster.collection_kotlin.mvvm.liveData.SingleLiveEvent
import com.youngmanster.collection_kotlin.mvvm.mode.BaseModel
import com.youngmanster.collection_kotlin.network.rx.RxManager
import com.youngmanster.collection_kotlin.utils.LogUtils
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.*

/**
 * Created by yangy
 *2021/3/29
 *Describe:
 */
open class BaseViewModel<M : BaseModel>(@NonNull application: Application) : IBaseViewModel,
    AndroidViewModel(application), Consumer<Disposable> {

    var model: M? = null
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    var rxManager: RxManager? =null
    private var ucLiveData: UIChangeLiveData<*>? = null


    init {
        rxManager= RxManager()
        model = ClassGetUtil.getClass(this, 0)
    }




    open fun getUcLiveData(): UIChangeLiveData<*>? {
        if (ucLiveData == null) {
            ucLiveData = UIChangeLiveData<Any>()
        }
        return ucLiveData
    }


    class UIChangeLiveData<Any>:SingleLiveEvent<Any>(){
        private var startActivityEvent: SingleLiveEvent<Map<String, kotlin.Any>>? = null
        private var finishEvent: SingleLiveEvent<Void>? = null
        private var onBackPressedEvent: SingleLiveEvent<Void>? = null

        fun getStartActivityEvent(): SingleLiveEvent<Map<String, kotlin.Any>> {
            return createLiveData(startActivityEvent)
        }

        fun getFinishEvent(): SingleLiveEvent<Void> {
            return createLiveData(finishEvent)
        }

        fun getOnBackPressedEvent(): SingleLiveEvent<Void>? {
            return createLiveData(onBackPressedEvent)
        }

        private fun <T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
            if (liveData == null) {
                return SingleLiveEvent()
            }
            return liveData
        }

    }


    object ParameterField {
        var CLASS = "CLASS"
        var CANONICAL_NAME = "CANONICAL_NAME"
        var BUNDLE = "BUNDLE"
    }


    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    open fun startActivity(clz: Class<*>) {
        startActivity(clz, null)
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    open fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val params = HashMap<String, Any>()
        params[ParameterField.CLASS] = clz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        ucLiveData?.getStartActivityEvent()?.postValue(params)
    }
    /**
     * 关闭界面
     */
    open fun finish() {
        ucLiveData?.getFinishEvent()?.call()
    }

    /**
     * 返回上一层
     */
    open fun onBackPressed() {
        ucLiveData?.getOnBackPressedEvent()?.call()
    }


    override fun onCleared() {
        super.onCleared()
        model?.onCleared()
        //ViewModel销毁时会执行，同时取消所有异步任务
        rxManager?.clear()
    }

    /**
     * Consume the given value.
     * @param t the value
     * @throws Exception on error
     */
    override fun accept(t: Disposable?) {
        rxManager?.addObserver(t)
    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

}