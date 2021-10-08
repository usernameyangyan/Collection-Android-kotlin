package com.youngmanster.collectionkotlin.activity.mvp

import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.CollectionFragmentAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.mvp.FragmentChinaNewsDefinition
import com.youngmanster.collectionkotlin.fragment.mvp.FragmentWeChatFeaturedCommonClass
import com.youngmanster.collectionkotlin.fragment.mvp.FragmentWeChatFeaturedNoCommonClass
import com.youngmanster.collectionkotlin.fragment.mvp.FragmentWorldNewsDefinition
import kotlinx.android.synthetic.main.activity_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class WeChatNewsDefinitionActivity:BaseActivity<BasePresenter<*>>(){


    private val fragments = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle("自定义磁盘缓存")
        customTabView.tabIndicatorWidth= (DisplayUtils.getScreenWidthPixels()/2).toFloat()

        fragments.add(FragmentWorldNewsDefinition())
        fragments.add(FragmentChinaNewsDefinition())
        titleList.add(getString(R.string.fen_ye_cache))
        titleList.add(getString(R.string.limit_time_cache))

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