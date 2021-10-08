package com.youngmanster.collection_kotlin.mvp
import android.content.Intent
import android.os.Bundle
import com.youngmanster.collection_kotlin.base.baseview.ICommonActivity

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

@Suppress("UNCHECKED_CAST")
abstract class IBaseActivity<T : BasePresenter<*>> : ICommonActivity() {

    var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = ClassGetUtil.getClass(this, 0)
        mPresenter?.setV(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
    }


    fun <U> startAc(claz: Class<U>) {
        val intent = Intent()
        intent.setClass(this, claz)
        startActivity(intent)
    }

}