package com.youngmanster.collection_kotlin.recyclerview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseViewHolder>,
    BaseRecycleItemTouchHelper.ItemTouchHelperCallback {
    var mContext: Context? = null
    private var mLayoutResId: Int = 0
    var mDatas: List<T>? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: onItemLongClickListener? = null
    private var onDragAndDeleteListener: OnDragAndDeleteListener? = null
    private var mRecyclerView: PullToRefreshRecyclerView? = null

    constructor(
        mContext: Context?,
        mLayoutResId: Int,
        mDatas: List<T>,
        pullToRefreshRecyclerView: PullToRefreshRecyclerView
    ) {
        this.mContext = mContext
        this.mLayoutResId = mLayoutResId
        this.mDatas = mDatas
        this.mRecyclerView = pullToRefreshRecyclerView
    }

    constructor(mContext: Context, mLayoutResId: Int, mDatas: List<T>) {
        this.mContext = mContext
        this.mLayoutResId = mLayoutResId
        this.mDatas = mDatas
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val baseViewHolder = BaseViewHolder.onCreateViewHolder(mContext, parent, mLayoutResId)
        setItemClickListener(baseViewHolder)
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, mDatas!![position])
    }

    override fun getItemCount(): Int {
        return mDatas!!.size
    }

    /**
     * 简化ViewHolder创建
     * @param baseViewHolder
     * @param t
     */
    abstract fun convert(baseViewHolder: BaseViewHolder, t: T)

    /**
     * 获取data
     * @return
     */
    fun getDatas(): List<T> {
        return mDatas!!
    }


    /**
     * item点击事件
     * @param baseViewHolder
     */
    private fun setItemClickListener(baseViewHolder: BaseViewHolder) {

        baseViewHolder.convertView.setOnClickListener { v ->
            if (mOnItemClickListener != null) {
                var position = if (mRecyclerView != null) {
                    baseViewHolder.adapterPosition - mRecyclerView!!.realItemCount
                } else {
                    baseViewHolder.adapterPosition
                }

                mOnItemClickListener?.onItemClick(v, position)
            }
        }

        baseViewHolder.convertView.setOnLongClickListener { v ->
            if (mOnItemLongClickListener != null) {

                val position = if (mRecyclerView != null) {
                    baseViewHolder.adapterPosition - mRecyclerView!!.realItemCount
                } else {
                    baseViewHolder.adapterPosition
                }

                return@setOnLongClickListener mOnItemLongClickListener?.onItemLongClick(v, position)!!
            }
            return@setOnLongClickListener false
        }
    }

    /**
     * 点击事件监听
     */
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    interface onItemLongClickListener {
        fun onItemLongClick(view: View, position: Int): Boolean
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener
    }


    /**
     * ========================================拖拽删除=======================================
     */

    interface OnDragAndDeleteListener {
        fun onMoveComplete()
        fun onDeleteComplete()
    }

    fun setDragAndDeleteListener(onDragAndDeleteListener: OnDragAndDeleteListener) {
        this.onDragAndDeleteListener = onDragAndDeleteListener
    }

    override fun onItemDelete(position: Int) {
        (mDatas as ArrayList).removeAt(position)
        notifyItemRemoved(position)
        onDragAndDeleteListener?.onDeleteComplete()
    }

    private var isMove:Boolean=false
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(mDatas, fromPosition, toPosition)//交换数据
        notifyItemMoved(fromPosition, toPosition)
        isMove=true
    }

    override fun onItemSelected() {

    }

    override fun onItemFinish() {
        if(isMove){
            isMove=false
            onDragAndDeleteListener?.onMoveComplete()
        }
    }
}