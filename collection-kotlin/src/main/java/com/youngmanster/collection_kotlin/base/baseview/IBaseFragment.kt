package com.youngmanster.collection_kotlin.base.baseview

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.youngmanster.collection_kotlin.R
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.ClassGetUtil


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
    var isDataInitiated:Boolean = false
    private var isInitLazy:Boolean=false
    private var isResumeLoad=false
    private var frame_caption_container:FrameLayout?=null
    private var frame_content_container:FrameLayout?=null

    val defineActionBarConfig: DefaultDefineActionBarConfig = DefaultDefineActionBarConfig()
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
                defineActionBarConfig.defaultDefineView=defaultBar
            }

            frame_caption_container?.visibility = View.VISIBLE
        } else {
            frame_caption_container?.visibility = View.GONE
        }

        isInitLazy=onCreateViewAndInitLazy()

        isResumeLoad=true
        if(!isInitLazy){
            layoutInit()
        }

        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!isInitLazy){
            isResumeLoad=false
            isInitLazy=true
            init()
        }
    }

    override fun onResume() {
        super.onResume()

        if(isInitLazy&&isResumeLoad){
            isInitLazy=false
            layoutInit()
            init()
        }

        if(!isDataInitiated){
            requestData()
            isDataInitiated=true
        }
    }


    private fun layoutInit(){
        if (getLayoutId() != 0) {
            addContainerFrame(getLayoutId())
        }else{
            throw IllegalArgumentException("请设置getLayoutId")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.onDestroy()
        isDataInitiated=false
    }

    /**
     * 是否进行布局懒加载
     */
    open fun onCreateViewAndInitLazy(): Boolean {
        return false
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

        fun setBarBackground(bgColor: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<LinearLayout>(R.id.common_bar_panel)?.setBackgroundResource(bgColor)
            return this
        }

        fun setBarPadding(left:Int,top:Int,right:Int,bottom:Int):DefaultDefineActionBarConfig{
            (defaultDefineView?.findViewById<RelativeLayout>(R.id.inRootRel)?.layoutParams as LinearLayout.LayoutParams).setMargins(left,top,right,bottom)
            return this
        }

        fun setBarDividingLineHeight(height: Int):DefaultDefineActionBarConfig{
            defaultDefineView?.findViewById<View>(R.id.lineView)?.layoutParams?.height=height
            return this
        }

        fun setBarDividingLineBackground(color: Int):DefaultDefineActionBarConfig{
            defaultDefineView?.findViewById<View>(R.id.lineView)?.setBackgroundResource(color)
            return this
        }

        fun setBarDividingLineShowStatus(isShow:Boolean):DefaultDefineActionBarConfig{
            if(isShow){
                defaultDefineView?.findViewById<View>(R.id.lineView)?.visibility=View.VISIBLE
            }else{
                defaultDefineView?.findViewById<View>(R.id.lineView)?.visibility=View.GONE
            }

            return this
        }

        fun setBarHeight(height: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<LinearLayout>(R.id.common_bar_panel)?.layoutParams?.height = height
            return this
        }

        fun setBackIcon(resId: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<ImageView>(R.id.btnBack)?.setImageResource(resId)
            if(resId != 0){
                defaultDefineView?.findViewById<ImageView>(R.id.btnBack)?.visibility=View.VISIBLE
            }
            return this
        }

        fun setTitleColor(context: Context,color: Int): DefaultDefineActionBarConfig {
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




    /******************************Fragment**********************************/


    open fun onBackPressed():Boolean{
        return true
    }

    /**
     * IBaseActivity.
     */
    private var mActivity: IBaseActivity<*>? = null

    companion object{
        const val REQUEST_CODE_INVALID = IBaseActivity.REQUEST_CODE_INVALID
        const val RESULT_OK: Int = Activity.RESULT_OK
        const val RESULT_CANCELED = Activity.RESULT_CANCELED
    }

    // ------------------------- Stack ------------------------- //
    /**
     * Stack info.
     */
    private var mStackEntity: IBaseActivity.Companion.FragmentStackEntity? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as IBaseActivity<*>
    }


    /**
     * Get the resultCode for requestCode.
     */
    fun setStackEntity(@NonNull stackEntity: IBaseActivity.Companion.FragmentStackEntity) {
        this.mStackEntity = stackEntity
    }

    /**
     * Set result.
     *
     * @param resultCode result code, one of [IBaseFragment.RESULT_OK], [IBaseFragment.RESULT_CANCELED].
     */
    protected fun setResult(@ResultCode resultCode: Int) {
        mStackEntity!!.resultCode = resultCode
    }

    /**
     * Set result.
     *
     * @param resultCode resultCode, use [].
     * @param result     [Bundle].
     */
    protected fun setResult(@ResultCode resultCode: Int, @NonNull result: Bundle?) {
        mStackEntity!!.resultCode = resultCode
        mStackEntity!!.result = result
    }


    /**
     * You should override it.
     *
     * @param resultCode resultCode.
     * @param result     [Bundle].
     */
    open fun onFragmentResult(
        requestCode: Int,
        @ResultCode resultCode: Int,
        @Nullable result: Bundle?
    ) {
    }


    /**
     * Show a fragment.
     *
     * @param clazz fragment class.
    </T> */
    fun <T : IBaseFragment<*>> startFragment(clazz: Class<T>) {
        try {
            val targetFragment: IBaseFragment<*> = clazz.newInstance()
            startFragment(
                targetFragment,
                true,
                REQUEST_CODE_INVALID,
                false
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * Show a fragment.
     *
     * @param clazz       fragment class.
    </T> */
    fun <T : IBaseFragment<*>> startFragment(
        clazz: Class<T>,
        isSkipAnimation: Boolean
    ) {
        try {
            val targetFragment: IBaseFragment<*> = clazz.newInstance()
            startFragment(
                targetFragment,
                true,
                REQUEST_CODE_INVALID,
                isSkipAnimation
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
    </T> */
    fun <T : IBaseFragment<*>> startFragment(targetFragment: T) {
        startFragment(targetFragment, true, REQUEST_CODE_INVALID,false)
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
    </T> */
    fun <T : IBaseFragment<*>> startFragment(
        targetFragment: T,
        isSkipAnimation: Boolean
    ) {
        startFragment(
            targetFragment,
            true,
            REQUEST_CODE_INVALID,
            isSkipAnimation
        )
    }

    /**
     * Show a fragment for result.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
    </T> */
    fun <T : IBaseFragment<*>> startFragmentForResult(
        clazz: Class<T>,
        requestCode: Int
    ) {
        try {
            val targetFragment: IBaseFragment<*> = clazz.newInstance()
            startFragment(targetFragment, true, requestCode,false)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
    * Show a fragment for result.
    *
    * @param clazz       fragment to display.
    * @param requestCode requestCode.
    </T> */
    fun <T : IBaseFragment<*>> startFragmentForResult(
        clazz: Class<T>,
        requestCode: Int,
        isSkipAnimation: Boolean
    ) {
        try {
            val targetFragment: IBaseFragment<*> = clazz.newInstance()
            startFragment(targetFragment, true, requestCode,isSkipAnimation)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Show a fragment for result.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.
    </T> */
    fun <T : IBaseFragment<*>> startFragmentForResult(
        targetFragment: T,
        requestCode: Int
    ) {
        startFragment(targetFragment, true, requestCode,false)
    }

    /**
     * Show a fragment for result.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.
    </T> */
    fun <T : IBaseFragment<*>> startFragmentForResult(
        targetFragment: T,
        requestCode: Int,
        isSkipAnimation: Boolean
    ) {
        startFragment(targetFragment, true, requestCode,isSkipAnimation)
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param requestCode    requestCode.
    </T> */
    private  fun <T : IBaseFragment<*>> startFragment(
        targetFragment: T,
        stickyStack: Boolean,
        requestCode: Int,
        isSkipAnimation:Boolean
    ) {
        mActivity?.startFragment(this, targetFragment, stickyStack, requestCode,isSkipAnimation)
    }

    fun <T : IBaseFragment<*>> findFragment(clazz: Class<T>):T?{
        return mActivity!!.findFragment(clazz)
    }


    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) { //表示是一个进入动作，比如add.show等
            return if (enter) { //普通的进入的动作
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.slide_right_in
                )
            } else { //比如一个已经Fragmen被另一个replace，是一个进入动作，被replace的那个就是false
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.slide_left_out
                )
            }
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) { //表示一个退出动作，比如出栈，hide，detach等
            return if (enter) { //之前被replace的重新进入到界面或者Fragment回到栈顶
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.slide_left_in
                )
            } else { //Fragment退出，出栈
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.slide_right_out
                )
            }
        }
        return null
    }
}