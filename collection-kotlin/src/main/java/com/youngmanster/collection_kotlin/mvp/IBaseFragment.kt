package com.youngmanster.collection_kotlin.mvp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.base.baseview.ICommonFragment


/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

abstract class IBaseFragment<T: BasePresenter<*>>: ICommonFragment(){

    var mPresenter: T? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = ClassGetUtil.getClass(this, 0)
        mPresenter?.setV(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.onDestroy()
    }


}