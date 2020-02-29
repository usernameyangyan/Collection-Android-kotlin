package com.youngmanster.collectionkotlin.bean

import com.youngmanster.collection_kotlin.data.database.Column

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */
class User{
    @Column(isPrimaryKey = true)
    var id:Int=0
    var name:String?="你好"
    var age:Int?=0
}