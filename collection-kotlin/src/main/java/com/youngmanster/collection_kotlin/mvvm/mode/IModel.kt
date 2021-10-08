package com.youngmanster.collection_kotlin.mvvm.mode

/**
 * Created by yangy
 *2021/3/29
 *Describe:
 */
interface IModel {
    /**
     * ViewModel销毁时清除Model，与ViewModel共消亡。Model层同样不能持有长生命周期对象
     */
    fun onCleared()
}