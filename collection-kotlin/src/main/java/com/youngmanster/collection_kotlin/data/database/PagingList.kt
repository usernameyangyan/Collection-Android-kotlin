package com.youngmanster.collection_kotlin.data.database

/**
 * Created by yangy
 *2020-02-24
 *Describe:
 */
class PagingList<T>:ArrayList<T>(){

    private val serialVersionUID = 5526933443772285251L

    private var mTotalSize: Int = 0

    /**
     * return total size of your query condition
     * @return
     */
    fun getTotalSize(): Int {
        return mTotalSize
    }

    /**
     * return size of this paing query
     */

    internal fun setTotalSize(totalSize: Int) {
        this.mTotalSize = totalSize
    }
}