package com.youngmanster.collectionkotlin.activity.baseadapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.BaseRecycleItemTouchHelper
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.baseadapter.DragAndDeleteAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.layout_recyclerview.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
class DragAndDeleteActivity :BaseActivity<BasePresenter<*>>(), BaseRecyclerViewAdapter.OnDragAndDeleteListener{

    private var dragAndDeleteAdapter: DragAndDeleteAdapter? = null
    private val mDatas = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.layout_recyclerview
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_drag_delete_title))

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        recycler_rv.layoutManager = layoutManager

        for (i in 0..19) {
            mDatas.add((i + 1).toString() + "   左右滑动删除/长按拖动")
        }


        dragAndDeleteAdapter =
            DragAndDeleteAdapter(
                this,
                R.layout.item_main,
                mDatas
            )
        dragAndDeleteAdapter?.setDragAndDeleteListener(this)
        recycler_rv.adapter = dragAndDeleteAdapter

        val callback = BaseRecycleItemTouchHelper(dragAndDeleteAdapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recycler_rv)
    }

    override fun requestData() {
    }



    override fun onMoveComplete() {
        ToastUtils.showToast(this, "移动操作完成")

    }

    override fun onDeleteComplete() {
        ToastUtils.showToast(this, "删除操作完成")
    }

}