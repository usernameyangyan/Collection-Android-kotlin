package com.youngmanster.collection_kotlin.base.customview.tagview

import com.youngmanster.collection_kotlin.R

/**
 * Created by yangy
 *2020-02-25
 *Describe:
 */

class TagViewConfigBuilder {

    enum class TagViewAlign{
        LEFT,RIGHT,CENTER
    }

    private var titles:Array<String>?=null
    private var textSize:Float=14f
    private var textColor:Int=R.color.colorAccent
    private var textSelectColor:Int=R.color.black
    private var paddingLeftAndRight:Float=10f
    private var paddingTopAndBottom:Float=6f
    private var marginLeftAndRight:Float=6f
    private var marginTopAndBottom:Float=8f
    private var backgroudRes:Int=R.drawable.collection_library_selector_tagview
    private var tagViewAlign:TagViewAlign=TagViewAlign.CENTER

    fun setTitles(titles:Array<String>?):TagViewConfigBuilder{
        this.titles=titles
        return this
    }

    fun getTitles():Array<String>?{
        return titles
    }

    fun setTextSize(textSize:Float):TagViewConfigBuilder{
        this.textSize=textSize
        return this
    }

    fun getTextSize():Float{
        return textSize
    }

    fun setTextColor(textColor:Int):TagViewConfigBuilder{
        this.textColor=textColor
        return this
    }

    fun getTextColor():Int{
        return textColor
    }

    fun setTextSelectColor(textColor:Int):TagViewConfigBuilder{
        this.textSelectColor=textColor
        return this
    }

    fun getTextSelectColor():Int{
        return textSelectColor
    }

    fun setPaddingLeftAndRight(padding:Float):TagViewConfigBuilder{
        this.paddingLeftAndRight=padding
        return this
    }

    fun getPaddingLeftAndRight():Float{
        return paddingLeftAndRight
    }

    fun setPaddingTopAndBottom(padding:Float):TagViewConfigBuilder{
        this.paddingTopAndBottom=padding
        return this
    }

    fun getPaddingTopAndBottom():Float{
        return paddingTopAndBottom
    }

    fun setMarginAndTopBottom(margin:Float):TagViewConfigBuilder{
        this.marginTopAndBottom=margin
        return this
    }

    fun getMarginTopAndBottom():Float{
        return marginTopAndBottom
    }

    fun setMarginLeftAndRight(margin:Float):TagViewConfigBuilder{
        this.marginLeftAndRight=margin
        return this
    }

    fun getMarginLeftAndRight():Float{
        return marginLeftAndRight
    }

    fun setBackgroudDrawableRes(backgroudRes:Int):TagViewConfigBuilder{
        this.backgroudRes=backgroudRes
        return this
    }

    fun getBackgroudRes():Int{
        return backgroudRes
    }

    fun setTagViewAlign(tagViewAlign:TagViewAlign):TagViewConfigBuilder{
        this.tagViewAlign=tagViewAlign
        return this
    }

    fun getTagViewAlign():TagViewAlign{
        return tagViewAlign
    }

}