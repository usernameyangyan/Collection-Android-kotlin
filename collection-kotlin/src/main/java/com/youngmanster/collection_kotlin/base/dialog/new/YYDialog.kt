package com.youngmanster.collection_kotlin.base.dialog.new

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.youngmanster.collection_kotlin.R
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collection_kotlin.utils.LogUtils

/**
 * Created by yangy
 *2020/12/14
 *Describe:
 */
class YYDialog:YYBaseDialog,IDialog{
    private var context: Context? = null
    private var controller: YYDialogController? = null
    private var buildListener: IDialog.OnBuildListener? = null
    private var dismissListener: IDialog.OnDismissListener? = null
    companion object{
        private var FTag = "dialogTag"
    }


    constructor(){
        controller = YYDialogController(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context=context
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.context=activity
    }

    override fun getLayoutRes(): Int {
        return controller!!.getLayoutRes()
    }

    override fun getDialogView(): View? {
        return controller!!.getDialogView()
    }

    override fun getDialogWidth(): Int {
        LogUtils.info("100000","内容为："+controller!!.getDialogWidth())
        return controller!!.getDialogWidth()
    }

    override fun getDialogHeight(): Int {
        return controller!!.getDialogHeight()
    }

    override fun isCancelableOutside(): Boolean {
        return controller!!.isCancelableOutside()
    }

    override fun isCancelable(): Boolean {
        return controller!!.isCancelable()
    }

    override fun getDimAmount(): Float {
        return controller!!.getDimAmount()
    }

    override fun getGravity(): Int {
        return controller!!.getGravity()
    }

    override fun getAnimRes(): Int {
        return controller!!.getAnimRes()
    }

    override fun getContext(): Context? {
        return context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (controller == null) {
            controller = YYDialogController(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //设置默认Dialog的View布局
        controller!!.setChildView(view)
        //回调给调用者，用来设置子View及点击事件等
        if (buildListener != null && getBaseView() != null) {
            buildListener!!.onBuildChildView(this, getBaseView(), getLayoutRes())
        }
    }


    /**
     * 解决 Can not perform this action after onSaveInstanceState问题
     *
     * @param manager FragmentManager
     * @param tag     tag
     */
    fun showAllowingLoss(manager: FragmentManager, tag: String?) {
        try {
            val cls: Class<*> = DialogFragment::class.java
            val mDismissed = cls.getDeclaredField("mDismissed")
            mDismissed.isAccessible = true
            mDismissed[this] = false
            val mShownByMe = cls.getDeclaredField("mShownByMe")
            mShownByMe.isAccessible = true
            mShownByMe[this] = true
        } catch (e: Exception) {
            //调系统的show()方法
            show(manager, tag)
            return
        }
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun dismiss() {
        //防止横竖屏切换时 getFragmentManager置空引起的问题：
        //Attempt to invoke virtual method 'android.app.FragmentTransaction
        //android.app.FragmentManager.beginTransaction()' on a null object reference
        if (fragmentManager == null) return
        super.dismissAllowingStateLoss()
    }

    override fun onDestroy() {
        //监听dialog的dismiss
        if (dismissListener != null) {
            dismissListener!!.onDismiss(this)
        }
        if (controller != null) {
            controller = null
        }
        super.onDestroy()
    }

    class Builder(context: Context) {
        private val params: YYDialogController.YYParams
        private var buildListener: IDialog.OnBuildListener? = null
        private var dismissListener: IDialog.OnDismissListener? = null

        /**
         * 设置DialogView
         *
         * @param layoutRes 布局文件
         * @return Builder
         */
        fun setDialogView(@LayoutRes layoutRes: Int): Builder {
            params.layoutRes = layoutRes
            return this
        }

        /**
         * 设置DialogView
         *
         * @param dialogView View
         * @return Builder
         */
        fun setDialogView(dialogView: View?): Builder {
            params.dialogView = dialogView
            return this
        }

        /**
         * 设置屏幕宽度百分比
         *
         * @param percentage 0.0f~1.0f
         * @return Builder
         */
        fun setScreenWidthP(percentage: Float): Builder {
            params.dialogWidth = (DisplayUtils.getScreenWidthPixels(params.context!!) * percentage).toInt()
            return this
        }

        /**
         * 设置屏幕高度百分比
         *
         * @param percentage 0.0f~1.0f
         * @return Builder
         */
        fun setScreenHeightP(percentage: Float): Builder {
            params.dialogHeight = (DisplayUtils.getScreenHeightPixels(params.context!!) * percentage).toInt()
            return this
        }

        /**
         * 设置Dialog的宽度
         *
         * @param width 宽度
         * @return Builder
         */
        fun setWidth(width: Int): Builder {
            params.dialogWidth = width
            return this
        }

        /**
         * 设置Dialog的高度
         *
         * @param height 高度
         * @return Builder
         */
        fun setHeight(height: Int): Builder {
            params.dialogHeight = height
            return this
        }

        /**
         * 设置背景色色值
         *
         * @param percentage 0.0f~1.0f 1.0f为完全不透明
         * @return Builder
         */
        fun setWindowBackgroundP(percentage: Float): Builder {
            params.dimAmount = percentage
            return this
        }

        /**
         * 设置Gravity
         *
         * @param gravity Gravity
         * @return Builder
         */
        fun setGravity(gravity: Int): Builder {
            params.gravity = gravity
            return this
        }

        /**
         * 设置dialog外点击是否可以让dialog消失
         *
         * @param cancelableOutSide true 则在dialog屏幕外点击可以使dialog消失
         * @return Builder
         */
        fun setCancelableOutSide(cancelableOutSide: Boolean): Builder {
            params.isCancelableOutside = cancelableOutSide
            return this
        }

        /**
         * 设置是否屏蔽物理返回键
         *
         * @param cancelable true 点击物理返回键可以让dialog消失；反之不消失
         * @return Builder
         */
        fun setCancelable(cancelable: Boolean): Builder {
            params.cancelable = cancelable
            return this
        }

        /**
         * 构建子View的listener
         *
         * @param listener IDialog.OnBuildListener
         * @return Builder
         */
        fun setBuildChildListener(listener: IDialog.OnBuildListener?): Builder {
            buildListener = listener
            return this
        }

        /**
         * 监听dialog的dismiss
         *
         * @param dismissListener IDialog.OnDismissListener
         * @return Builder
         */
        fun setOnDismissListener(dismissListener: IDialog.OnDismissListener?): Builder {
            this.dismissListener = dismissListener
            return this
        }

        /**
         * 设置dialog的动画效果
         *
         * @param animStyle 动画资源文件
         * @return Builder
         */
        fun setAnimStyle(animStyle: Int): Builder {
            params.animRes = animStyle
            return this
        }

        /**
         * 设置默认右侧点击按钮
         *
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        fun setPositiveButton(onclickListener: IDialog.OnClickListener?): Builder {
            return setPositiveButton("确定", onclickListener)
        }

        /**
         * 设置默认右侧点击按钮及文字
         *
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        fun setPositiveButton(btnStr: String?, onclickListener: IDialog.OnClickListener?): Builder {
            params.positiveBtnListener = onclickListener
            params.positiveStr = btnStr
            params.showBtnRight = true
            return this
        }

        /**
         * 设置左侧按钮
         *
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        fun setNegativeButton(onclickListener: IDialog.OnClickListener?): Builder {
            return setNegativeButton("取消", onclickListener)
        }

        /**
         * 设置左侧文字及按钮
         *
         * @param btnStr          文字
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        fun setNegativeButton(btnStr: String?, onclickListener: IDialog.OnClickListener?): Builder {
            params.negativeBtnListener = onclickListener
            params.negativeStr = btnStr
            params.showBtnLeft = true
            return this
        }

        /**
         * 设置默认dialog的title
         *
         * @param title 标题
         * @return Builder
         */
        fun setTitle(title: String?): Builder {
            params.titleStr = title
            return this
        }

        fun setDefaultLoading(isDefaultLoading:Boolean): Builder {
            params.isDefaultLoading = isDefaultLoading
            return this
        }

        /**
         * 设置默认dialog的内容
         *
         * @param content 内容
         * @return Builder
         */
        fun setContent(content: String?): Builder {
            params.contentStr = content
            return this
        }

        private fun create(): YYDialog {
            val dialog =
                YYDialog()
            params.apply(dialog.controller!!)
            dialog.buildListener = buildListener
            dialog.dismissListener = dismissListener
            return dialog
        }

        /**
         * 展示Dialog
         *
         * @return SYDialog
         */
        fun show():YYDialog {
            if (params.layoutRes <= 0 && params.dialogView == null) {
                //如果没有设置布局 提供默认设置
                setDefaultOption()
            }
            val dialog: YYDialog = create()
            //判空
            if (params.context == null) return dialog
            if (params.context is Activity) {
                val activity = params.context as Activity
                //如果Activity正在关闭或者已经销毁 直接返回
                val isRefuse =
                    activity.isFinishing || activity.isDestroyed
                if (isRefuse) return dialog
            }
            removePreDialog()
            dialog.showAllowingLoss(
                params.fragmentManager!!,
                FTag
            )
            return dialog
        }

        /**
         * 设置默认Dialog的配置
         */
        private fun setDefaultOption() {
            if(params.isDefaultLoading){
                params.layoutRes = R.layout.collection_library_dialog_progress
            }else{
                params.layoutRes = R.layout.lib_ui_layout_dialog_default
            }

        }

        /**
         * 移除之前的dialog
         */
        private fun removePreDialog() {
            val ft: FragmentTransaction = params.fragmentManager!!.beginTransaction()
            val prev: Fragment? =params.fragmentManager?.findFragmentByTag(FTag)
            if (prev != null) {
                ft.remove(prev)
            }
            ft.commitAllowingStateLoss()
        }

        init {
            require(context is Activity) { "Context must be Activity" }
            params = YYDialogController.YYParams()
            params.context = context
            params.fragmentManager = (context as AppCompatActivity).supportFragmentManager

        }
    }


}