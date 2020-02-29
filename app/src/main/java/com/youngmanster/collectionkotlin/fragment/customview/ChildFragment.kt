package com.youngmanster.collectionkotlin.fragment.customview

import android.os.Bundle
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_child.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class ChildFragment :BaseFragment<BasePresenter<*>>(){

    companion object{
        fun newInstance(str: String): ChildFragment {
            val childFragment = ChildFragment()
            val bundle = Bundle()
            bundle.putString("str", str)
            childFragment.arguments = bundle
            return childFragment
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_child
    }

    override fun init() {
        val bundle = arguments
        val str = bundle?.getString("str")
        childTv.text = str
    }

    override fun requestData() {
    }

}