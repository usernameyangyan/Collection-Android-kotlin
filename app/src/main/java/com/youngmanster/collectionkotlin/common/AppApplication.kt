package com.youngmanster.collectionkotlin.common

import android.app.Application
import com.youngmanster.collectionkotlin.BuildConfig
import com.youngmanster.collectionkotlin.bean.HttpResult
import com.youngmanster.collection_kotlin.config.Config
import com.youngmanster.collection_kotlin.data.database.SQLiteVersionMigrate

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class AppApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        config()

        SQLiteVersionMigrate().setMigrateListener(object :SQLiteVersionMigrate.MigrateListener{
            override fun onMigrate(oldVersion: Int, newVersion: Int) {
                for (i in oldVersion..newVersion){
                    if(i==2){
                    }

                }
            }

        })
    }

    private fun config(){
        //基本配置
        Config.DEBUG = BuildConfig.DEBUG
        Config.CONTEXT = this
        Config.URL_CACHE = AppConfig.getURLCacheDir(this)
        Config.MClASS=HttpResult::class.java
        Config.USER_CONFIG = "Collection_User"
        Config.URL_DOMAIN = "https://api.apiopen.top/"
        Config.SQLITE_DB_VERSION=0

//        val textConfig = LoadingTextConfig.getInstance(applicationContext)
//        textConfig
//            .setCollectionLoadingMore("加载更多")
//            .setCollectionLastRefreshTimeTip("更新时间：")
//            .setCollectionNoMoreData("没有更多数据")
//            .setCollectionPullReleaseText("释放刷新")
//            .setCollectionRefreshing("正在刷新")
//            .setCollectionPullDownRefreshText("下拉刷新")
//            .setCollectionRefreshDone("加载完成")
//        PullToRefreshRecyclerViewUtils.loadingTextConfig = textConfig

    }
}