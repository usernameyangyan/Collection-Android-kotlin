package com.youngmanster.collectionkotlin.activity.fragment

import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.LogUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.skip.SkipFragment

/**
 * Created by yangy
 *2020/5/21
 *Describe:
 */
class FragmentActivity:BaseActivity<BasePresenter<*>>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_fragment_skip
    }

    override fun init() {
        defineActionBarConfig.setTitle("Fragment页面跳转")
        startFragment(SkipFragment::class.java)
    }

    override fun requestData() {
    }


    override fun fragmentLayoutId(): Int {
        return R.id.root
    }
}