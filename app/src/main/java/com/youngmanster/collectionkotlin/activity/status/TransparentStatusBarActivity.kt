package com.youngmanster.collectionkotlin.activity.status

import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collection_kotlin.utils.GlideUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_transparent_statusbar.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class TransparentStatusBarActivity :BaseActivity<BasePresenter<*>>(){
    override fun getLayoutId(): Int {
        return R.layout.activity_transparent_statusbar
    }

    override fun init() {
        DisplayUtils.setStatusBarFullTranslucent(this)

        GlideUtils.loadImg(
            this,
            "https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike272%2C5%2C5%2C272%2C90/sign=06f0367c57b5c9ea76fe0bb1b450dd65/d1a20cf431adcbef44627e71a0af2edda3cc9f76.jpg",
            R.mipmap.ic_bttom_loading_01, ivImage
        )

        GlideUtils.loadImgBlur(
            this,
            "https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike272%2C5%2C5%2C272%2C90/sign=06f0367c57b5c9ea76fe0bb1b450dd65/d1a20cf431adcbef44627e71a0af2edda3cc9f76.jpg",
            R.mipmap.ic_bttom_loading_01, imgBg
        )
    }

    override fun requestData() {
    }

    override fun isShowCustomActionBar(): Boolean {
        return false
    }

}