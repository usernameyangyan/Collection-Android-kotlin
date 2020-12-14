package com.youngmanster.collectionkotlin.activity.base

import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.youngmanster.collection_kotlin.base.dialog.CommonDialog
import com.youngmanster.collection_kotlin.base.dialog.new.DialogWrapper
import com.youngmanster.collection_kotlin.base.dialog.new.IDialog
import com.youngmanster.collection_kotlin.base.dialog.new.YYDialog
import com.youngmanster.collection_kotlin.base.dialog.new.YYDialogsManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.GlideUtils
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
        dialog_btn6.setOnClickListener(this)
    }

    override fun requestData() {
    }
    var customizeDialog:CustomizeDialog? =null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialog_btn1 -> {

                YYDialog.Builder(this)
                    .setTitle("默认样式")
                    .setContent("这是一个默认的Dialog样式")
                    .setNegativeButton("取消", object : IDialog.OnClickListener {
                        override fun onClick(dialog: IDialog?) {
                            dialog?.dismiss()
                            ToastUtils.showToast(this@DialogActivity, "点击了取消按钮")
                        }

                    }).setPositiveButton("确定", object : IDialog.OnClickListener {
                        override fun onClick(dialog: IDialog?) {
                            dialog?.dismiss()
                            ToastUtils.showToast(
                                this@DialogActivity,
                                "点击了确定按钮"
                            )
                        }

                    }).show()

            }
            R.id.dialog_btn2 -> {
                YYDialog.Builder(this)
                    .setTitle("正在加载中，请稍后")
                    .setDefaultLoading(true)
                    .setCancelable(true)
                    .setCancelableOutSide(false)
                    .show()
            }
            R.id.dialog_btn3 -> {
                YYDialog.Builder(this)
                    .setTitle("默认样式")
                    .setContent("这是一个默认的Dialog样式")
                    .setPositiveButton("确定", object : IDialog.OnClickListener {
                        override fun onClick(dialog: IDialog?) {
                            dialog?.dismiss()
                            ToastUtils.showToast(
                                this@DialogActivity,
                                "点击了确定按钮"
                            )
                        }

                    }).show()
            }

            R.id.dialog_btn5 -> {
                YYDialog.Builder(this)
                    .setDialogView(R.layout.layout_bottom_up)
                    .setWindowBackgroundP(0.5f)
                    .setAnimStyle(R.style.animation_bottom)
                    .setCancelableOutSide(true)
                    .setCancelableOutSide(true)
                    .setBuildChildListener(object : IDialog.OnBuildListener {

                        override fun onBuildChildView(
                            dialog: IDialog?,
                            view: View?,
                            layoutRes: Int
                        ) {
                            val btn_take_photo = view?.findViewById<Button>(R.id.btn_take_photo)
                            btn_take_photo?.setOnClickListener {
                                ToastUtils.showToast(this@DialogActivity, "拍照")
                                dialog?.dismiss()
                            }
                            val btn_select_photo = view?.findViewById<Button>(R.id.btn_select_photo)
                            btn_select_photo?.setOnClickListener {
                                ToastUtils.showToast(this@DialogActivity, "相册选取")
                                dialog?.dismiss()
                            }
                            val btn_cancel_dialog =
                                view?.findViewById<Button>(R.id.btn_cancel_dialog)
                            btn_cancel_dialog?.setOnClickListener {
                                ToastUtils.showToast(this@DialogActivity, "取消")
                                dialog?.dismiss()
                            }
                        }
                    })
                    .setScreenWidthP(1.0f)
                    .setGravity(Gravity.BOTTOM).show()
            }
            R.id.dialog_btn4 -> {
                customizeDialog =
                    CustomizeDialog(this)
                customizeDialog?.show()
            }

            R.id.dialog_btn6 -> {

                //Build第一个Dialog
                val builder1: YYDialog.Builder = YYDialog.Builder(this)
                    .setDialogView(R.layout.layout_ad_dialog)
                    .setWindowBackgroundP(0.5f)
                    .setBuildChildListener(object : IDialog.OnBuildListener {
                        override fun onBuildChildView(
                            dialog: IDialog?,
                            view: View?,
                            layoutRes: Int
                        ) {
                            val iv_close = view?.findViewById<ImageView>(R.id.iv_close)
                            iv_close?.setOnClickListener {
                                dialog?.dismiss()
                            }
                            val iv_ad = view?.findViewById<ImageView>(R.id.iv_ad)
                            GlideUtils.loadImg(this@DialogActivity,"http://pic1.win4000.com/m00/74/cf/2054d825dbde397cac7b3119e255bcc6.jpg",
                                R.mipmap.ic_bttom_loading_01,iv_ad!!)

                            iv_ad.setOnClickListener {
                                ToastUtils.showToast(this@DialogActivity, "点击图片")
                                dialog?.dismiss()
                            }
                        }
                    })

                //Build第一个Dialog
                val builder2: YYDialog.Builder = YYDialog.Builder(this)
                    .setDialogView(R.layout.layout_ad_dialog)
                    .setWindowBackgroundP(0.5f)
                    .setBuildChildListener(object : IDialog.OnBuildListener {
                        override fun onBuildChildView(
                            dialog: IDialog?,
                            view: View?,
                            layoutRes: Int
                        ) {
                            val iv_close = view?.findViewById<ImageView>(R.id.iv_close)
                            iv_close?.setOnClickListener {
                                dialog?.dismiss()
                            }
                            val iv_ad = view?.findViewById<ImageView>(R.id.iv_ad)
                            GlideUtils.loadImg(this@DialogActivity,"http://pic1.win4000.com/m00/15/39/849f010d29e38c27a1116fec69e3149f.jpg",
                                R.mipmap.ic_bttom_loading_01,iv_ad!!)
                            iv_ad.setOnClickListener {
                                ToastUtils.showToast(this@DialogActivity, "点击图片")
                                dialog?.dismiss()
                            }
                        }
                    })

                //添加第一个Dialog
                YYDialogsManager.getInstance().requestShow(DialogWrapper(builder1))
                YYDialogsManager.getInstance().requestShow(DialogWrapper(builder2))

            }
        }
    }

}