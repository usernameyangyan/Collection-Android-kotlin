package com.youngmanster.collectionkotlin.activity.customview

import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class WrapLinearLayoutActivity :BaseActivity<BasePresenter<*>>(){
    override fun getLayoutId(): Int {
        return R.layout.activity_wrap_linear
    }

    override fun init() {
        defineActionBarConfig.setTitle("AutoLineLayout")
    }

    override fun requestData() {
    }

}