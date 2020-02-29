package com.youngmanster.collectionkotlin.activity.data.popup

import android.content.Context
import android.view.ViewGroup
import com.youngmanster.collection_kotlin.base.dialog.BasePopupWindow
import com.youngmanster.collectionkotlin.R

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class PopupSlideButton : BasePopupWindow {

    constructor(context: Context):super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT){
        setShowMaskView(false)
    }

    override fun getPopupLayoutRes(): Int {
        return R.layout.popup_slide_button
    }

    override fun getPopupAnimationStyleRes(): Int {
        return 0
    }

}