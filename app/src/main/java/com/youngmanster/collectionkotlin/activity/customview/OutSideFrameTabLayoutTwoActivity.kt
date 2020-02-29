package com.youngmanster.collectionkotlin.activity.customview

import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.customview.CustomTabPagerAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.customview.ChildFragment
import kotlinx.android.synthetic.main.activity_custom_outsideframetablayout_two.*
import java.util.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class OutSideFrameTabLayoutTwoActivity :BaseActivity<BasePresenter<*>>(){

    private val fragmentList = ArrayList<Fragment>()
    private val strList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_custom_outsideframetablayout_two
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.tab_Indicator_title))

        initData()

        val indexPagerAdapter = CustomTabPagerAdapter(supportFragmentManager, fragmentList,strList)
        viewPager.adapter = indexPagerAdapter
        customTabView.setupWithViewPager(viewPager)
    }

    private fun initData() {
        strList.add("精选")
        strList.add("爱看")
        strList.add("电视剧")
        for (i in strList.indices) {
            val fragment = ChildFragment.newInstance(strList[i])
            fragmentList.add(fragment)
        }
    }

    override fun requestData() {
    }

}