package com.youngmanster.collection_kotlin.mvp

import java.lang.NullPointerException
import java.lang.reflect.ParameterizedType

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */
@Suppress("UNCHECKED_CAST")
class ClassGetUtil{
    companion object{
        fun <T>  getClass(o: Any, i: Int): T? {
            try {
                return ((o.javaClass
                    .genericSuperclass as ParameterizedType).actualTypeArguments[i] as? Class<T>)!!
                    .newInstance()
            } catch (e: Exception) {
            }

            return null
        }
    }
}