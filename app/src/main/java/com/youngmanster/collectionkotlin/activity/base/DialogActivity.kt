package com.youngmanster.collectionkotlin.activity.base

import android.view.View
import com.youngmanster.collection_kotlin.base.dialog.CommonDialog
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.activity.data.dialog.CustomizeDialog
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_dialog.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */
class DialogActivity :BaseActivity<BasePresenter<*>>(),View.OnClickListener{

    private var commonDialog: CommonDialog?=null
    private val items = arrayOf("男生", "女生")

    override fun getLayoutId(): Int {
        return R.layout.activity_dialog    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.dialog_title))

        dialog_btn1.setOnClickListener(this)
        dialog_btn2.setOnClickListener(this)
        dialog_btn3.setOnClickListener(this)
        dialog_btn4.setOnClickListener(this)
        dialog_btn5.setOnClickListener(this)
    }

    override fun requestData() {
    }
    var customizeDialog:CustomizeDialog? =null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialog_btn1 ->{
                commonDialog = CommonDialog(
                    CommonDialog.DIALOG_TEXT_TWO_BUTTON_DEFAULT,
                    "默认样式",
                    "这是一个默认的Dialog样式",
                    object : CommonDialog.OnDialogClickListener {
                        override fun onDialogClick(state: Int) {
                            when (state) {
                                CommonDialog.ONCLICK_LEFT -> ToastUtils.showToast(this@DialogActivity,"点击了取消按钮")
                                CommonDialog.ONCLICK_RIGHT -> ToastUtils.showToast(
                                    this@DialogActivity,
                                    "点击了确定按钮"
                                )
                            }
                        }
                    })
                commonDialog?.show(supportFragmentManager,null)
            }
            R.id.dialog_btn2 ->{
                commonDialog =
                    CommonDialog( CommonDialog.DIALOG_LOADING_PROGRASSBAR, "正在加载中，请稍后")
                commonDialog?.show(supportFragmentManager,null)
            }
            R.id.dialog_btn3 ->{
                commonDialog = CommonDialog(
                    CommonDialog.DIALOG_TEXT_TWO_BUTTON_CUSTOMIZE,
                    "修改点击按钮",
                    "这是一个修改按钮提示的Dialog样式",
                    "退出",
                    "去设置",
                    object : CommonDialog.OnDialogClickListener {
                        override fun onDialogClick(state: Int) {
                            when (state) {
                                CommonDialog.ONCLICK_LEFT -> ToastUtils.showToast(
                                    this@DialogActivity,
                                    "点击了退出按钮"
                                )
                                CommonDialog.ONCLICK_RIGHT -> ToastUtils.showToast(
                                    this@DialogActivity,
                                    "点击了去设置按钮"
                                )
                            }
                        }
                    })
                commonDialog?.show(supportFragmentManager,null)
            }

            R.id.dialog_btn5 ->{

                commonDialog = CommonDialog(
                    CommonDialog.DIALOG_CHOICE_ITEM,
                    "单项选择",
                    items,
                    object : CommonDialog.OnDialogClickListener {
                        override fun onDialogClick(state: Int) {
                            when (state) {
                                CommonDialog.ONCLICK_LEFT -> ToastUtils.showToast(
                                    this@DialogActivity,
                                    "点击了取消按钮"
                                )
                                CommonDialog.ONCLICK_RIGHT -> ToastUtils.showToast(
                                    this@DialogActivity,
                                    "点击了确定按钮"
                                )
                                else -> ToastUtils.showToast(this@DialogActivity, items[state])
                            }
                        }
                    })
                commonDialog?.show(supportFragmentManager,null)
            }
            R.id.dialog_btn4 ->{
                customizeDialog =
                    CustomizeDialog()
                //如果需要设置dialog的高度
                customizeDialog?.setDialogHeight(DisplayUtils.dip2px(this,300f))
                customizeDialog?.show(supportFragmentManager,null)
            }
        }
    }

}