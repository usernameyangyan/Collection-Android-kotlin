package com.youngmanster.collectionkotlin.activity.data.popup

import android.view.View
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_popup_list.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class PopupMenuActivity :BaseActivity<BasePresenter<*>>(),View.OnClickListener{
    override fun getLayoutId(): Int {
        return R.layout.activity_popup_list
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.popup_title))

        clickLl.setOnClickListener(this)
    }

    override fun requestData() {
    }

    override fun onClick(v: View?) {
        val popupMenuList = PopupMenuList(this)
        popupMenuList.showPopupAsDropDown(clickLl)
    }

}