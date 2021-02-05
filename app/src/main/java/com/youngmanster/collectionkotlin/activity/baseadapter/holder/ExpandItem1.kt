package com.youngmanster.collectionkotlin.activity.baseadapter.holder

import com.youngmanster.collection_kotlin.recyclerview.tree.TreeItem
import java.util.ArrayList

/**
 * Created by yangy
 *2021/1/8
 *Describe:
 */
class ExpandItem1:TreeItem() {

    override fun getLevel(): Int {
        return 1
    }
}