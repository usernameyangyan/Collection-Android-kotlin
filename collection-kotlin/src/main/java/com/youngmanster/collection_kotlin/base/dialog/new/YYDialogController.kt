package com.youngmanster.collection_kotlin.base.dialog.new

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.youngmanster.collection_kotlin.R
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collection_kotlin.utils.LogUtils
import java.lang.ref.WeakReference

/**
 * Created by yangy
 *2020/12/14
 *Describe:
 */
class YYDialogController {
    private var layoutRes = 0
    private var dialogWidth = 0
    private var dialogHeight = 0
    private var dimAmount = 0.2f
    private var gravity = Gravity.CENTER
    private var isCancelableOutside = true
    private var cancelable = false
    private var animRes = 0
    private var dialogView: View? = null
    private var mPositiveButtonListener: IDialog.OnClickListener? = null
    private var mNegativeButtonListener: IDialog.OnClickListener? = null
    private var mDialog: WeakReference<IDialog>? = null
    private var titleStr: String? = null
    private var contentStr: String? = null
    private var positiveStr : String? = null
    private var negativeStr: String? = null
    private var showBtnLeft = false
    private var showBtnRight = false
    private var isDefaultLoading=false

    private var btn_ok: Button? = null
    private var btn_cancel:Button? = null
    private var tv_ProgressTip:TextView?=null


    constructor(dialog: IDialog){
        mDialog = WeakReference(dialog)
    }


    fun getAnimRes(): Int {
        return animRes
    }

    fun getLayoutRes(): Int {
        return layoutRes
    }

    fun setLayoutRes(layoutRes: Int) {
        this.layoutRes = layoutRes
    }

    fun getDialogWidth(): Int {

        return dialogWidth
    }

    fun getDialogHeight(): Int {
        return dialogHeight
    }

    fun getDimAmount(): Float {
        return dimAmount
    }

    fun getGravity(): Int {
        return gravity
    }

    fun isCancelableOutside(): Boolean {
        return isCancelableOutside
    }

    fun isCancelable(): Boolean {
        return cancelable
    }

    fun setDialogView(dialogView: View?) {
        this.dialogView = dialogView
    }

    fun getDialogView(): View? {
        return dialogView
    }

    fun setChildView(view: View?) {
        setDialogView(view)

        if(layoutRes==R.layout.collection_library_dialog_progress) {
            dealLoadingDialog()
        }else if(layoutRes==R.layout.lib_ui_layout_dialog_default){
            dealDefaultDialog(
                mPositiveButtonListener, mNegativeButtonListener, titleStr,
                contentStr, showBtnLeft, negativeStr, showBtnRight, positiveStr
            )
        }
    }

