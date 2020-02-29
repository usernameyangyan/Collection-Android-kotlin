package com.youngmanster.collectionkotlin.base

import android.os.Bundle
import com.youngmanster.collection_kotlin.base.baseview.IBaseFragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

abstract class BaseFragment<T:BasePresenter<*>>:IBaseFragment<T>(){

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
