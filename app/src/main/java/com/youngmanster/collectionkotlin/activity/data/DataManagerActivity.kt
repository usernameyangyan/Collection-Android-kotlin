package com.youngmanster.collectionkotlin.activity.data

import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.CollectionFragmentAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.data.FragmentSharePreference
import com.youngmanster.collectionkotlin.fragment.data.FragmentSqlite
import com.youngmanster.collectionkotlin.fragment.mvp.FragmentWeChatFeaturedCommonClass
import kotlinx.android.synthetic.main.activity_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */
class DataManagerActivity:BaseActivity<BasePresenter<*>>(){

    private val fragments = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.data_manager_title))


        fragments.add(FragmentWeChatFeaturedCommonClass())
        fragments.add(FragmentSharePreference())
        fragments.add(FragmentSqlite())
        titleList.add(getString(R.string.retrofit_manager))
        titleList.add(getString(R.string.sharePreference_manager))
        titleList.add(getString(R.string.sqlite_manager))

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