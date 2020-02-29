package com.youngmanster.collectionkotlin.bean

import com.youngmanster.collection_kotlin.recyclerview.BaseMultiItemEntity

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class MultiItem:BaseMultiItemEntity{

    companion object{
        const val TYPE_TEXT = 1
        const val TYPE_IMG = 2
        const val TYPE_TEXT_IMG = 3
    }

    var title: String? = null
    var res: Int = 0
    var type: Int = 0

    override fun getItemViewType(): Int {
        return type
    }

}