package com.youngmanster.collectionkotlin.base

import android.os.Bundle
import android.view.View
import com.youngmanster.collection_kotlin.base.baseview.IBaseActivity
import com.youngmanster.collectionkotlin.R

import com.youngmanster.collection_kotlin.mvp.BasePresenter

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

abstract class BaseActivity<T : BasePresenter<*>> : IBaseActivity<T>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defineActionBarConfig
            .setBarBackground(R.color.colorPrimaryDark)
            .setTitleColor(this,R.color.white)
            .setBackIcon(R.mipmap.ic_back_btn)
            .setBackClick(View.OnClickListener {
                onBackPressed()
            })

    }

}