package com.youngmanster.collectionkotlin.activity.recyclerview

import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.CollectionFragmentAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentAddHeader
import com.youngmanster.collectionkotlin.fragment.recyclerview.FragmentEmptyView
import kotlinx.android.synthetic.main.activity_refresh.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class HeaderAndEmptyViewActivity: BaseActivity<BasePresenter<*>>(){

    private val fragments = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_refresh
    }

    override fun init() {
       defineActionBarConfig.setTitle(getString(R.string.fragment_header_title))

        customTabView.tabIndicatorWidth= (DisplayUtils.getScreenWidthPixels(this)/2).toFloat()

        fragments.add(FragmentAddHeader())
        fragments.add(FragmentEmptyView())
        titleList.add(getString(R.string.add_header))
        titleList.add(getString(R.string.set_empty))


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