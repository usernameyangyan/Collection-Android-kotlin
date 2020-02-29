package com.youngmanster.collectionkotlin.activity.mvp

import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.CollectionFragmentAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.mvp.FragmentWeChatFeaturedCommonClass
import com.youngmanster.collectionkotlin.fragment.mvp.FragmentWeChatFeaturedNoCommonClass
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentAddHeader
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentEmptyView
import kotlinx.android.synthetic.main.activity_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
class WeChatFeaturedActivity:BaseActivity<BasePresenter<*>>(){

    private val fragments = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_refresh
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_wechat_title))
        customTabView.tabIndicatorWidth= (DisplayUtils.getScreenWidthPixels(this)/2).toFloat()

        fragments.add(FragmentWeChatFeaturedCommonClass())
        fragments.add(FragmentWeChatFeaturedNoCommonClass())
        titleList.add(getString(R.string.use_common_result))
        titleList.add(getString(R.string.no_use_common_result))

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