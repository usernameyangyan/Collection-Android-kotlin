package com.youngmanster.collectionkotlin.activity.customview

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.customview.CustomTabPagerAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.fragment.customview.ChildFragment
import kotlinx.android.synthetic.main.activity_custom_tablayout_one.*
import java.util.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class CommonLayoutStyleSevenActivity :BaseActivity<BasePresenter<*>>(){

    private val fragmentList = ArrayList<Fragment>()
    private val customViews = ArrayList<View>()
    private val strList = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_custom_tablayout_seven
    }

    override fun init() {
       defineActionBarConfig.setTitle(getString(R.string.tab_Indicator_title))

        initData()

        val indexPagerAdapter = CustomTabPagerAdapter(supportFragmentManager, fragmentList,strList)
        viewPager.adapter = indexPagerAdapter
        customTabView.setCustomViews(customViews)
        customTabView.setupWithViewPager(viewPager)
    }

    override fun requestData() {
    }

    private fun initData() {
        strList.add("精选")
        strList.add("爱看")
        strList.add("电视剧")
        strList.add("电影")

        for (i in 0 until strList.size) {
            val fragment = ChildFragment.newInstance(strList[i])
            fragmentList.add(fragment)
            val view = LayoutInflater.from(this).inflate(R.layout.custom_tab_view, null)
            customViews.add(view)
        }

    }


}