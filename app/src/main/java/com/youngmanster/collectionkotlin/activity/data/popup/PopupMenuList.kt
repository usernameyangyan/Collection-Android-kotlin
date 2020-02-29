package com.youngmanster.collectionkotlin.activity.data.popup

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youngmanster.collection_kotlin.base.dialog.BasePopupWindow
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.data.PopupMenuListAdapter
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class PopupMenuList :BasePopupWindow{
    override fun getPopupLayoutRes(): Int {
        return R.layout.popup_menu_list
    }

    override fun getPopupAnimationStyleRes(): Int {
        return 0
    }

    constructor(context: Context):super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT){
        val list = ArrayList<String>()
        for (i in 0..4) {
            list.add("item$i")
        }
        val rv=popupView?.findViewById<RecyclerView>(R.id.rv)
        val popupMenuListAdapter = PopupMenuListAdapter(context, R.layout.item_popup,list)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv?.layoutManager = linearLayoutManager
        rv?.adapter = popupMenuListAdapter
    }
}