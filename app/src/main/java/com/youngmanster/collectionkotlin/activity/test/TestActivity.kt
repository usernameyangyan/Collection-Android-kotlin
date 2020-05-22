package com.youngmanster.collectionkotlin.activity.test

import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.data.FragmentSharePreference

/**
 * Created by yangy
 *2020/5/21
 *Describe:测试使用类
 */
class TestActivity:BaseActivity<BasePresenter<*>>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun init() {
       startFragment(FragmentSharePreference::class.java)
    }

    override fun requestData() {

    }

    override fun fragmentLayoutId(): Int {
        return R.id.root
    }
}