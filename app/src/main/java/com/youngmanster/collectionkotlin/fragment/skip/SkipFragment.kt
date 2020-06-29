package com.youngmanster.collectionkotlin.fragment.skip
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.LogUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_skip.*

/**
 * Created by yangy
 *2020/5/21
 *Describe:
 */
class SkipFragment:BaseFragment<BasePresenter<*>>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_skip
    }

    override fun init() {
        btn1.setOnClickListener{
            startFragment(SkipFragment2::class.java)
        }

        btn2.setOnClickListener{
            startFragment(SkipFragment2::class.java,true)
        }
    }

    override fun requestData() {
    }
}