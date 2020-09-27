package com.youngmanster.collection_kotlin.base.baseview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.youngmanster.collection_kotlin.R
import com.youngmanster.collection_kotlin.base.set.LinkedMultiValueMap
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.ClassGetUtil
import kotlinx.android.synthetic.main.collection_library_default_base_activity.*
import java.util.*

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

@Suppress("UNCHECKED_CAST")
abstract class IBaseActivity<T : BasePresenter<*>> : AppCompatActivity() {

    private var isFirst = false
    var mPresenter: T? = null
    var customBar: View?=null
    val defineActionBarConfig:DefaultDefineActionBarConfig=DefaultDefineActionBarConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFManager = supportFragmentManager
        setContentView(R.layout.collection_library_default_base_activity)
        if (getLayoutId() != 0) {
            addContainerFrame(getLayoutId())
        }else{
            throw IllegalArgumentException("请设置getLayoutId")
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

        fun setBarBackground(bgId: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<LinearLayout>(R.id.common_bar_panel)?.setBackgroundResource(bgId)
            return this
        }

        fun setBarPadding(left:Int,top:Int,right:Int,bottom:Int):DefaultDefineActionBarConfig{
            (defaultDefineView?.findViewById<RelativeLayout>(R.id.inRootRel)?.layoutParams as LinearLayout.LayoutParams).setMargins(left,top,right,bottom)
            return this
        }

        fun setBarDividingLineHeight(height: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<View>(R.id.lineView)?.layoutParams?.height=height
            return this
        }

        fun setBarDividingLineBackground(bgId: Int): DefaultDefineActionBarConfig {
            defaultDefineView?.findViewById<View>(R.id.lineView)?.setBackgroundResource(bgId)
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
        val view = LayoutInflater.from(this).inflate(layoutResID, frame_content_container, false)
        frame_content_container.addView(view)
    }


    fun <U> startAc(claz:Class<U>){
        var intent = Intent()
        intent.setClass(this,claz)
        startActivity(intent)
    }

    override fun getDelegate(): AppCompatDelegate {
        return SkinAppCompatDelegateImpl.get(this, this)
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



    /***************************Fragment设置**************************************/

    companion object{
        const val REQUEST_CODE_INVALID=-1
        class FragmentStackEntity {
            var isSticky = false
            var requestCode: Int = REQUEST_CODE_INVALID

            @ResultCode
            var resultCode = RESULT_CANCELED
            var result: Bundle? = null
        }
    }

    private var mFManager: FragmentManager? = null
    private val mFragmentStack: MutableList<IBaseFragment<*>> =
        ArrayList()
    private val fragmentStack = LinkedMultiValueMap<String,IBaseFragment<*>>()
    private val mFragmentEntityMap: LinkedMultiValueMap<IBaseFragment<*>, FragmentStackEntity> =
        LinkedMultiValueMap()


    /**
     * 是否是根Fragment
     * @return
     */
    open fun isRootFragment(): Boolean {
        return mFragmentStack.size <= 1
    }

    /**
     * When the back off.
     */
    private fun onBackStackFragment(): Boolean {
        if (mFragmentStack.size > 1) {
            mFManager!!.popBackStack()
            val inFragment: IBaseFragment<*> = mFragmentStack[mFragmentStack.size - 2]
            val fragmentTransaction: FragmentTransaction = mFManager!!.beginTransaction()
            fragmentTransaction.show(inFragment)
            fragmentTransaction.commit()
            val outFragment: IBaseFragment<*> = mFragmentStack[mFragmentStack.size - 1]
            inFragment.onResume()
            var stackEntity:FragmentStackEntity?=null
            if(mFragmentEntityMap.getValues(outFragment)!=null){
                stackEntity = mFragmentEntityMap.getValue(outFragment,getIndex(mFragmentEntityMap.getValues(outFragment).size))
            }
            mFragmentStack.remove(outFragment)

            if(mFragmentEntityMap.getValues(outFragment)!=null){
                mFragmentEntityMap.remove(outFragment,getIndex(mFragmentEntityMap.getValues(outFragment).size))
            }else{
                mFragmentEntityMap.remove(outFragment)
            }

            if(fragmentStack.getValues(outFragment.javaClass.simpleName)!=null){
                fragmentStack.remove(outFragment.javaClass.simpleName,getIndex(fragmentStack.getValues(outFragment.javaClass.simpleName).size))
            }else{
                fragmentStack.remove(outFragment.javaClass.simpleName)
            }

            if (stackEntity!=null&&stackEntity.requestCode != REQUEST_CODE_INVALID) {
                inFragment.onFragmentResult(
                    stackEntity.requestCode,
                    stackEntity.resultCode,
                    stackEntity.result
                )
            }
            return true
        }
        return false
    }

    override fun onBackPressed() {
        if(mFragmentStack.size>0){
            val fragment: IBaseFragment<*> = mFragmentStack[mFragmentStack.size - 1]
            val isBack=fragment.onBackPressed()
            if(isBack){
                if (!onBackStackFragment()) {
                    finish()
                }
            }
        }else{
            if (!onBackStackFragment()) {
                finish()
            }
        }

    }


    private fun getIndex(currentIndex:Int):Int{
        return if(currentIndex-1<=0){
            0
        }else{
            currentIndex-1
        }
    }


    /**
     * Show a fragment.
     *
     * @param clazz fragment class.
     */
    fun <T : IBaseFragment<*>> startFragment(clazz: Class<T>) {
        try {
            val targetFragment:  IBaseFragment<*> = clazz.newInstance()
            startFragment< IBaseFragment<*>>(
                null,
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
     * @param stickyStack sticky to back stack.
     */
    fun <T :  IBaseFragment<*>> startFragment(
        clazz: Class<T>,
        isSkipAnimation: Boolean
    ) {
        try {
            val targetFragment:  IBaseFragment<*> = clazz.newInstance()
            startFragment(
                null,
                targetFragment,
                true,
                REQUEST_CODE_INVALID,
                isSkipAnimation
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
    </T> */
    fun <T :  IBaseFragment<*>> startFragment(targetFragment: T) {
        startFragment<T>(
            null,
            targetFragment,
            true,
            REQUEST_CODE_INVALID,
            false
        )
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
        startFragment<T>(
            null,
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
        require(requestCode !=REQUEST_CODE_INVALID) { "The requestCode must be positive integer." }
        try {
            val targetFragment: IBaseFragment<*> = clazz.newInstance()
            startFragment(null, targetFragment, true, requestCode,false)
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
        require(requestCode !=REQUEST_CODE_INVALID) { "The requestCode must be positive integer." }
        try {
            val targetFragment: IBaseFragment<*> = clazz.newInstance()
            startFragment(null, targetFragment, true, requestCode,isSkipAnimation)
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
    fun <T : IBaseFragment<*>> startFragmentForResult(targetFragment: T, requestCode: Int) {
        require(requestCode != REQUEST_CODE_INVALID) { "The requestCode must be positive integer." }
        startFragment(null, targetFragment, true, requestCode,false)
    }


    /**
     * Show a fragment for result.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.

    </T> */
    fun <T : IBaseFragment<*>> startFragmentForResult(targetFragment: T, requestCode: Int,isSkipAnimation: Boolean) {
        require(requestCode != REQUEST_CODE_INVALID) { "The requestCode must be positive integer." }
        startFragment(null, targetFragment, true, requestCode,isSkipAnimation)
    }

    /**
     * Show a fragment.
     *
     * @param thisFragment Now show fragment, can be null.
     * @param thatFragment fragment to display.
     * @param stickyStack  sticky back stack.
     * @param requestCode  requestCode.
    </T> */
    fun <T : IBaseFragment<*>> startFragment(
        thisFragment: T?, thatFragment: T,
        stickyStack: Boolean, requestCode: Int,
        isSkipAnimation:Boolean
    ) {
        var fragmentTransaction = mFManager!!.beginTransaction()
        if (thisFragment != null) {
            var thisStackEntity:FragmentStackEntity?=null
            if(mFragmentEntityMap.getValues(thisFragment)!=null){
                thisStackEntity = mFragmentEntityMap.getValue(thisFragment,getIndex(mFragmentEntityMap.getValues(thisFragment).size))
            }
            if (thisStackEntity != null) {
                if (thisStackEntity.isSticky) {
                    thisFragment.onPause()
                    thisFragment.onStop()
                    fragmentTransaction.hide(thisFragment)
                    if(isSkipAnimation){
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    }

                } else {
                    fragmentTransaction.remove(thisFragment).commit()
                    if(isSkipAnimation){
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    }
                    fragmentTransaction.commitNow()
                    fragmentTransaction = mFManager!!.beginTransaction()
                    mFragmentStack.remove(thisFragment)

                    if(mFragmentEntityMap.getValues(thisFragment)!=null){
                        mFragmentEntityMap.remove(thisFragment,getIndex(mFragmentEntityMap.getValues(thisFragment).size))
                    }else{
                        mFragmentEntityMap.remove(thisFragment)
                    }

                    if(fragmentStack.getValues(thisFragment.javaClass.simpleName)!=null){
                        fragmentStack.remove(thisFragment.javaClass.simpleName,getIndex(fragmentStack.getValues(thisFragment.javaClass.simpleName).size))
                    }else{
                        fragmentStack.remove(thisFragment.javaClass.simpleName)
                    }
                }
            }
        }
        val fragmentTag: String =
            thatFragment.javaClass.simpleName
        fragmentTransaction.add(fragmentLayoutId(), thatFragment, fragmentTag)
        if(isSkipAnimation){
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }
        fragmentTransaction.addToBackStack(fragmentTag)
        fragmentTransaction.commit()
        mFManager?.executePendingTransactions()
        val fragmentStackEntity = FragmentStackEntity()
        fragmentStackEntity.isSticky = stickyStack
        fragmentStackEntity.requestCode = requestCode
        thatFragment.setStackEntity(fragmentStackEntity)
        mFragmentEntityMap.add(thatFragment,fragmentStackEntity)
        mFragmentStack.add(thatFragment)
        fragmentStack.add(fragmentTag,thatFragment)
    }

    fun <T : IBaseFragment<*>> findFragment(clazz: Class<T>):T?{
        val fragmentTag: String =
            clazz.newInstance().javaClass.simpleName

        if(fragmentStack.getValues(fragmentTag)!=null&&fragmentStack.getValue(fragmentTag,getIndex(fragmentStack.getValues(fragmentTag).size))!=null){
            return fragmentStack.getValue(fragmentTag,getIndex(fragmentStack.getValues(fragmentTag).size)) as T
        }
        return null
    }

    open fun fragmentLayoutId(): Int{
        return 0
    }

}