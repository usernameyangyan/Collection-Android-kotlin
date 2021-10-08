package com.youngmanster.collection_kotlin.utils

import java.util.regex.Pattern

/**
 *
 * Created by LiaoYuQiang
 * on 2020/6/17
 */
object MatchUtils {

     fun nameMatch(name:String):Boolean{
        val pattern  = Pattern.compile("^[a-zA-Z0-9\\u4E00-\\u9FA5_\\-]+\$")
        return pattern.matcher(name).matches()
    }
}