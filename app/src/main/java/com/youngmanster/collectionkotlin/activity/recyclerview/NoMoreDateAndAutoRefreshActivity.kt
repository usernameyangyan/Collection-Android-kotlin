package com.youngmanster.collectionkotlin.activity.recyclerview

import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.CollectionFragmentAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentAutoRefresh
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentNoMoreData
import kotlinx.android.synthetic.main.activity_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class NoMoreDateAndAutoRefreshActivity:BaseActivity<BasePresenter<*>>(){

    private val fragments = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.fragment_auto_title))

        customTabView.tabIndicatorWidth= (DisplayUtils.getScreenWidthPixels()/2).toFloat()

        fragments.add(FragmentNoMoreData())
        fragments.add(FragmentAutoRefresh())
        titleList.add(getString(R.string.no_more_data))
        titleList.add(getString(R.string.auto_refresh))


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