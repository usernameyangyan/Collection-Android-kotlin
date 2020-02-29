package com.youngmanster.collectionkotlin.bean

import java.io.Serializable

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class HttpResult<T> : Serializable {
    var code: Int = 0
    var message: String? = null
    var result: T? = null
}