package com.youngmanster.collection_kotlin.base.baseview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.youngmanster.collection_kotlin.R
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.ClassGetUtil
import com.youngmanster.collection_kotlin.utils.AndroidWorkaroundUtils
import kotlinx.android.synthetic.main.collection_library_default_base_activity.*

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

abstract class IBaseActivity<T : BasePresenter<*>> : AppCompatActivity() {
    private var isFirst = false
    var mPresenter: T? = null
    var customBar: View?=null
    val defineActionBarConfig:DefaultDefineActionBarConfig=DefaultDefineActionBarConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_library_default_base_activity)
        if (getLayoutId() != 0) {
            addContainerFrame(getLayoutId())
        }else{
            throw IllegalArgumentException("请设置getLayoutId")
        }

        if(isDealDeviceHasNavigationBar()){
            AndroidWorkaroundUtils.assistActivity(findViewById(android.R.id.content))
        }

        mPresenter = ClassGetUtil.getClass(this, 0)
        mPresenter?.setV(this)

        val isShowActionBar: Boolean = isShowSystemActionBar()
        val isShowCustomActionBar: Boolean = isShowCustomActionBar()
        val customRes = setCustomActionBar()
        if (!isShowActionBar) {//如果系统ActionBar隐藏并且自定义Bar显示
            supportActionBar!!.hide()
            if (isShowCustomActionBar) {
                if (customRes != 0) {
                    customBar = LayoutInflater.from(this)
                        .inflate(customRes, frame_caption_container, false)
                    frame_caption_container.addView(customBar)
                } else {
                    val defaultBar:View  = LayoutInflater.from(this)
                        .inflate(R.layout.collection_library_default_common_toolbar, frame_caption_container, false)

                    frame_caption_container.addView(defaultBar)
                    defineActionBarConfig.defaultDefineView=defaultBar
                }

                frame_caption_container.visibility = View.VISIBLE
            } else {
                frame_caption_container.visibility = View.GONE
            }
        } else {//如果系统ActionBar显示那么自定义Bar隐藏
            frame_caption_container.visibility = View.GONE
        }

    }

    override fun onStart() {
        super.onStart()
        if (!isFirst) {
            init()
            requestData()
            isFirst = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
    }

    /**
     * 是否显示系统ActionBar
     */
    open fun isShowSystemActionBar(): Boolean {
        return false
    }

    /**
     * 显示默认顶部Title栏
     */

    open fun isShowCustomActionBar(): Boolean {
        return true
    }

    /***
     * 实现处理底部虚拟按钮
     * @return
     */
    open fun isDealDeviceHasNavigationBar(): Boolean {
        return true
    }

    /**
     * 设置自定义ActionBar
     */

    open fun setCustomActionBar(): Int {
        return 0
    }


    /**
     * 设置自定义ActionBar
     */

    class DefaultDefineActionBarConfig {
        var defaultDefineView: View? = null


        fun hideBackBtn():DefaultDefineActionBarConfig{
            defaultDefineView?.findViewById<ImageView>(R.id.btnBack)?.visibility=View.GONE
            return this
        }

        fun setBarBackgroundColor(context:Context,bgColor: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<RelativeLayout>(R.id.common_bar_panel)?.setBackgroundColor(ContextCompat.getColor(context,bgColor))
            return this
        }

        fun setBarDividingLineHeight(height: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<View>(R.id.lineView)?.layoutParams?.height=height
            return this
        }

        fun setBarDividingLineColor(context:Context,color: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<View>(R.id.lineView)?.setBackgroundColor(ContextCompat.getColor(context,color))
            return this
        }

        fun setBarDividingLineShowStatus(isShow:Boolean): DefaultDefineActionBarConfig {
            if(isShow){
                defaultDefineView?.findViewById<View>(R.id.lineView)?.visibility=View.VISIBLE
            }else{
                defaultDefineView?.findViewById<View>(R.id.lineView)?.visibility=View.GONE
            }

            return this
        }

        fun setBarHeight(height: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<RelativeLayout>(R.id.common_bar_panel)?.layoutParams?.height = height
            return this
        }

        fun setBackIcon(resId: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<ImageView>(R.id.btnBack)?.setImageResource(resId)
            if(resId != 0){
                defaultDefineView?.findViewById<ImageView>(R.id.btnBack)?.visibility=View.VISIBLE
            }
            return this
        }

        fun setTitleColor(context:Context,color: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<TextView>(R.id.titleTv)?.setTextColor(ContextCompat.getColor(context,color))
            return this
        }

        fun setTitleSize(size: Float): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<TextView>(R.id.titleTv)?.textSize = size
            return this
        }

        fun setTitle(con: String): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<TextView>(R.id.titleTv)?.text = con
            return this
        }

        fun setBackClick(onListener:View.OnClickListener):DefaultDefineActionBarConfig{
            defaultDefineView?.findViewById<ImageView>(R.id.btnBack)?.setOnClickListener(onListener)
            return this
        }
    }


    /**
     * add the children layout
     *
     * @param layoutResID
     */
    private fun addContainerFrame(layoutResID: Int) {
        val view = LayoutInflater.from(this).inflate(layoutResID, frame_content_container, false)
        frame_content_container.addView(view)
    }


    fun <U> startAc(claz:Class<U>){
        var intent = Intent()
        intent.setClass(this,claz)
        startActivity(intent)
    }

    /**
     * 布局文件加载
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化参数
     */
    abstract fun init()

    /**
     * 请求数据
     */
    abstract fun requestData()
}