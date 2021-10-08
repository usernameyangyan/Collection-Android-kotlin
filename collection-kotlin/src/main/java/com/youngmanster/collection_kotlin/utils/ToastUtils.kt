package com.youngmanster.collection_kotlin.utils

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import com.youngmanster.collection_kotlin.config.Config

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class ToastUtils {
    companion object {
        fun showToast(message: String) {
            createToast(Gravity.BOTTOM, message, Toast.LENGTH_SHORT)
        }

        fun showToast(strResId: Int) {
            val text = Config.CONTEXT?.getString(strResId)
            createToast( Gravity.BOTTOM, text, Toast.LENGTH_SHORT)
        }

        fun showLongToast(message: String) {
            createToast(Gravity.BOTTOM, message, Toast.LENGTH_LONG)
        }

        fun showLongToast(strResId: Int) {
            val text = Config.CONTEXT?.getString(strResId)
            createToast(Gravity.BOTTOM, text, Toast.LENGTH_LONG)
        }


        fun showToast(message: String, gravity: Int) {
            createToast(gravity, message, Toast.LENGTH_SHORT)
        }

        fun showToast(strResId: Int, gravity: Int) {
            val text = Config.CONTEXT?.getString(strResId)
            createToast(gravity, text, Toast.LENGTH_SHORT)
        }

        fun showLongToast(message: String, gravity: Int) {
            createToast(gravity, message, Toast.LENGTH_LONG)
        }

        fun showLongToast(strResId: Int, gravity: Int) {
            val text = Config.CONTEXT?.getString(strResId)
            createToast(gravity, text, Toast.LENGTH_LONG)
        }


        private fun setGravity(toast: Toast, gravity: Int) {
            toast.setGravity(gravity, 0, 0)
        }


        private fun createToast(gravity: Int, str: String?, delay: Int) {
            var toast: Toast? = null
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                if (null == toast) {
                    toast = Toast(Config.CONTEXT)
                    setGravity(toast, gravity)
                    toast.duration = delay
                } else {
                    toast.duration = delay
                    toast.setText(str)
                }
                toast.show()
            } else {
                toast?.cancel()
                toast = Toast.makeText(Config.CONTEXT,str,delay)
                setGravity(toast, gravity)
                toast.show()
            }
        }

    }
}
