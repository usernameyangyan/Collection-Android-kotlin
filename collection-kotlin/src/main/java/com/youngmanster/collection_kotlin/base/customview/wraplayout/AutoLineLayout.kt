package com.youngmanster.collection_kotlin.base.customview.wraplayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */
class AutoLineLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    ViewGroup(context, attrs, defStyle) {

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutHorizontal()
    }

    private fun layoutHorizontal() {
        val count = childCount
        val lineWidth = (measuredWidth - paddingLeft
                - paddingRight)
        var paddingTop = paddingTop

        var childLeft = paddingLeft

        var availableLineWidth = lineWidth
        var maxLineHight = 0

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child == null) {
                continue
            } else if (child.visibility != View.GONE) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight

                if (availableLineWidth < childWidth) {
                    availableLineWidth = lineWidth
                    paddingTop += maxLineHight
                    childLeft = paddingLeft
                    maxLineHight = 0
                }
                var childTop = paddingTop
                setChildFrame(
                    child, childLeft, childTop, childWidth,
                    childHeight
                )
                childLeft += childWidth
                availableLineWidth -= childWidth
                maxLineHight = max(maxLineHight, childHeight)
            }
        }
    }

    private fun setChildFrame(
        child: View, left: Int, top: Int, width: Int,
        height: Int
    ) {
        child.layout(left, top, left + width, top + height)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val count = childCount
        for (i in 0 until count) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            val width =MeasureSpec.getSize(widthMeasureSpec)
            super.onMeasure(
                widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                    getDesiredHeight(width), MeasureSpec.EXACTLY
                )
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    private fun getDesiredHeight(width: Int): Int {
        val lineWidth = width - paddingLeft - paddingRight
        var availableLineWidth = lineWidth
        var totalHeight = paddingTop + paddingBottom
        var lineHeight = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            if (availableLineWidth < childWidth) {
                availableLineWidth = lineWidth
                totalHeight += lineHeight
                lineHeight = 0
            }
            availableLineWidth -= childWidth
            lineHeight = max(childHeight, lineHeight)
        }
        totalHeight += lineHeight
        return totalHeight
    }
}
