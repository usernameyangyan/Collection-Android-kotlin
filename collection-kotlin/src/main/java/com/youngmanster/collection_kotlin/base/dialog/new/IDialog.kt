package com.youngmanster.collection_kotlin.base.dialog.new

import android.content.Context
import android.view.View

/**
 * Created by yangy
 *2020/12/14
 *Describe:
 */
interface IDialog {

    /**
     * 弹窗消失
     */
    fun dismiss()

    /**
     * 构造dialog里的View
     */
    interface OnBuildListener {
        /**
         * @param dialog    IDialog
         * @param view      Dialog整体View
         * @param layoutRes Dialog的布局 如果没有传入 默认是0
         */
        fun onBuildChildView(dialog: IDialog?, view: View?, layoutRes: Int)
    }

    /***
     * 点击事件
     */
    interface OnClickListener {
        fun onClick(dialog: IDialog?)
    }

    /**
     * Dialog消失回调
     */
    interface OnDismissListener {
        /**
         * This method will be invoked when the dialog is dismissed.
         *
         * @param dialog the dialog that was dismissed will be passed into the
         * method
         */
        fun onDismiss(dialog: IDialog?)
    }

    fun getContext(): Context?

}