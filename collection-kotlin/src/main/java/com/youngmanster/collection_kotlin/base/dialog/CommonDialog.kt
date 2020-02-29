package com.youngmanster.collection_kotlin.base.dialog

import android.content.Context
import android.content.DialogInterface
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.youngmanster.collection_kotlin.R
import com.youngmanster.collection_kotlin.utils.GlideUtils
import com.youngmanster.collection_kotlin.utils.LogUtils
import kotlin.math.roundToInt

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class CommonDialog : BaseDialog {

    //点击事件监听
    private var listener: OnDialogClickListener? = null

    private var type: Int = 0
    private var title: String? = null
    private var content: String? = null
    private var leftBtn: String?=null
    private var rightBtn: String?=null
    private var items: Array<String>?=null

    private var layoutRes: Int=0
        get() {
            var layoutRes = 0
            when (type) {
                DIALOG_TEXT_TWO_BUTTON_DEFAULT -> layoutRes = R.layout.collection_library_dialog_title_text_two_button
                DIALOG_TEXT_TWO_BUTTON_CUSTOMIZE -> layoutRes =
                    R.layout.collection_library_dialog_title_text_two_button
                DIALOG_LOADING_PROGRASSBAR -> layoutRes = R.layout.collection_library_dialog_progress
                DIALOG_CHOICE_ITEM -> {
                }
            }
            return layoutRes
        }

    constructor(context: Context, type: Int, content: String) : super(context) {
        this.type = type
        this.content = content

        setContentView(layoutRes)
    }

    constructor(
        context: Context,
        type: Int,
        title: String,
        items: Array<String>,
        listener: OnDialogClickListener
    ) : super(context) {
        this.type = type
        this.title = title
        this.items = items
        this.listener = listener

        setContentView(layoutRes)
    }

    constructor(
        context: Context,
        type: Int,
        title: String,
        content: String,
        listener: OnDialogClickListener
    ) : super(context) {
        this.type = type
        this.title = title
        this.content = content
        this.listener = listener

        setContentView(layoutRes)
    }

    constructor(
        context: Context,
        type: Int,
        title: String,
        content: String,
        leftBtn: String,
        rightBtn: String,
        listener: OnDialogClickListener
    ) : super(context) {
        this.type = type
        this.title = title
        this.content = content
        this.leftBtn = leftBtn
        this.rightBtn = rightBtn
        this.listener = listener

        setContentView(layoutRes)
    }


    override fun onViewCreated() {
        when (type) {
            DIALOG_TEXT_TWO_BUTTON_DEFAULT -> {
                (mainView?.findViewById(R.id.dg_content) as TextView).text = content
                builder?.setTitle(title)
                builder?.setPositiveButton(R.string.collection_dialog_submit
                ) { _, _ ->
                    if (listener != null)
                        listener!!.onDialogClick(ONCLICK_RIGHT)
                }
                builder?.setNegativeButton(R.string.collection_dialog_cancel
                ) { _, _ ->
                    if (listener != null)
                        listener!!.onDialogClick(ONCLICK_LEFT)
                }
            }
            DIALOG_TEXT_TWO_BUTTON_CUSTOMIZE -> {
                (mainView?.findViewById(R.id.dg_content) as TextView).text = content
                builder?.setTitle(title)
                builder?.setPositiveButton(
                    rightBtn
                ) { _, _ ->
                    if (listener != null)
                        listener!!.onDialogClick(ONCLICK_RIGHT)
                }
                builder?.setNegativeButton(
                    leftBtn
                ) { _, _ ->
                    if (listener != null)
                        listener!!.onDialogClick(ONCLICK_LEFT)
                }
            }

            DIALOG_LOADING_PROGRASSBAR -> (mainView?.findViewById(R.id.tv_ProgressTip) as TextView).text =
                content
            DIALOG_CHOICE_ITEM -> {
                builder?.setTitle(title)
                val onClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        AlertDialog.BUTTON_POSITIVE -> if (listener != null)
                            listener!!.onDialogClick(ONCLICK_RIGHT)
                        AlertDialog.BUTTON_NEGATIVE -> if (listener != null)
                            listener!!.onDialogClick(ONCLICK_LEFT)
                        else -> if (listener != null)
                            listener!!.onDialogClick(which)
                    }
                }
                builder?.setSingleChoiceItems(items, 0, onClickListener)
                builder?.setPositiveButton(R.string.collection_dialog_submit, onClickListener)
                builder?.setNegativeButton(R.string.collection_dialog_cancel, onClickListener)
            }
        }
    }

    /**
     * 点击监听
     */
    interface OnDialogClickListener {
        fun onDialogClick(state: Int)
    }


    companion object {

        const val DIALOG_TEXT_TWO_BUTTON_DEFAULT = 1// 提示信息确认和取消
        const val DIALOG_TEXT_TWO_BUTTON_CUSTOMIZE = 2//自定义按钮
        const val DIALOG_LOADING_PROGRASSBAR = 3// 等待中
        const val DIALOG_CHOICE_ITEM = 4//单项选择

        const val ONCLICK_LEFT = 0x10001// 点击对话框的左边
        const val ONCLICK_RIGHT = 0x1002// 点击对话框的右边
    }
}
