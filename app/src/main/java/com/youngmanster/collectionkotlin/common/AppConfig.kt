package com.youngmanster.collectionkotlin.common

import android.content.Context

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
class AppConfig{

    companion object{

        fun getStorageDir(context:Context):String{
            return context.getExternalFilesDir(null)?.absolutePath + "/Collection_kotlin/"
        }

        fun getURLCacheDir(context:Context):String{
            return getStorageDir(context)+ "url/cache/"
        }
    }

}
