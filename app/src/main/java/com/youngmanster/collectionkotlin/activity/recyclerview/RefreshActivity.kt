package com.youngmanster.collectionkotlin.activity.recyclerview

import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.CollectionFragmentAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentDefaultRefreshAndLoading
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentDefinitionRefreshAndLoading
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentGoogleRefreshAndLoading
import kotlinx.android.synthetic.main.activity_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class RefreshActivity : BaseActivity<BasePresenter<*>>() {
    private val fragments = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.fragment_refresh_title))

        fragments.add(FragmentDefaultRefreshAndLoading())
        fragments.add(FragmentDefinitionRefreshAndLoading())
        fragments.add(FragmentGoogleRefreshAndLoading())

        titleList.add("默认")
        titleList.add("自定义")
        titleList.add("Google刷新")

        collectionVp.adapter =
            CollectionFragmentAdapter(
                supportFragmentManager,
                fragments,
                titleList
            )
        collectionVp.offscreenPageLimit = titleList.size
        customTabView.setupWithViewPager(collectionVp)
    }

    override fun requestData() {
    }
}