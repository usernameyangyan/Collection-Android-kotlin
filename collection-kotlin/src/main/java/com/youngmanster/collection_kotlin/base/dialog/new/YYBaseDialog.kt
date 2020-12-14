package com.youngmanster.collection_kotlin.base.dialog.new

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.youngmanster.collection_kotlin.utils.LogUtils

/**
 * Created by yangy
 *2020/12/14
 *Describe:
 */
abstract class YYBaseDialog: DialogFragment() {
    private var mainView:View?=null

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        if (getLayoutRes() > 0) {
            //调用方通过xml获取view
            mainView = inflater.inflate(getLayoutRes(), container, false)
        } else if (getDialogView() != null) {
            //调用方直接传入view
            mainView = getDialogView()
        }
        return mainView!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dialog = dialog
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //如果isCancelable()是false 则会屏蔽物理返回键
            dialog.setCancelable(isCancelable)
            //如果isCancelableOutside()为false 点击屏幕外Dialog不会消失；反之会消失
            dialog.setCanceledOnTouchOutside(isCancelableOutside())
            //如果isCancelable()设置的是false 会屏蔽物理返回键
            dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN && !isCancelable })
        }
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val window = dialog.window ?: return
        //设置背景色透明
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //设置Dialog动画效果
        if (getAnimRes() > 0) {
            window.setWindowAnimations(getAnimRes())
        }
        val params = window.attributes
        //设置Dialog的Width
        if (getDialogWidth() > 0) {
            params.width = getDialogWidth()
        } else {
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
        }
        //设置Dialog的Height
        if (getDialogHeight() > 0) {
            params.height = getDialogHeight()
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        //设置屏幕透明度 0.0f~1.0f(完全透明~完全不透明)
        params.dimAmount = getDimAmount()
        params.gravity = getGravity()
        window.attributes = params
    }

    protected open fun getBaseView(): View? {
        return view
    }

    protected abstract fun getLayoutRes(): Int

    protected abstract fun getDialogView(): View?

    protected open fun isCancelableOutside(): Boolean {
        return true
    }

    protected open fun getDialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    protected open fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    open fun getDimAmount(): Float {
        return 0.2f
    }

    protected open fun getGravity(): Int {
        return Gravity.CENTER
    }

    protected open fun getAnimRes(): Int {
        return 0
    }

    private val point = Point()


    /**
     * 获取屏幕宽度
     *
     * @param activity Activity
     * @return ScreenWidth
     */
    open fun getScreenWidth(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        if (display != null) {
            display.getSize(point)
            return point.x
        }
        return 0
    }

    /**
     * 获取屏幕高度
     *
     * @param activity Activity
     * @return ScreenHeight
     */
    open fun getScreenHeight(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        if (display != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //api 大于17才有
                display.getRealSize(point)
            } else {
                display.getSize(point)
            }

            //需要减去statusBar的高度  不用考虑navigationBar Display已经自动减去了
            return point.y - getStatusBarHeight(activity)
        }
        return 0
    }

    open fun getStatusBarHeight(activity: Activity): Int {
        val resources = activity.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    open fun getNavigationBarHeight(activity: Activity): Int {
        val resources = activity.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    override fun onDestroy() {
        super.onDestroy()
        YYDialogsManager.getInstance().over()
    }
}