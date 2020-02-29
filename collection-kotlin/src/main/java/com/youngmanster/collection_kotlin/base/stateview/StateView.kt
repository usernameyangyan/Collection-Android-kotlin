package com.youngmanster.collection_kotlin.base.stateview

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.youngmanster.collection_kotlin.R
import com.youngmanster.collection_kotlin.utils.LogUtils
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message


/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class StateView @JvmOverloads constructor(
    context: Context, @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.styleStateView
) :
    LinearLayout(context, attrs, defStyleAttr) {


    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            animationDrawable!!.start()
        }
    }

    //加载控件
    private var mLoadingView: View? = null
    private val loadingViewDrawable: Int
    private val loadingText: String?
    //空布局
    private val mEmptyImageId: Int
    private val mEmptyText: String?
    private var mEmptyView: View? = null
    private val mEmptyViewRes: Int
    //无网络
    private val mDisConnectImageId: Int
    private val mDisConnectText: String?
    private var mDisConnectView: View? = null

    private val mTextColor: Int
    private val mTextSize: Int

    private val mInflater: LayoutInflater
    private val params: ViewGroup.LayoutParams
    private var mEmptyViewListener: OnEmptyViewListener? = null
    private var mDisConnectViewListener: OnDisConnectListener? = null
    //动画
    private var animationDrawable: AnimationDrawable? = null

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.StateView,
            defStyleAttr,
            R.style.StateView_Config
        )

        loadingViewDrawable =
            typedArray.getResourceId(R.styleable.StateView_loadingViewAnimation, View.NO_ID)
        loadingText = typedArray.getString(R.styleable.StateView_loadingText)

        mEmptyImageId = typedArray.getResourceId(R.styleable.StateView_emptyImage, View.NO_ID)
        mEmptyText = typedArray.getString(R.styleable.StateView_emptyText)
        mEmptyViewRes = typedArray.getResourceId(R.styleable.StateView_emptyViewRes, View.NO_ID)

        mDisConnectImageId =
            typedArray.getResourceId(R.styleable.StateView_disConnectImage, View.NO_ID)
        mDisConnectText = typedArray.getString(R.styleable.StateView_disConnectText)

        mTextColor = typedArray.getColor(R.styleable.StateView_tipTextColor, -0x76000000)
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.StateView_tipTextSize, 14)

        typedArray.recycle()

        mInflater = LayoutInflater.from(context)
        params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))

        setLoadingView()
        if (mEmptyViewRes == View.NO_ID) {
            setEmptyView()
        } else {
            setEmptyView(mEmptyViewRes)
        }
        setDisConnectView()
        showViewByState(STATE_NO_DATA)
    }

    /**
     * 显示加载中的状态
     */
    private fun setLoadingView() {

        if (mLoadingView == null) {
            mLoadingView = mInflater.inflate(R.layout.collection_library_view_loading, null)
            val loadMore_Ll = mLoadingView!!.findViewById<LinearLayout>(R.id.library_loadMore_Ll)
            val loadingBar = mLoadingView!!.findViewById<ProgressBar>(R.id.library_loadingBar)
            val loadingIv = mLoadingView!!.findViewById<ImageView>(R.id.library_loadingIv)
            val loadingTv = mLoadingView!!.findViewById<TextView>(R.id.library_loadingTv)

            if (loadingViewDrawable != View.NO_ID) {
                loadingIv.setBackgroundResource(loadingViewDrawable)
                loadingBar.visibility = View.GONE
                loadMore_Ll.visibility = View.VISIBLE
                loadingTv.text = loadingText
                loadingTv.setTextColor(mTextColor)
                loadingTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
                animationDrawable = loadingIv.background as AnimationDrawable?

                if (animationDrawable != null) {
                    handler.sendEmptyMessage(1000)


                }

            } else {
                loadingBar.visibility = View.VISIBLE
                loadMore_Ll.visibility = View.GONE
            }
            addView(mLoadingView, VIEW_POSITION, params)
        }
    }

    /**
     * 显示无数据状态
     */
    private fun setEmptyView() {
        if (mEmptyView == null) {
            mEmptyView = mInflater.inflate(R.layout.collection_library_view_empty, null)
            val emptyImage = mEmptyView!!.findViewById<ImageView>(R.id.library_empty_image)
            val emptyText = mEmptyView!!.findViewById<TextView>(R.id.library_empty_text)
            if (null != emptyImage && mEmptyImageId != View.NO_ID) {
                emptyImage.setImageResource(mEmptyImageId)
            }

            if (null != emptyText && !TextUtils.isEmpty(mEmptyText)) {
                emptyText.text = mEmptyText
                emptyText.setTextColor(mTextColor)
                emptyText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            }

            addView(mEmptyView, VIEW_POSITION, params)

        }

        mEmptyView!!.setOnClickListener {
            if (null != mEmptyViewListener) {
                mEmptyViewListener!!.onEmptyViewClick()
            }
        }
    }


    /**
     * 自定义布局
     * @param emptyRes
     */
    private fun setEmptyView(emptyRes: Int) {
        if (mEmptyView == null) {
            mEmptyView = mInflater.inflate(emptyRes, null)
            addView(mEmptyView, VIEW_POSITION, params)
        }

        mEmptyView!!.setOnClickListener {
            if (null != mEmptyViewListener) {
                mEmptyViewListener!!.onEmptyViewClick()
            }
        }
    }

    /**
     * 获取无数据状态View
     * @return
     */
    fun getmEmptyView(): View? {
        return mEmptyView
    }


    /**
     * 显示无网络
     */
    private fun setDisConnectView() {
        if (mDisConnectView == null) {
            mDisConnectView = mInflater.inflate(R.layout.collection_library_view_disconnect, null)
            val disConnectImage =
                mDisConnectView!!.findViewById<ImageView>(R.id.library_disconnect_image)
            val disConnectText =
                mDisConnectView!!.findViewById<TextView>(R.id.library_disconnect_text)
            if (null != disConnectImage && mDisConnectImageId != View.NO_ID) {
                disConnectImage.setImageResource(mDisConnectImageId)
            }

            if (null != disConnectText && !TextUtils.isEmpty(mDisConnectText)) {
                disConnectText.text = mDisConnectText
                disConnectText.setTextColor(mTextColor)
                disConnectText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
            }

            addView(mDisConnectView, VIEW_POSITION, params)
        }

        mDisConnectView!!.setOnClickListener {
            if (null != mDisConnectViewListener) {
                mDisConnectViewListener!!.onDisConnectViewClick()
            }
        }
    }


    /**
     * 设置状态
     *
     * @param state
     */
    fun showViewByState(state: Int) {
        //如果当前状态为加载成功，隐藏此View，反之显示
        this.visibility = if (state == STATE_NO_DATA) View.GONE else View.VISIBLE

        if (state == STATE_NO_DATA) {
            if (animationDrawable != null) {
                animationDrawable!!.stop()
            }
        }

        if (null != mLoadingView) {
            mLoadingView!!.visibility = if (state == STATE_LOADING) View.VISIBLE else View.GONE
        }

        if (null != mEmptyView) {
            mEmptyView!!.visibility = if (state == STATE_EMPTY) View.VISIBLE else View.GONE
        }

        if (null != mDisConnectView) {
            mDisConnectView!!.visibility =
                if (state == STATE_DISCONNECT) View.VISIBLE else View.GONE
        }
    }

    /**
     * ============================空布局点击监听=================================================
     */

    fun setOnEmptyViewListener(listener: OnEmptyViewListener) {
        mEmptyViewListener = listener
    }

    interface OnEmptyViewListener {
        fun onEmptyViewClick()
    }

    /**
     * ============================网络连接失败=================================================
     */

    fun setOnDisConnectViewListener(listener: OnDisConnectListener) {
        mDisConnectViewListener = listener
    }

    interface OnDisConnectListener {
        fun onDisConnectViewClick()
    }

    companion object {

        //当前的加载状态
        const val STATE_NO_DATA = 0
        const val STATE_LOADING = 1
        const val STATE_EMPTY = 2
        const val STATE_DISCONNECT = 3

        //布局添加位置
        const val VIEW_POSITION = 0
    }
}
