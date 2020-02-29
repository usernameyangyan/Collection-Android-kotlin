package com.youngmanster.collectionkotlin.activity.customview

import android.view.View
import com.youngmanster.collection_kotlin.base.customview.tagview.TagView
import com.youngmanster.collection_kotlin.base.customview.tagview.TagViewConfigBuilder
import com.youngmanster.collection_kotlin.base.customview.tagview.layout.AlignContent
import com.youngmanster.collection_kotlin.base.customview.tagview.layout.AlignItems
import com.youngmanster.collection_kotlin.base.customview.tagview.layout.FlexDirection
import com.youngmanster.collection_kotlin.base.customview.tagview.layout.JustifyContent
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.LogUtils
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_tagview.*

/**
 * Created by yangy
 *2020-02-25
 *Describe:
 */

class TagViewActivity :BaseActivity<BasePresenter<*>>(){
    override fun getLayoutId(): Int {
        return R.layout.activity_tagview
    }

    override fun init() {

        defineActionBarConfig!!.setTitle("TagView")

        var list= arrayOf("werwrw","4545465","金浩","风和日丽",
            "一只蜜蜂叮在挂历上","阳光","灿烂","1+1","浏览器","玲珑骰子安红豆，入骨相思知不知")

        var builder=TagViewConfigBuilder()
            .setTitles(list)



        tagView.create(builder,object :TagView.TagViewPressListener{
            override fun onPress(view: View, title: String, position: Int) {
                ToastUtils.showToast(this@TagViewActivity,title)
            }
        })

    }

    override fun requestData() {
    }

}