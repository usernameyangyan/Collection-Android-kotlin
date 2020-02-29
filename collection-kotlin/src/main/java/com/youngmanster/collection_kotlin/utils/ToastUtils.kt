package com.youngmanster.collection_kotlin.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class ToastUtils {
    companion object{
        fun showToast(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showToast(context: Context?, strResId: Int) {
            val text = context?.getString(strResId)
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun showLongToast(context: Context?, strResId: Int) {
            val text = context?.getString(strResId)
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }


        fun showToast(context: Context?, message: String, gravity: Int) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            setGravity(toast, gravity)
            toast.show()
        }

        fun showToast(context: Context?, strResId: Int, gravity: Int) {
            val text = context?.getString(strResId)
            val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            setGravity(toast, gravity)
            toast.show()

        }

        fun showLongToast(context: Context?, message: String, gravity: Int) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            setGravity(toast, gravity)
            toast.show()
        }

        fun showLongToast(context: Context?, strResId: Int, gravity: Int) {
            val text = context?.getString(strResId)
            val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
            setGravity(toast, gravity)
            toast.show()
        }


        private fun setGravity(toast: Toast, gravity: Int) {
            toast.setGravity(gravity, 0, 0)
        }
    }
}
