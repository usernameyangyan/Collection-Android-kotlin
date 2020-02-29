package com.youngmanster.collectionkotlin.adapter.customview

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class CustomTabPagerAdapter:FragmentPagerAdapter{
    private var fragmentList: List<Fragment>?=null
    private var titleList: List<String>?=null

    @SuppressLint("WrongConstant")
    constructor(fm: FragmentManager, fragmentList:List<Fragment>, titleList: ArrayList<String>):super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
        this.fragmentList = fragmentList
        this.titleList=titleList
    }

    override fun getCount(): Int {
        return fragmentList?.size!!
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList?.get(position)!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList?.get(position)
    }
}