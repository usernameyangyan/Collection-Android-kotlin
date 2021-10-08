package com.youngmanster.collection_kotlin.mvvm.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.NonNull
import com.youngmanster.collection_kotlin.base.manager.AppManager
import org.conscrypt.Conscrypt
import java.security.Security

/**
 * Created by yangy
 *2021/3/29
 *Describe:
 */
open class BaseApplication: Application() {

    companion object{
        private var sInstance: Application? = null
    }


    override fun onCreate() {
        super.onCreate()
        setApplication(this)

        Security.insertProviderAt(Conscrypt.newProvider(), 1);
    }
    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     *
     * @param application
     */
    open fun setApplication(@NonNull application: Application) {
        sInstance = application
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                AppManager.getAppManager().addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(
                activity: Activity,
                outState: Bundle
            ) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                AppManager.getAppManager().removeActivity(activity)
            }
        })
    }

    /**
     * 获得当前app运行的Application
     */
    open fun getInstance(): Application? {
        if (sInstance == null) {
            throw NullPointerException("please inherit BaseApplication or call setApplication.")
        }
        return sInstance
    }
}