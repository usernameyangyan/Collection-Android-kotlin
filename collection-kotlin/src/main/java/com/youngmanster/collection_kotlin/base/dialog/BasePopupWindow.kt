package com.youngmanster.collection_kotlin.base.dialog

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.IBinder
import android.view.*
import android.widget.PopupWindow

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

abstract class BasePopupWindow : PopupWindow {
    var context: Context
    var popupView: View?=null
    private var isShowMaskView = false
    private var maskView: View? = null
    private var windowManager: WindowManager? = null
    private var popupWidth: Int = 0
    private var popupHeight: Int = 0
    private var touch_dismiss = true


    abstract fun getPopupLayoutRes():Int
    abstract fun getPopupAnimationStyleRes(): Int

    constructor(context: Context) {
        this.context = context
        initView(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    constructor(context: Context, w: Int, h: Int) {
        this.context = context
        initView(context, w, h)
    }

    private fun initView(context: Context, w: Int, h: Int) {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (getPopupLayoutRes() != 0) {
            popupView = LayoutInflater.from(context).inflate(getPopupLayoutRes(), null)
            this.contentView = popupView
            this.width = w
            this.height = h
            isFocusable = true
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable())
            if (getPopupAnimationStyleRes() != 0) {
                animationStyle = getPopupAnimationStyleRes()
            }


            //获取自身的长宽高
            popupView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            popupHeight = popupView!!.measuredHeight
            popupWidth = popupView!!.measuredWidth

            popupView!!.setOnClickListener {
                if(touch_dismiss){
                    dismiss()
                }

            }
        }

    }

    fun showPopup(gravity: Int) {
        val anchor = (context as Activity).findViewById<View>(android.R.id.content)
        if (isShowMaskView) {
            addMask(anchor.windowToken)
        }
        this.showAtLocation(anchor, gravity, 0, 0)
    }


    fun showPopup() {
        val anchor = (context as Activity).findViewById<View>(android.R.id.content)
        if (isShowMaskView) {
            addMask(anchor.windowToken)
        }
        this.showAtLocation(anchor, Gravity.CENTER, 0, 0)
    }

    fun showPopupAsDropDown(anchor: View) {
        if (isShowMaskView) {
            addMask(anchor.windowToken)
        }

        val rect = Rect()
        anchor.getGlobalVisibleRect(rect)
        val h = anchor.resources.displayMetrics.heightPixels - rect.bottom
        height = h

        this.showAsDropDown(anchor)
    }

    fun setShowMaskView(isShowMaskView: Boolean) {
        this.isShowMaskView = isShowMaskView
    }

    fun setTouchDismiss(touch_dismiss:Boolean){
        this.touch_dismiss=touch_dismiss
    }

    private fun addMask(token: IBinder) {
        val wl = WindowManager.LayoutParams()
        wl.width = WindowManager.LayoutParams.MATCH_PARENT
        wl.height = WindowManager.LayoutParams.MATCH_PARENT
        wl.format = PixelFormat.TRANSLUCENT//不设置这个弹出框的透明遮罩显示为黑色
        wl.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL//该Type描述的是形成的窗口的层级关系
        wl.token = token//获取当前Activity中的View中的token,来依附Activity
        maskView = View(context)
        maskView!!.setBackgroundColor(0x7f000000)
        maskView!!.fitsSystemWindows = false
        maskView!!.setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                removeMask()
                return@OnKeyListener true
            }
            false
        })
        /**
         * 通过WindowManager的addView方法创建View，产生出来的View根据WindowManager.LayoutParams属性不同，效果也就不同了。
         * 比如创建系统顶级窗口，实现悬浮窗口效果！
         */
        windowManager!!.addView(maskView, wl)
    }

    private fun removeMask() {
        if (null != maskView) {
            windowManager!!.removeViewImmediate(maskView)
            maskView = null
        }
    }

    override fun dismiss() {
        if (isShowMaskView) {
            removeMask()
        }
        super.dismiss()
    }
}
