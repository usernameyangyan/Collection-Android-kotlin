package com.youngmanster.collection_kotlin.base.dialog

/**
 * Created by yangy
 *2020/12/14
 *Describe:
 */
class DialogWrapper {
    private var dialog //统一管理dialog的弹出顺序
            : YYDialog.Builder? = null

    constructor(dialog: YYDialog.Builder){
        this.dialog = dialog
    }


    fun getDialog(): YYDialog.Builder? {
        return dialog
    }

    fun setDialog(dialog: YYDialog.Builder?) {
        this.dialog = dialog
    }



}