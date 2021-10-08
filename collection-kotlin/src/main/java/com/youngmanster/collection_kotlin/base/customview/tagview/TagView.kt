package com.youngmanster.collection_kotlin.base.customview.tagview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.youngmanster.collection_kotlin.base.customview.tagview.layout.FlexWrap
import com.youngmanster.collection_kotlin.base.customview.tagview.layout.FlexboxLayout
import com.youngmanster.collection_kotlin.base.customview.tagview.layout.JustifyContent
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collection_kotlin.utils.ColorUtils


/**
 * Created by yangy
 *2020-02-25
 *Describe:
 */
class TagView : FlexboxLayout {

    interface TagViewPressListener{
        fun onPress(view: View,title:String,position:Int)
    }

    private var mContext: Context? = null
    private var onTagViewPressListener:TagViewPressListener?=null

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.mContext = context
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        this.mContext = context
    }

    constructor(context: Context?) : super(context) {
        this.mContext = context
    }

    private var tagViewBuilder: TagViewConfigBuilder? = null

    fun create(tagViewBuilder: TagViewConfigBuilder,onTagViewPressListener:TagViewPressListener) {
        this.tagViewBuilder = tagViewBuilder
        this.onTagViewPressListener=onTagViewPressListener
        this.flexWrap=FlexWrap.WRAP

        when(tagViewBuilder.getTagViewAlign()){
            TagViewConfigBuilder.TagViewAlign.CENTER -> this.justifyContent=JustifyContent.CENTER
            TagViewConfigBuilder.TagViewAlign.LEFT -> this.justifyContent=JustifyContent.FLEX_START
            TagViewConfigBuilder.TagViewAlign.RIGHT -> this.justifyContent=JustifyContent.FLEX_END
        }


        if (tagViewBuilder.getTitles() != null) {
            for (i in 0 until tagViewBuilder.getTitles()!!.size) {
                this.addView(createNewFlexItemTextView(tagViewBuilder.getTitles()!![i], i))
            }
        }
    }


    /**
     * TagView
     * 动态创建TextView
     * @return
     */
    private fun createNewFlexItemTextView(str: String, pos: Int): TextView {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = str
        textView.textSize = tagViewBuilder?.getTextSize()!!

        textView.setTextColor(ColorUtils.createColorStateList(context,tagViewBuilder?.getTextSelectColor()!!,tagViewBuilder?.getTextSelectColor()!!,tagViewBuilder?.getTextColor()!!))
        textView.tag = pos

        textView.setBackgroundResource(tagViewBuilder?.getBackgroudRes()!!)

        textView.setOnClickListener { view ->
            val position = view.tag as Int
            onTagViewPressListener?.onPress(view,tagViewBuilder?.getTitles()!![position],position)
        }
        val padding = DisplayUtils.dip2px(tagViewBuilder?.getPaddingTopAndBottom()!!)
        val paddingLeftAndRight =
            DisplayUtils.dip2px(tagViewBuilder?.getPaddingLeftAndRight()!!)
        ViewCompat.setPaddingRelative(
            textView,
            paddingLeftAndRight,
            padding,
            paddingLeftAndRight,
            padding
        )
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val margin = DisplayUtils.dip2px(tagViewBuilder?.getMarginLeftAndRight()!!)
        val marginTopAndBottom =
            DisplayUtils.dip2px(tagViewBuilder?.getMarginTopAndBottom()!!)
        layoutParams.setMargins(margin, marginTopAndBottom, margin, marginTopAndBottom)
        textView.layoutParams = layoutParams
        return textView
    }

}