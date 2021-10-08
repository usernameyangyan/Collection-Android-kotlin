package com.youngmanster.collection_kotlin.mvvm.view

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.youngmanster.collection_kotlin.mvvm.command.BindingCommand
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 *  author : yangyan
 *  date : 2021/4/8 9:27 AM
 *  description :
 */

private const val CLICK_INTERVAL = 1

/**
 * requireAll 是意思是是否需要绑定全部参数, false为否
 * View的onClick事件绑定
 * onClickCommand 绑定的命令,
 * isThrottleFirst 是否开启防止过快点击
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
fun onClickCommand(
    view: View,
    clickCommand: BindingCommand?,
    isThrottleFirst: Boolean
) {
    if (isThrottleFirst) {
        RxView.clicks(view)
            .subscribe(Consumer<Any?> {
                clickCommand?.execute()
            })
    } else {
        RxView.clicks(view)
            .throttleFirst(
                CLICK_INTERVAL.toLong(),
                TimeUnit.SECONDS
            ) //1秒钟内只允许点击1次
            .subscribe(Consumer<Any?> {
                clickCommand?.execute()
            })

    }
}


/**
 * view的onLongClick事件绑定
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
fun onLongClickCommand(view: View, clickCommand: BindingCommand?) {
    RxView.longClicks(view)
        .subscribe {
            clickCommand?.execute()
        }
}




