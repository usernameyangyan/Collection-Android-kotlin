package com.youngmanster.collectionkotlin.activity.status

import android.view.View
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.DisplayUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_change_statusbar.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class StatusBarColorActivity :BaseActivity<BasePresenter<*>>(),View.OnClickListener{

    private val colors =
        intArrayOf(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary)
    private var clickCount = 1
    private var type: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_color_statusbar
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.status_bar_bg))

        type = intent.getIntExtra("type", 0)
        if (type == 0) {
            DisplayUtils.setStatusBarBlackFontBgColor(this, colors[0])
        } else {
            DisplayUtils.setStatusBarColor(this, colors[0])
        }

        status_btn1.setOnClickListener(this)
    }

    override fun requestData() {
    }

    override fun onClick(v: View?) {
        when (type) {
            0 -> when {
                clickCount % 3 == 0 -> DisplayUtils.setStatusBarBlackFontBgColor(this, colors[0])
                clickCount % 3 == 1 -> DisplayUtils.setStatusBarBlackFontBgColor(this, colors[1])
                else -> DisplayUtils.setStatusBarBlackFontBgColor(this, colors[2])
            }
            1 -> when {
                clickCount % 3 == 0 -> DisplayUtils.setStatusBarColor(this, colors[0])
                clickCount % 3 == 1 -> DisplayUtils.setStatusBarColor(this, colors[1])
                else -> DisplayUtils.setStatusBarColor(this, colors[2])
            }
        }

        clickCount++
    }

}