    private fun dealLoadingDialog(){
        tv_ProgressTip = dialogView!!.findViewById(R.id.tv_ProgressTip)
        tv_ProgressTip?.text=titleStr
    }
    private fun dealDefaultDialog(
        positiveBtnListener: IDialog.OnClickListener?,
        negativeBtnListener: IDialog.OnClickListener?,
        titleStr: String?,
        contentStr: String?,
        showBtnLeft: Boolean,
        negativeStr: String?,
        showBtnRight: Boolean,
        positiveStr: String?
    ) {
        if (dialogView == null) return
        mNegativeButtonListener = negativeBtnListener
        mPositiveButtonListener = positiveBtnListener
        btn_ok = dialogView!!.findViewById<View>(R.id.btn_ok) as Button
        btn_cancel = dialogView!!.findViewById<View>(R.id.btn_cancel) as Button
        if (showBtnRight && showBtnLeft) {
            //左右两个按钮都存在
            if (btn_ok != null) {
                btn_ok!!.visibility = View.VISIBLE
                btn_ok!!.text =
                    Html.fromHtml(if (TextUtils.isEmpty(positiveStr)) "确定" else positiveStr)
                btn_ok!!.setOnClickListener(mButtonHandler)
            }
            if (btn_cancel != null) {
                btn_cancel!!.visibility = View.VISIBLE
                btn_cancel!!.text =
                    Html.fromHtml(if (TextUtils.isEmpty(negativeStr)) "取消" else negativeStr)
                btn_cancel!!.setOnClickListener(mButtonHandler)
            }
        } else if (showBtnRight) {
            //只有右边的按钮
            if (btn_ok != null) {
                btn_ok!!.visibility = View.VISIBLE
                btn_ok!!.setBackgroundResource(R.drawable.lib_ui_selector_btn_border_bg)
                btn_ok!!.text =
                    Html.fromHtml(if (TextUtils.isEmpty(positiveStr)) "确定" else positiveStr)
                btn_ok!!.setOnClickListener(mButtonHandler)
            }
        } else if (showBtnLeft) {
            //只有左边的按钮
            if (btn_cancel != null) {
                btn_cancel!!.visibility = View.VISIBLE
                btn_cancel!!.setBackgroundResource(R.drawable.lib_ui_selector_btn_border_bg)
                btn_cancel!!.text =
                    Html.fromHtml(if (TextUtils.isEmpty(negativeStr)) "取消" else negativeStr)
                btn_cancel!!.setOnClickListener(mButtonHandler)
            }
        }
        val tv_title = dialogView!!.findViewById<View>(R.id.dialog_title) as TextView
        val tv_content = dialogView!!.findViewById<View>(R.id.dialog_content) as TextView
        tv_title.visibility = if (TextUtils.isEmpty(titleStr)) View.GONE else View.VISIBLE
        tv_title.text = Html.fromHtml(if (!TextUtils.isEmpty(titleStr)) titleStr else "Title")
        if (TextUtils.isEmpty(contentStr) && mDialog!!.get() != null && mDialog!!.get()!!
                .getContext() != null
        ) {
            tv_title.minHeight = DisplayUtils.dip2px(mDialog!!.get()!!.getContext()!!, 100f)
            tv_title.gravity = Gravity.CENTER
            tv_title.setPadding(0, 10, 0, 0)
        }
        tv_content.visibility = if (TextUtils.isEmpty(contentStr)) View.GONE else View.VISIBLE
        tv_content.text = contentStr
        tv_content.viewTreeObserver.addOnPreDrawListener(ViewTreeObserver.OnPreDrawListener {
            val lineCount = tv_content.lineCount
            if (lineCount >= 3) {
                //超过三行居左显示
                tv_content.gravity = Gravity.START
            } else {
                //默认居中
                tv_content.gravity = Gravity.CENTER_HORIZONTAL
                if (TextUtils.isEmpty(titleStr)) {
                    tv_content.setPadding(0, 50, 0, 50)
                }
            }
            if (TextUtils.isEmpty(titleStr)) {
                //没有title，只有content
                tv_content.textSize = 18f
                if (mDialog!!.get() == null || mDialog!!.get()!!
                        .getContext() == null || mDialog!!.get()!!
                        .getContext()!!.resources == null
                ) return@OnPreDrawListener true
                tv_content.setTextColor(ContextCompat.getColor(mDialog!!.get()!!.getContext()!!,R.color.c333333))
            }
            true
        })
    }

    private val mButtonHandler =
        View.OnClickListener { view ->
            if (view === btn_cancel) {
                if (mDialog!!.get() == null) return@OnClickListener
                if (mNegativeButtonListener != null) {
                    mNegativeButtonListener!!.onClick(mDialog!!.get())
                }
            } else if (view === btn_ok) {
                if (mDialog!!.get() == null) return@OnClickListener
                mPositiveButtonListener?.onClick(mDialog!!.get())
            }
        }

    class YYParams {
        var fragmentManager: FragmentManager? = null
        var layoutRes = 0
        var dialogWidth =  0
        var dialogHeight = 0
        var dimAmount = 0.5f
        var gravity = Gravity.CENTER
        var isCancelableOutside = true
        var cancelable = true
        var dialogView: View? = null
        var context: Context? = null
        var positiveBtnListener: IDialog.OnClickListener? = null
        var negativeBtnListener: IDialog.OnClickListener? = null
        var titleStr //默认标题
                : String? = null
        var contentStr //默认内容
                : String? = null
        var positiveStr //右边按钮文字
                : String? = null
        var negativeStr //左边按钮文字
                : String? = null
        var showBtnLeft = false
        var showBtnRight = false
        var animRes: Int =0//Dialog动画style
        var isDefaultLoading=false
        fun apply(controller: YYDialogController) {
            controller.dimAmount = dimAmount
            controller.gravity = gravity
            controller.isCancelableOutside = isCancelableOutside
            controller.cancelable = cancelable
            controller.animRes = animRes
            controller.titleStr = titleStr
            controller.contentStr = contentStr
            controller.positiveStr = positiveStr
            controller.negativeStr = negativeStr
            controller.showBtnLeft = showBtnLeft
            controller.showBtnRight = showBtnRight
            controller.mPositiveButtonListener = positiveBtnListener
            controller.mNegativeButtonListener = negativeBtnListener
            controller.isDefaultLoading=isDefaultLoading
            when {
                layoutRes > 0 -> {
                    controller.setLayoutRes(layoutRes)
                }
                dialogView != null -> {
                    controller.dialogView = dialogView
                }
                else -> {
                    throw IllegalArgumentException("Dialog View can't be null")
                }
            }
            if (dialogWidth > 0) {
                controller.dialogWidth = dialogWidth
            }else{
                controller.dialogWidth=(DisplayUtils.getScreenWidthPixels(context!!) * 0.85f).toInt()
            }
            if (dialogHeight > 0) {
                controller.dialogHeight = dialogHeight
            }else{
                controller.dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }
    }

}