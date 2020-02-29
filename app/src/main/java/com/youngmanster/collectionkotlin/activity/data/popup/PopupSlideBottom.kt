package com.youngmanster.collectionkotlin.activity.data.popup

import android.content.Context
import com.youngmanster.collection_kotlin.base.dialog.BasePopupWindow
import com.youngmanster.collectionkotlin.R

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class PopupSlideBottom(context: Context) : BasePopupWindow(context) {
    override fun getPopupLayoutRes(): Int {
        return R.layout.popup_slide_bottom
    }

    override fun getPopupAnimationStyleRes(): Int {
        return R.style.animation_bottom
    }

}