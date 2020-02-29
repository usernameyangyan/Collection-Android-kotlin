package com.youngmanster.collection_kotlin.base.baseview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.youngmanster.collection_kotlin.R
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.ClassGetUtil
import kotlinx.android.synthetic.main.collection_library_default_base_activity.*

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

abstract class IBaseFragment<T: BasePresenter<*>>:Fragment(){

    var mainView: View?=null
    var mPresenter: T? = null

    /**
     * 是否加载过数据
     */
    private var isDataInitiated:Boolean = false
    private var frame_caption_container:FrameLayout?=null
    private var frame_content_container:FrameLayout?=null

    private val defineActionBarConfig: DefaultDefineActionBarConfig = DefaultDefineActionBarConfig()
    var customBar: View?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mPresenter = ClassGetUtil.getClass(this, 0)
        mPresenter?.setV(this)
        mainView=inflater.inflate(R.layout.collection_library_default_base_fragment,container,false)
        frame_caption_container=mainView!!.findViewById(R.id.frame_caption_container)
        frame_content_container=mainView!!.findViewById(R.id.frame_content_container)

        if (getLayoutId() != 0) {
            addContainerFrame(getLayoutId())
        }else{
            throw IllegalArgumentException("请设置getLayoutId")
        }

        val isShowCustomActionBar: Boolean = isShowCustomActionBar()
        val customBarRes = setCustomActionBar()

        if (isShowCustomActionBar) {
            if (customBarRes != 0) {
                customBar = LayoutInflater.from(activity)
                    .inflate(customBarRes, frame_caption_container, false)
                frame_caption_container?.addView(customBar)
            } else {
                val defaultBar:View = LayoutInflater.from(activity)
                    .inflate(R.layout.collection_library_default_common_toolbar, frame_caption_container, false)

                frame_caption_container?.addView(defaultBar)
                defineActionBarConfig?.defaultDefineView=defaultBar
            }

            frame_caption_container?.visibility = View.VISIBLE
        } else {
            frame_caption_container?.visibility = View.GONE
        }

        return mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }


    override fun onResume() {
        super.onResume()
        if(!isDataInitiated){
            requestData()
            isDataInitiated=true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.onDestroy()
        isDataInitiated=false
    }

    /**
     * 显示默认顶部Title栏
     */

    open fun isShowCustomActionBar(): Boolean {
        return false
    }

    /**
     * 设置自定义ActionBar
     */

    open fun setCustomActionBar(): Int{
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

        fun setBarBackgroundColor(context: Context, bgColor: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<RelativeLayout>(R.id.common_bar_panel)?.setBackgroundColor(
                ContextCompat.getColor(context,bgColor))
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

        fun setTitleColor(context: Context, color: Int): DefaultDefineActionBarConfig {
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
        val view = LayoutInflater.from(activity).inflate(layoutResID, frame_content_container, false)
        frame_content_container?.addView(view)
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