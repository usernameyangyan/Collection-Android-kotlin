package com.youngmanster.collectionkotlin.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.youngmanster.collection_kotlin.recyclerview.BaseLoadMoreView
import com.youngmanster.collectionkotlin.R

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class DefinitionAnimationLoadMoreView(context: Context?) : BaseLoadMoreView(context) {

    private var loadingIv: ImageView? = null
    private var noDataTv: TextView? = null
    private var loadMore_Ll: LinearLayout? = null
    private var isDestroy = false
    //动画
    private var animationDrawable: AnimationDrawable? = null

    override fun setState(state: Int) {

        if(isDestroy){
            return
        }

        when (state) {
            STATE_LOADING -> {
                loadMore_Ll?.visibility = VISIBLE
                noDataTv?.visibility = INVISIBLE
                animationDrawable = loadingIv?.drawable as AnimationDrawable
                animationDrawable?.start()
                this.visibility = VISIBLE
            }
            STATE_COMPLETE -> {
                animationDrawable?.stop()
                this.visibility = GONE
            }
            STATE_NODATA -> {
                loadMore_Ll?.visibility = INVISIBLE
                noDataTv?.visibility = VISIBLE
                animationDrawable = loadingIv?.drawable as AnimationDrawable
                animationDrawable?.start()
                this.visibility = VISIBLE
            }
        }

        mState = state
    }

    override fun destroy() {
        isDestroy=true
    }

    override fun initView(context: Context?) {
        mContainer = LayoutInflater.from(context)
            .inflate(R.layout.layout_definition_animation_loading_more, null)
        addView(mContainer)
        gravity = Gravity.CENTER

        loadingIv = mContainer.findViewById(R.id.loadingIv)
        noDataTv = mContainer.findViewById(R.id.no_data)
        loadMore_Ll = mContainer.findViewById(R.id.loadMore_Ll)
    }



}