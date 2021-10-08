package com.youngmanster.collection_kotlin.base.dialog

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by yangy
 *2020/12/14
 *Describe:
 */
open class YYDialogsManager private constructor() {
    @Volatile
    private var showing = false //是否有dialog在展示

    private val dialogQueue: ConcurrentLinkedQueue<DialogWrapper> =
        ConcurrentLinkedQueue()

    companion object{
        fun getInstance(): YYDialogsManager {
            return DialogHolder.instance
        }
        private class  DialogHolder{
            companion object{
                val instance =
                    YYDialogsManager()
            }
        }
    }



    /**
     * 请求加入队列并展示
     *
     * @param dialogWrapper DialogWrapper
     * @return 加入队列是否成功
     */
    @Synchronized
    fun requestShow(dialogWrapper: DialogWrapper?): Boolean {
        val b = dialogQueue.offer(dialogWrapper)
        checkAndDispatch()
        return b
    }

    /**
     * 结束一次展示 并且检查下一个弹窗
     */
    @Synchronized
    fun over() {
        showing = false
        next()
    }

    @Synchronized
    private fun checkAndDispatch() {
        if (!showing) {
            next()
        }
    }

    /**
     * 弹出下一个弹窗
     */
    @Synchronized
    private operator fun next() {
        val poll = dialogQueue.poll() ?: return
        val dialog: YYDialog.Builder? = poll.getDialog()
        if (dialog != null) {
            showing = true
            dialog.show()
        }
    }


}