package com.youngmanster.collection_kotlin.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class SoftInputUtils {

    companion object{
        fun hideSoftInput(context: Context) {
            val view = (context as Activity).window.peekDecorView()
            if (view != null) {
                val inputmanger =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }


        fun showSoftInput(editText: EditText) {
            val inputManager =
                editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }
    }

}
