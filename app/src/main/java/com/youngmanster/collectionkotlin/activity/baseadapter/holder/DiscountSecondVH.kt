package com.youngmanster.collectionkotlin.activity.baseadapter.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youngmanster.collectionkotlin.R

/**
 * Created by yangy
 *2021/1/8
 *Describe:
 */
class DiscountSecondVH: RecyclerView.ViewHolder {

    var tvName:TextView?=null

    constructor(itemView: View):super(itemView){
        initView()
    }


    private fun initView(){
        tvName=itemView.findViewById(R.id.tv_name)
    }

}