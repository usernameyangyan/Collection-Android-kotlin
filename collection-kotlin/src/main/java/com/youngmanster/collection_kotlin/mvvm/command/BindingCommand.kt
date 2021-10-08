package com.youngmanster.collection_kotlin.mvvm.command

/**
 *  author : yangyan
 *  date : 2021/4/8 9:23 AM
 *  description :
 */
open class BindingCommand {
    private var execute: BindingAction? = null

    constructor(execute: BindingAction) {
        this.execute = execute
    }


    /**
     * 执行BindingAction命令
     */
    open fun execute() {
        if (execute != null) {
            execute!!.call()
        }
    }

}