package com.youngmanster.collection_kotlin.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.youngmanster.collection_kotlin.utils.DisplayUtils

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

abstract class BaseDialog {

    var dismissListener: DismissListener?=null
    var alertDialog: AlertDialog? = null
    var builder: AlertDialog.Builder? = null
    var screenWidthPixels: Int = 0
    var space: Int = 0
    var height: Int = 0
    var context: Context
    var mainView: View? = null

    val isShowing: Boolean
        get() = alertDialog != null && alertDialog!!.isShowing

    constructor(context: Context) {
        this.context = context
    }



    constructor(context: Context, dismissListener: DismissListener) {
        this.context = context
        this.dismissListener = dismissListener
    }


    fun setContentView(layoutRes: Int) {
        if (layoutRes != 0) {
            this.mainView = LayoutInflater.from(context).inflate(layoutRes, null)
        } else {
            this.mainView = null
        }

        create()
    }

    fun setContentView(mainView: View?) {
        if (mainView != null) {
            this.mainView = mainView
        }
        create()
    }


    private fun create() {
        screenWidthPixels = DisplayUtils.getScreenWidthPixels(context)
        space = DisplayUtils.dip2px(context, 20f)
        builder = AlertDialog.Builder(context)
        if (mainView != null) {
            builder!!.setView(mainView)
        }

        onViewCreated()
        alertDialog = builder!!.create()

        alertDialog!!.setOnDismissListener {
            dismissListener?.onDismissListener()
        }
    }

    fun show() {
        alertDialog!!.show()
        val params = alertDialog!!.window?.attributes
        params?.width   = (screenWidthPixels - space * 2)
        params?.gravity  = Gravity.CENTER
        if (height > 0) {
            params?.height = height
        }
        alertDialog!!.window?.attributes = params
    }

    fun dismiss() {
        if (alertDialog != null) {
            alertDialog!!.dismiss()
        }
    }

    /**
     * 点击返回键和外部不可取消
     */
    fun setCancelable(b: Boolean) {
        if (alertDialog != null) {
            alertDialog!!.setCancelable(b)
        } else if (builder != null) {
            builder!!.setCancelable(b)
        }
    }

    /**
     * 点击返回键可以取消
     */
    fun setOnBackPressDialogCancel(isCancel: Boolean) {
        if (alertDialog != null) {
            alertDialog!!.setCanceledOnTouchOutside(isCancel)
        }
    }

    fun setDialogInterval(interval: Float) {
        space = DisplayUtils.dip2px(context, interval)
    }

    fun setDialogHeight(height: Float) {
        this.height = DisplayUtils.dip2px(context, height)
    }


    protected abstract fun onViewCreated()


    interface DismissListener {
        fun onDismissListener()
    }

}
