package com.youngmanster.collectionkotlin.view

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.youngmanster.collection_kotlin.recyclerview.BasePullToRefreshView
import com.youngmanster.collectionkotlin.R

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class DefinitionAnimationRefreshHeaderView(context: Context?) : BasePullToRefreshView(context),BasePullToRefreshView.OnStateChangeListener{

    private var ivPullBgOne: ImageView? = null
    private var ivPullBgTwo:ImageView? = null
    private var ivWheelOne: ImageView? = null
    private var ivWheelTwo:ImageView? = null
    private var ivSun: ImageView? = null

    private var wheelAnimation: Animation? = null
    private var sunAnimation:Animation? = null  //轮子、太阳动画
    private var backAnimationOne: Animation? = null
    private var backAnimationTwo:Animation? = null    //两张背景图动画

    private var isDestroy = false

    init {
        onStateChangeListener=this
    }

    override fun initView(context: Context?) {
        mContainer =
            LayoutInflater.from(context).inflate(R.layout.layout_definition_animation_refresh, null)

        ivPullBgOne = mContainer.findViewById(R.id.iv_pull_bg_one)
        ivPullBgTwo = mContainer.findViewById(R.id.iv_pull_bg_two)
        ivWheelOne = mContainer.findViewById(R.id.ivWheelOne)
        ivWheelTwo = mContainer.findViewById(R.id.ivWheelTwo)
        ivSun = mContainer.findViewById(R.id.ivSun)

        //获取动画
        wheelAnimation = AnimationUtils.loadAnimation(context, R.anim.wheel_animation)
        sunAnimation = AnimationUtils.loadAnimation(context, R.anim.sun_animation)
        val lir = LinearInterpolator()
        wheelAnimation?.interpolator = lir
        sunAnimation?.interpolator = lir
        backAnimationOne = AnimationUtils.loadAnimation(context, R.anim.back_animation_one)
        backAnimationTwo = AnimationUtils.loadAnimation(context, R.anim.back_animation_two)

        //把刷新头部的高度初始化为0
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 0, 0, 0)
        this.layoutParams = lp
        this.setPadding(0, 0, 0, 0)
        addView(mContainer, LayoutParams(LayoutParams.MATCH_PARENT, 0))
        gravity = Gravity.BOTTOM

        //测量高度
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mMeasuredHeight = measuredHeight
    }

    /**
     * 开启动画
     */
    fun startAnim() {
        ivPullBgOne?.startAnimation(backAnimationOne)
        ivPullBgTwo?.startAnimation(backAnimationTwo)
        ivSun?.startAnimation(sunAnimation)
        ivWheelOne?.startAnimation(wheelAnimation)
        ivWheelTwo?.startAnimation(wheelAnimation)
    }

    /**
     * 清除动画
     */
    fun clearAnim() {
        ivPullBgOne?.clearAnimation()
        ivPullBgTwo?.clearAnimation()
        ivSun?.clearAnimation()
        ivWheelOne?.clearAnimation()
        ivWheelTwo?.clearAnimation()
    }

    override fun setRefreshTimeVisible(show: Boolean) {
    }

    override fun destroy() {
        isDestroy=true
        backAnimationOne?.cancel()
        backAnimationTwo?.cancel()
        sunAnimation?.cancel()
        wheelAnimation?.cancel()
    }

    override fun onStateChange(state: Int) {

        if(isDestroy){
            return
        }
        //下拉时状态相同不做继续保持原有的状态
        if (state == mState) return
        //根据状态进行动画显示
        when (state) {
            STATE_PULL_DOWN -> {
                clearAnim()
                startAnim()
            }
            STATE_RELEASE_REFRESH -> {
            }
            STATE_REFRESHING -> {
                clearAnim()
                startAnim()
                scrollTo(mMeasuredHeight)
            }
            STATE_DONE -> {
            }
        }
        mState = state
    }

}