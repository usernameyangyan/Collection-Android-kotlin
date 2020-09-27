package com.youngmanster.collection_kotlin.base.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collection_kotlin.utils.LogUtils


/**
 * Created by yangy
 *2020/4/28
 *Describe:
 */

abstract class BaseDialogFragment :DialogFragment(),View.OnTouchListener{

    var mainView: View? = null
    var dismissListener: OnDismissListener?=null
    var builder: AlertDialog.Builder? = null
    var screenWidthPixels: Int = 0
    var space: Int = 0
    var height: Int = 0
    var layoutRes:Int=0


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        space = DisplayUtils.dip2px(context!!, 20f)
        if(this.layoutRes!=0){
            this.mainView = LayoutInflater.from(context).inflate(layoutRes, null)
        }else{
            if(setContentView()!=0){
                this.mainView = LayoutInflater.from(context).inflate(setContentView(), null)
            }

        }

        builder = AlertDialog.Builder(context!!)
        if (mainView != null) {
            builder!!.setView(mainView)
        }

        onViewCreated()
        return builder!!.create()
    }

    /**
     * 设置布局
     */
    open fun setContentView():Int{
        return 0
    }
    protected abstract fun onViewCreated()


    private var mCancelable = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val isShow  = this.showsDialog;
        this.showsDialog = false
        super.onActivityCreated(savedInstanceState);
        this.showsDialog = isShow

        val activity  = activity
        if (activity != null)
        {
            this.dialog!!.setOwnerActivity(activity)
        }
        this.dialog!!.setCancelable(false);
        this.dialog!!.window!!.decorView.setOnTouchListener(this)
        this.dialog!!.setOnKeyListener(object :DialogInterface.OnKeyListener{
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                if(!isAllCancelable){
                    return false
                }
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE){
                    this@BaseDialogFragment.dismiss()
                    return true
                }

                return false
            }

        })
        if (savedInstanceState != null)
        {
            val dialogState = savedInstanceState.getBundle("android:savedDialogState");
            if (dialogState != null)
            {
                this.dialog!!.onRestoreInstanceState(dialogState);
            }
        }

        showConfig()
    }
    /**
     * 点击返回键和外部不可取消
     */
    private var isAllCancelable=true
    fun setAllCancelable(b: Boolean) {
        isAllCancelable=b
    }
    /**
     * 点击返回键可以取消
     */
    fun setOnlyBackPressDialogCancel() {
        mCancelable=true
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if(!this.isAllCancelable){
            return false
        }
        if (!mCancelable && dialog?.isShowing!!) {
            this.dismiss()
            return true
        }
        return false
    }


    fun setOnDismissListener(dismissListener: OnDismissListener){
        this.dismissListener=dismissListener
    }

    private fun onDismiss(){
        dismissListener?.dismissListener()
    }

    private fun showConfig() {
        screenWidthPixels = DisplayUtils.getScreenWidthPixels(context!!)
        val params = this.dialog!!.window?.attributes
        params?.width   = (screenWidthPixels - space * 2)
        params?.gravity  = Gravity.CENTER
        if (height > 0) {
            params?.height = height
        }
        this.dialog!!.window?.attributes = params
    }




    fun setDialogInterval(interval: Int) {
        space = interval
    }

    fun setDialogHeight(height: Int) {
        this.height = height
    }


    interface OnDismissListener {
        fun dismissListener()
    }

    override fun show(manager: FragmentManager, tag: String?) {


        try {
            if (this.isAdded)
                this.dismiss();
            else
                manager.beginTransaction().add(this, tag).commitAllowingStateLoss()

        }catch (e:Exception){
            this.dismiss()
        }

    }


    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        try {
            if (this.isAdded)
                this.dismiss();
            else
                return transaction.add(this, tag).commitAllowingStateLoss()
        }catch (e:Exception){
            this.dismiss()
        }

        return 0
    }

    override fun dismiss() {
        try {
            super.dismiss()
            onDismiss()
        }catch(e: IllegalStateException){
        }
    }
}