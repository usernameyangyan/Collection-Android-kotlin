package com.youngmanster.collectionkotlin.activity.data.popup

import android.view.View
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_popupwindow.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class PopupWindowDemoActivity :BaseActivity<BasePresenter<*>>(),View.OnClickListener{


    override fun getLayoutId(): Int {
        return R.layout.activity_popupwindow
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.popup_title))

        status_btn1.setOnClickListener(this)
        status_btn2.setOnClickListener(this)
        status_btn3.setOnClickListener(this)
        status_btn4.setOnClickListener(this)
    }

    override fun requestData() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.status_btn1 ->{
                val popupSlideBottom = PopupSlideBottom(this)
                popupSlideBottom.showPopup()
            }

            R.id.status_btn2 ->{
                val popupSlideButton = PopupSlideButton(this)
                popupSlideButton.showPopupAsDropDown(status_btn2)
            }

            R.id.status_btn3 ->{
                val popupTip = PopupTip(this)
                popupTip.showPopup()
            }

            R.id.status_btn4 ->{
                startAc(PopupMenuActivity().javaClass)
            }
        }
    }

}