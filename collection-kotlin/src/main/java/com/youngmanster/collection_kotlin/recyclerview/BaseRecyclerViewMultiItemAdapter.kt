package com.youngmanster.collection_kotlin.recyclerview

import android.content.Context
import android.util.SparseIntArray
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
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class BaseRecyclerViewMultiItemAdapter<T :BaseMultiItemEntity> : RecyclerView.Adapter<BaseViewHolder> ,BaseRecycleItemTouchHelper.ItemTouchHelperCallback{

    private var layouts: SparseIntArray? = null
    var mContext: Context?=null
    var mDatas: ArrayList<T>?=null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: onItemLongClickListener? = null
    private var onDragAndDeleteListener: OnDragAndDeleteListener? = null
    private var mRecyclerView: PullToRefreshRecyclerView? = null

    constructor(mContext:Context, mDatas:ArrayList<T> ,pullToRefreshRecyclerView:PullToRefreshRecyclerView){
        this.mContext = mContext
        this.mDatas = mDatas
        this.mRecyclerView = pullToRefreshRecyclerView
    }

    constructor(mContext:Context, mDatas:ArrayList<T> ){
        this.mContext = mContext
        this.mDatas = mDatas
    }

    override fun getItemViewType(position: Int): Int {
        if (!isAddItemView()) return super.getItemViewType(position)

        return if (mDatas?.get(position) is BaseMultiItemEntity) {
            mDatas?.get(position)?.getItemViewType()!!
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutId = layouts?.get(viewType)
        val baseViewHolder = BaseViewHolder.onCreateViewHolder(mContext, parent, layoutId!!)
        setItemClickListener(baseViewHolder)
        return baseViewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, mDatas!![position])
    }

    override fun getItemCount(): Int {
        return mDatas!!.size
    }

    private fun isAddItemView(): Boolean {
        return layouts!!.size() > 0
    }

    /**
     * 添加不同布局
     */
    fun addItemType(type: Int, layoutRes: Int) {
        if (this.layouts == null) {
            this.layouts = SparseIntArray()
        }

        this.layouts?.put(type, layoutRes)
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
    fun setItemClickListener(baseViewHolder: BaseViewHolder) {

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

        baseViewHolder.convertView.setOnLongClickListener(View.OnLongClickListener { v ->
            if (mOnItemLongClickListener != null) {
                var position = if (mRecyclerView != null) {
                    baseViewHolder.adapterPosition - mRecyclerView!!.realItemCount
                } else {
                    baseViewHolder.adapterPosition
                }
                return@OnLongClickListener mOnItemLongClickListener?.onItemLongClick(v, position)!!
            }
            return@OnLongClickListener false
        })
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

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    fun setOnItemLongClickListener(mOnItemLongClickListener: onItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener
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
        mDatas?.removeAt(position)
        notifyItemRemoved(position)
        onDragAndDeleteListener?.onDeleteComplete()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(mDatas, fromPosition, toPosition)//交换数据
        notifyItemMoved(fromPosition, toPosition)

        onDragAndDeleteListener?.onMoveComplete()
    }

    override fun onItemSelected() {

    }

    override fun onItemFinish() {
    }
}