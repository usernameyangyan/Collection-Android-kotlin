package com.youngmanster.collectionkotlin.fragment.recyclerview

import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.DefinitionRecyclerAdapter
import com.youngmanster.collectionkotlin.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_addheader_empty.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */

class FragmentAddHeader:BaseFragment<BasePresenter<*>>(){

    private val mtDatas = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_addheader_empty
    }

    override fun init() {
        val header = LayoutInflater.from(activity).inflate(R.layout.layout_main_header, null)
        recycler_rv.addHeaderView(header)

        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager

        for (i in 0..9) {
            mtDatas.add("item$i")
        }

        recycler_rv.adapter =
            DefinitionRecyclerAdapter(
                activity!!,
                R.layout.item_pull_refresh,
                mtDatas,
                recycler_rv
            )

    }

    override fun requestData() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler_rv.destroy()
    }

}