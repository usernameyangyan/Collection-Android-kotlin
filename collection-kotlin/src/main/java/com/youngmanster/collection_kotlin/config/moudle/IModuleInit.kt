package com.youngmanster.collection_kotlin.config.moudle

import android.app.Application

/**
 *  author : yangyan
 *  date : 2021/4/12 11:34 AM
 *  description :
 */
interface IModuleInit {
    fun onInitModule(application: Application)
}