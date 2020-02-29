package com.youngmanster.collection_kotlin.utils

import java.util.concurrent.*

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

/**
 * 1.  线程池管理类
 */
class ThreadPoolManager
private constructor() {
    //1.将请求任务放到请求队列中
    //通过阻塞式队列来存储任务
    private val queue = LinkedBlockingQueue<Runnable>()

    //2.把队列中的任务放到线程池
    private var threadPoolExecutor: ThreadPoolExecutor?=null

    private val removeTask = LinkedBlockingQueue<Runnable>()

    //当线程数超过maxPoolSize或者keep-alive时间超时时执行拒绝策略
    private val rejectedExecutionHandler =
        RejectedExecutionHandler { runnable, _ ->
            /**
             * @param runnable 超时被线程池抛弃的线程
             * @param threadPoolExecutor
             */
            //将该线程重新放入请求队列中
            try {
                queue.put(runnable)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    //3.让他们开始工作起来
    //整个的工作线程
    private val runnable = Runnable {
        while (true) {
            var runnable: Runnable? = null
            //不断从从请求队列中取出请求
            try {
                runnable = queue.take()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            //如果不为空，放入线程池中执行
            if (runnable != null) {
                if (!removeTask.contains(runnable)) {
                    threadPoolExecutor?.execute(runnable)
                }
            }
        }
    }

    init {
        threadPoolExecutor = ThreadPoolExecutor(
            4,
            20,
            15,
            TimeUnit.SECONDS,
            ArrayBlockingQueue(4),
            rejectedExecutionHandler
        )
        //运行线程池
        threadPoolExecutor?.execute(runnable)
    }

    //添加任务
    fun execute(runnable: Runnable?) {
        if (runnable != null) {
            try {
                queue.put(runnable)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    fun remove(runnable: Runnable) {
        try {
            removeTask.put(runnable)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun reset() {
        removeTask.clear()
        queue.clear()
    }

    companion object {

        //单例模式
        val singleInstance = ThreadPoolManager()
    }
}